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

/**
 * Controlador central do módulo de Seleções e Jogadores.
 *
 * Melhorias implementadas:
 * - Cache em memória: evita múltiplas leituras de disco por operação (melhoria 2)
 * - Injeção de dependência via construtor (melhoria 5)
 * - editarJogador revalida número duplicado (melhoria 3)
 * - MIN_JOGADORES aplicado: bloqueia exclusão abaixo de 18 (melhoria 4)
 * - Filtros de consulta combinados/cumulativos (melhoria 7)
 */
public class GestaoSelecoesJogadores {

    public static final int MIN_JOGADORES = 18;
    public static final int MAX_JOGADORES = 26;

    private final PersistenciaSelecoesJogadores persistencia;

    // Cache em memória — evita reler o arquivo a cada operação
    private List<Selecao> cacheSelecoes = null;
    private List<Jogador> cacheJogadores = null;

    /** Construtor padrão — cria sua própria persistência. */
    public GestaoSelecoesJogadores() {
        this.persistencia = new PersistenciaSelecoesJogadores();
    }

    /** Construtor com injeção de dependência — facilita testes. */
    public GestaoSelecoesJogadores(PersistenciaSelecoesJogadores persistencia) {
        this.persistencia = persistencia;
    }

    // ========== SELEÇÕES ==========

    public void cadastrarSelecao(Selecao selecao) throws IllegalArgumentException, PersistenciaException {
        validarCamposSelecao(selecao);
        boolean jaExiste = getSelecoes().stream()
                .anyMatch(s -> s.getPais().equalsIgnoreCase(selecao.getPais()));
        if (jaExiste)
            throw new IllegalArgumentException("Já existe uma seleção cadastrada para o país: " + selecao.getPais());
        persistencia.salvarSelecao(selecao);
        invalidarCache();
    }

    public void editarSelecao(String pais, Selecao novosDados)
            throws SelecaoNaoEncontradaException, PersistenciaException {
        Selecao existente = buscarSelecaoOuLancarExcecao(pais);
        if (novosDados.getGrupo() != null && !novosDados.getGrupo().isBlank())
            existente.setGrupo(novosDados.getGrupo());
        if (novosDados.getTecnico() != null && !novosDados.getTecnico().isBlank())
            existente.setTecnico(novosDados.getTecnico());
        persistencia.salvarSelecao(existente);
        invalidarCache();
    }

    public void excluirSelecao(String pais)
            throws SelecaoNaoEncontradaException, IllegalStateException, PersistenciaException {
        buscarSelecaoOuLancarExcecao(pais);
        long vinculados = getJogadores().stream()
                .filter(j -> j.getPaisSelecao().equalsIgnoreCase(pais)).count();
        if (vinculados > 0)
            throw new IllegalStateException(
                    "Não é possível excluir a seleção pois ela possui " + vinculados + " jogador(es) vinculado(s).");
        persistencia.excluirSelecao(pais);
        invalidarCache();
    }

    public List<Selecao> listarSelecoes() throws PersistenciaException {
        return new ArrayList<>(getSelecoes());
    }

    /**
     * Busca seleções combinando todos os critérios preenchidos (melhoria 7).
     * Todos os parâmetros são opcionais — null ou vazio = ignorar critério.
     */
    public List<Selecao> buscarSelecoes(String pais, String tecnico, String grupo) throws PersistenciaException {
        return getSelecoes().stream()
                .filter(s -> pais == null || pais.isBlank()
                        || s.getPais().toLowerCase().contains(pais.toLowerCase()))
                .filter(s -> tecnico == null || tecnico.isBlank()
                        || s.getTecnico().toLowerCase().contains(tecnico.toLowerCase()))
                .filter(s -> grupo == null || grupo.isBlank() || grupo.equalsIgnoreCase("Todos")
                        || s.getGrupo().equalsIgnoreCase(grupo))
                .collect(Collectors.toList());
    }

    // Mantidos para compatibilidade
    public List<Selecao> buscarSelecoesPorGrupo(String grupo) throws PersistenciaException {
        return buscarSelecoes(null, null, grupo);
    }
    public List<Selecao> buscarSelecoesPorPais(String texto) throws PersistenciaException {
        return buscarSelecoes(texto, null, null);
    }
    public List<Selecao> buscarSelecoesPorTecnico(String texto) throws PersistenciaException {
        return buscarSelecoes(null, texto, null);
    }

    // ========== JOGADORES ==========

    public void cadastrarJogador(Jogador jogador)
            throws ElencoCheioException, IllegalArgumentException, SelecaoNaoEncontradaException, PersistenciaException {
        validarCamposJogador(jogador);
        buscarSelecaoOuLancarExcecao(jogador.getPaisSelecao());
        List<Jogador> elenco = buscarJogadoresPorSelecaoPais(jogador.getPaisSelecao());
        if (elenco.size() >= MAX_JOGADORES)
            throw new ElencoCheioException(
                    "O elenco da seleção " + jogador.getPaisSelecao() + " já está cheio (" + MAX_JOGADORES + " jogadores).");
        if (elenco.stream().anyMatch(j -> j.getNumero() == jogador.getNumero()))
            throw new IllegalArgumentException("O número " + jogador.getNumero() + " já está em uso nesta seleção.");
        persistencia.salvarJogador(jogador);
        invalidarCache();
    }

