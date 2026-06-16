package copamundo.selecoes.controle;

import copamundo.comum.Jogador;
import copamundo.comum.Selecao;
import copamundo.comum.StatusJogador;
import copamundo.selecoes.excecoes.ElencoCheioException;
import copamundo.selecoes.excecoes.JogadorNaoEncontradoException;
import copamundo.selecoes.excecoes.PersistenciaException;
import copamundo.selecoes.excecoes.SelecaoNaoEncontradaException;
import copamundo.selecoes.persistencia.PersistenciaSelecoesJogadores;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestaoSelecoesJogadores {

    public static final int MIN_JOGADORES = 18;
    public static final int MAX_JOGADORES = 26;

    private final PersistenciaSelecoesJogadores persistencia;

    private List<Selecao> cacheSelecoes = null;
    private List<Jogador> cacheJogadores = null;

    public GestaoSelecoesJogadores() {
        this.persistencia = new PersistenciaSelecoesJogadores();
    }

    public GestaoSelecoesJogadores(PersistenciaSelecoesJogadores persistencia) {
        this.persistencia = persistencia;
    }

    public void cadastrarSelecao(Selecao selecao) throws IllegalArgumentException, PersistenciaException {
        validarCamposSelecao(selecao);
        boolean jaExiste = selecoes().stream()
                .anyMatch(s -> s.getPais().equalsIgnoreCase(selecao.getPais()));
        if (jaExiste)
            throw new IllegalArgumentException("Já existe uma seleção cadastrada para o país: " + selecao.getPais());
        persistencia.salvarSelecao(selecao);
        limparCache();
    }

    public void editarSelecao(String pais, Selecao novosDados)
            throws SelecaoNaoEncontradaException, PersistenciaException {
        Selecao selecao = buscarSelecaoOuErro(pais);
        if (novosDados.getGrupo() != null && !novosDados.getGrupo().isBlank())
            selecao.setGrupo(novosDados.getGrupo());
        if (novosDados.getTecnico() != null && !novosDados.getTecnico().isBlank())
            selecao.setTecnico(novosDados.getTecnico());
        persistencia.salvarSelecao(selecao);
        limparCache();
    }

    public void excluirSelecao(String pais)
            throws SelecaoNaoEncontradaException, IllegalStateException, PersistenciaException {
        buscarSelecaoOuErro(pais);
        long jogadoresVinculados = jogadores().stream()
                .filter(j -> j.getPaisSelecao().equalsIgnoreCase(pais)).count();
        if (jogadoresVinculados > 0)
            throw new IllegalStateException(
                    "Não é possível excluir a seleção pois ela possui " + jogadoresVinculados + " jogador(es) vinculado(s).");
        persistencia.excluirSelecao(pais);
        limparCache();
    }

    public List<Selecao> listarSelecoes() throws PersistenciaException {
        return new ArrayList<>(selecoes());
    }

    public List<Selecao> buscarSelecoes(String pais, String tecnico, String grupo) throws PersistenciaException {
        return selecoes().stream()
                .filter(s -> pais == null || pais.isBlank() || s.getPais().toLowerCase().contains(pais.toLowerCase()))
                .filter(s -> tecnico == null || tecnico.isBlank() || s.getTecnico().toLowerCase().contains(tecnico.toLowerCase()))
                .filter(s -> grupo == null || grupo.isBlank() || grupo.equalsIgnoreCase("Todos") || s.getGrupo().equalsIgnoreCase(grupo))
                .collect(Collectors.toList());
    }

    public List<Selecao> buscarSelecoesPorGrupo(String grupo) throws PersistenciaException {
        return buscarSelecoes(null, null, grupo);
    }

    public List<Selecao> buscarSelecoesPorPais(String texto) throws PersistenciaException {
        return buscarSelecoes(texto, null, null);
    }

    public List<Selecao> buscarSelecoesPorTecnico(String texto) throws PersistenciaException {
        return buscarSelecoes(null, texto, null);
    }

    public void cadastrarJogador(Jogador jogador)
            throws ElencoCheioException, IllegalArgumentException, SelecaoNaoEncontradaException, PersistenciaException {
        validarCamposJogador(jogador);
        buscarSelecaoOuErro(jogador.getPaisSelecao());
        List<Jogador> elenco = jogadoresDaSelecao(jogador.getPaisSelecao());
        if (elenco.size() >= MAX_JOGADORES)
            throw new ElencoCheioException(
                    "O elenco da seleção " + jogador.getPaisSelecao() + " já está cheio (" + MAX_JOGADORES + " jogadores).");
        if (elenco.stream().anyMatch(j -> j.getNumero() == jogador.getNumero()))
            throw new IllegalArgumentException("O número " + jogador.getNumero() + " já está em uso nesta seleção.");
        persistencia.salvarJogador(jogador);
        limparCache();
    }

    public void editarJogador(String idOriginal, Jogador novosDados)
            throws JogadorNaoEncontradoException, IllegalArgumentException, PersistenciaException {
        Jogador jogador = buscarJogadorOuErro(idOriginal);

        int novoNumero = novosDados.getNumero() > 0 ? novosDados.getNumero() : jogador.getNumero();
        String novoPais = (novosDados.getPaisSelecao() != null && !novosDados.getPaisSelecao().isBlank())
                ? novosDados.getPaisSelecao() : jogador.getPaisSelecao();

        // Verifica se o novo número já está ocupado por outro jogador da mesma seleção
        if (novoNumero != jogador.getNumero() || !novoPais.equals(jogador.getPaisSelecao())) {
            boolean numeroDuplicado = jogadores().stream()
                    .filter(j -> j.getPaisSelecao().equalsIgnoreCase(novoPais))
                    .filter(j -> !j.getId().equals(idOriginal))
                    .anyMatch(j -> j.getNumero() == novoNumero);
            if (numeroDuplicado)
                throw new IllegalArgumentException("O número " + novoNumero + " já está em uso nesta seleção.");
        }

        if (novosDados.getNome() != null && !novosDados.getNome().isBlank()) jogador.setNome(novosDados.getNome());
        if (novosDados.getPosicao() != null && !novosDados.getPosicao().isBlank()) jogador.setPosicao(novosDados.getPosicao());
        if (novosDados.getNumero() > 0) jogador.setNumero(novosDados.getNumero());
        if (novosDados.getIdade() > 0) jogador.setIdade(novosDados.getIdade());
        if (novosDados.getStatus() != null) jogador.setStatus(novosDados.getStatus());
        if (novosDados.getPaisSelecao() != null && !novosDados.getPaisSelecao().isBlank())
            jogador.setPaisSelecao(novosDados.getPaisSelecao());

        if (!idOriginal.equals(jogador.getId()))
            persistencia.excluirJogador(idOriginal);
        persistencia.salvarJogador(jogador);
        limparCache();
    }

    public void excluirJogador(String idJogador)
            throws JogadorNaoEncontradoException, IllegalStateException, PersistenciaException {
        Jogador jogador = buscarJogadorOuErro(idJogador);
        long totalNoElenco = jogadores().stream()
                .filter(j -> j.getPaisSelecao().equalsIgnoreCase(jogador.getPaisSelecao())).count();
        if (totalNoElenco <= MIN_JOGADORES)
            throw new IllegalStateException(
                    "Não é possível excluir: o elenco já está no mínimo de " + MIN_JOGADORES + " jogadores.");
        persistencia.excluirJogador(idJogador);
        limparCache();
    }

    public List<Jogador> listarTodosJogadores() throws PersistenciaException {
        return new ArrayList<>(jogadores());
    }

    public List<Jogador> jogadoresDaSelecao(String pais) throws PersistenciaException {
        return jogadores().stream()
                .filter(j -> j.getPaisSelecao().equalsIgnoreCase(pais))
                .collect(Collectors.toList());
    }

    public List<Jogador> buscarJogadoresPorSelecaoPais(String pais) throws PersistenciaException {
        return jogadoresDaSelecao(pais);
    }

    public List<Jogador> listarJogadoresPorSelecao(Selecao selecao) throws PersistenciaException {
        return jogadoresDaSelecao(selecao.getPais());
    }

    public List<Jogador> buscarJogadores(String nome, String posicao, StatusJogador status, String paisSelecao)
            throws PersistenciaException {
        return jogadores().stream()
                .filter(j -> nome == null || nome.isBlank() || j.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(j -> posicao == null || posicao.isBlank() || posicao.equalsIgnoreCase("Todas") || j.getPosicao().equalsIgnoreCase(posicao))
                .filter(j -> status == null || j.getStatus() == status)
                .filter(j -> paisSelecao == null || paisSelecao.isBlank() || j.getPaisSelecao().equalsIgnoreCase(paisSelecao))
                .collect(Collectors.toList());
    }

    public List<Jogador> buscarJogadoresPorNome(String texto) throws PersistenciaException {
        return buscarJogadores(texto, null, null, null);
    }

    public List<Jogador> buscarJogadoresPorPosicao(String posicao) throws PersistenciaException {
        return buscarJogadores(null, posicao, null, null);
    }

    public List<Jogador> buscarJogadoresPorStatus(StatusJogador status) throws PersistenciaException {
        return buscarJogadores(null, null, status, null);
    }

    public int contarJogadoresDaSelecao(String pais) throws PersistenciaException {
        return (int) jogadores().stream()
                .filter(j -> j.getPaisSelecao().equalsIgnoreCase(pais)).count();
    }

    private List<Selecao> selecoes() throws PersistenciaException {
        if (cacheSelecoes == null) cacheSelecoes = persistencia.carregarSelecoes();
        return cacheSelecoes;
    }

    private List<Jogador> jogadores() throws PersistenciaException {
        if (cacheJogadores == null) cacheJogadores = persistencia.carregarJogadores();
        return cacheJogadores;
    }

    private void limparCache() {
        cacheSelecoes = null;
        cacheJogadores = null;
    }

    private Selecao buscarSelecaoOuErro(String pais) throws SelecaoNaoEncontradaException, PersistenciaException {
        return selecoes().stream()
                .filter(s -> s.getPais().equalsIgnoreCase(pais))
                .findFirst()
                .orElseThrow(() -> new SelecaoNaoEncontradaException("Seleção não encontrada: " + pais));
    }

    private Jogador buscarJogadorOuErro(String id) throws JogadorNaoEncontradoException, PersistenciaException {
        return jogadores().stream()
                .filter(j -> j.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new JogadorNaoEncontradoException("Jogador não encontrado: " + id));
    }

    private void validarCamposSelecao(Selecao selecao) {
        if (selecao.getPais() == null || selecao.getPais().isBlank())
            throw new IllegalArgumentException("O campo País é obrigatório.");
        if (selecao.getGrupo() == null || selecao.getGrupo().isBlank())
            throw new IllegalArgumentException("O campo Grupo é obrigatório.");
        if (selecao.getTecnico() == null || selecao.getTecnico().isBlank())
            throw new IllegalArgumentException("O campo Técnico é obrigatório.");
    }

    private void validarCamposJogador(Jogador jogador) {
        if (jogador.getNome() == null || jogador.getNome().isBlank())
            throw new IllegalArgumentException("O campo Nome é obrigatório.");
        if (jogador.getPosicao() == null || jogador.getPosicao().isBlank())
            throw new IllegalArgumentException("O campo Posição é obrigatório.");
        if (jogador.getNumero() <= 0 || jogador.getNumero() > 99)
            throw new IllegalArgumentException("O número da camisa deve ser entre 1 e 99.");
        if (jogador.getIdade() <= 0 || jogador.getIdade() > 60)
            throw new IllegalArgumentException("A idade informada é inválida.");
        if (jogador.getStatus() == null)
            throw new IllegalArgumentException("O status do jogador é obrigatório.");
        if (jogador.getPaisSelecao() == null || jogador.getPaisSelecao().isBlank())
            throw new IllegalArgumentException("O jogador deve estar vinculado a uma seleção.");
    }
}