    public void editarJogador(String idOriginal, Jogador novosDados)
            throws JogadorNaoEncontradoException, IllegalArgumentException, PersistenciaException {
        Jogador existente = buscarJogadorPorIdOuLancarExcecao(idOriginal);

        // Melhoria 3: revalida número se mudou
        int novoNumero = novosDados.getNumero() > 0 ? novosDados.getNumero() : existente.getNumero();
        String novoPais = (novosDados.getPaisSelecao() != null && !novosDados.getPaisSelecao().isBlank())
                ? novosDados.getPaisSelecao() : existente.getPaisSelecao();

        if (novoNumero != existente.getNumero() || !novoPais.equals(existente.getPaisSelecao())) {
            boolean numeroDuplicado = getJogadores().stream()
                    .filter(j -> j.getPaisSelecao().equalsIgnoreCase(novoPais))
                    .filter(j -> !j.getId().equals(idOriginal))
                    .anyMatch(j -> j.getNumero() == novoNumero);
            if (numeroDuplicado)
                throw new IllegalArgumentException("O número " + novoNumero + " já está em uso nesta seleção.");
        }

        if (novosDados.getNome() != null && !novosDados.getNome().isBlank())
            existente.setNome(novosDados.getNome());
        if (novosDados.getPosicao() != null && !novosDados.getPosicao().isBlank())
            existente.setPosicao(novosDados.getPosicao());
        if (novosDados.getNumero() > 0)
            existente.setNumero(novosDados.getNumero());
        if (novosDados.getIdade() > 0)
            existente.setIdade(novosDados.getIdade());
        if (novosDados.getStatus() != null)
            existente.setStatus(novosDados.getStatus());
        if (novosDados.getPaisSelecao() != null && !novosDados.getPaisSelecao().isBlank())
            existente.setPaisSelecao(novosDados.getPaisSelecao());

        if (!idOriginal.equals(existente.getId()))
            persistencia.excluirJogador(idOriginal);
        persistencia.salvarJogador(existente);
        invalidarCache();
    }

    public void excluirJogador(String idJogador)
            throws JogadorNaoEncontradoException, IllegalStateException, PersistenciaException {
        Jogador jogador = buscarJogadorPorIdOuLancarExcecao(idJogador);

        // Melhoria 4: bloqueia exclusão se elenco ficaria abaixo do mínimo
        long totalElenco = getJogadores().stream()
                .filter(j -> j.getPaisSelecao().equalsIgnoreCase(jogador.getPaisSelecao())).count();
        if (totalElenco <= MIN_JOGADORES)
            throw new IllegalStateException(
                    "Não é possível excluir: o elenco já está no mínimo de " + MIN_JOGADORES + " jogadores.");

        persistencia.excluirJogador(idJogador);
        invalidarCache();
    }

    public List<Jogador> listarTodosJogadores() throws PersistenciaException {
        return new ArrayList<>(getJogadores());
    }

    public List<Jogador> buscarJogadoresPorSelecaoPais(String pais) throws PersistenciaException {
        return getJogadores().stream()
                .filter(j -> j.getPaisSelecao().equalsIgnoreCase(pais))
                .collect(Collectors.toList());
    }

    public List<Jogador> listarJogadoresPorSelecao(Selecao selecao) throws PersistenciaException {
        return buscarJogadoresPorSelecaoPais(selecao.getPais());
    }

    /**
     * Busca jogadores combinando todos os critérios preenchidos (melhoria 7).
     */
    public List<Jogador> buscarJogadores(String nome, String posicao, StatusJogador status, String paisSelecao)
            throws PersistenciaException {
        return getJogadores().stream()
                .filter(j -> nome == null || nome.isBlank()
                        || j.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(j -> posicao == null || posicao.isBlank() || posicao.equalsIgnoreCase("Todas")
                        || j.getPosicao().equalsIgnoreCase(posicao))
                .filter(j -> status == null || j.getStatus() == status)
                .filter(j -> paisSelecao == null || paisSelecao.isBlank()
                        || j.getPaisSelecao().equalsIgnoreCase(paisSelecao))
                .collect(Collectors.toList());
    }

    // Mantidos para compatibilidade
    public List<Jogador> buscarJogadoresPorNome(String texto) throws PersistenciaException {
        return buscarJogadores(texto, null, null, null);
    }
    public List<Jogador> buscarJogadoresPorPosicao(String posicao) throws PersistenciaException {
        return buscarJogadores(null, posicao, null, null);
    }
    public List<Jogador> buscarJogadoresPorStatus(StatusJogador status) throws PersistenciaException {
        return buscarJogadores(null, null, status, null);
    }

    /** Conta jogadores de uma seleção — útil para o contador na UI. */
    public int contarJogadoresDaSelecao(String pais) throws PersistenciaException {
        return (int) getJogadores().stream()
                .filter(j -> j.getPaisSelecao().equalsIgnoreCase(pais)).count();
    }

    // ========== CACHE ==========

    private List<Selecao> getSelecoes() throws PersistenciaException {
        if (cacheSelecoes == null) cacheSelecoes = persistencia.carregarSelecoes();
        return cacheSelecoes;
    }

    private List<Jogador> getJogadores() throws PersistenciaException {
        if (cacheJogadores == null) cacheJogadores = persistencia.carregarJogadores();
        return cacheJogadores;
    }

    private void invalidarCache() {
        cacheSelecoes = null;
        cacheJogadores = null;
    }

    // ========== AUXILIARES PRIVADOS ==========

    private Selecao buscarSelecaoOuLancarExcecao(String pais)
            throws SelecaoNaoEncontradaException, PersistenciaException {
        return getSelecoes().stream()
                .filter(s -> s.getPais().equalsIgnoreCase(pais))
                .findFirst()
                .orElseThrow(() -> new SelecaoNaoEncontradaException(
                        "Seleção não encontrada para o país: " + pais));
    }

    private Jogador buscarJogadorPorIdOuLancarExcecao(String id)
            throws JogadorNaoEncontradoException, PersistenciaException {
        return getJogadores().stream()
                .filter(j -> j.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new JogadorNaoEncontradoException(
                        "Jogador não encontrado: " + id));
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
