package copamundo.estadios.controle;

import copamundo.comum.Partida;
import copamundo.estadios.excecoes.PersistenciaException;
import copamundo.estadios.excecoes.RegraNegocioException;
import copamundo.estadios.modelo.Arbitro;
import copamundo.estadios.persistencia.RepositorioArquivo;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArbitroController {
    private final RepositorioArquivo<Arbitro> repositorioArbitros;
    private final RepositorioArquivo<Partida> repositorioPartidas;

    public ArbitroController() {
        repositorioArbitros = new RepositorioArquivo<>(Path.of("dados", "arbitros.dat"));
        repositorioPartidas = new RepositorioArquivo<>(Path.of("dados", "partidas.dat"));
    }

    public Arbitro cadastrarArbitro(String nome, String federacao, String categoria) throws PersistenciaException {
        return cadastrarArbitro(nome, federacao, categoria, "1 ano");
    }

    public Arbitro cadastrarArbitro(String nome, String federacao, String categoria, String experiencia) throws PersistenciaException {
        validarArbitro(nome, federacao, categoria, experiencia);
        validarDuplicidade(nome, federacao, 0);
        Arbitro arbitro = new Arbitro(proximoId(), nome, categoria, federacao, experiencia);
        repositorioArbitros.salvar(arbitro);
        return arbitro;
    }

    public Arbitro editarArbitro(int id, String nome, String federacao, String categoria)
            throws PersistenciaException, RegraNegocioException {
        return editarArbitro(id, nome, federacao, categoria, "1 ano");
    }

    public Arbitro editarArbitro(int id, String nome, String federacao, String categoria, String experiencia)
            throws PersistenciaException, RegraNegocioException {
        buscarArbitroPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Arbitro nao encontrado."));
        validarArbitro(nome, federacao, categoria, experiencia);
        validarDuplicidade(nome, federacao, id);

        Arbitro arbitro = new Arbitro(id, nome, categoria, federacao, experiencia);
        repositorioArbitros.salvar(arbitro);
        atualizarArbitroNasPartidas(arbitro);
        return arbitro;
    }

    public void excluirArbitro(int id) throws PersistenciaException, RegraNegocioException {
        Arbitro arbitro = buscarArbitroPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Arbitro nao encontrado."));

        boolean designado = repositorioPartidas.listar().stream()
                .anyMatch(partida -> arbitro.equals(partida.getArbitroPrincipal()));
        if (designado) {
            throw new RegraNegocioException("Nao e possivel excluir um arbitro ja designado.");
        }

        repositorioArbitros.excluir(String.valueOf(id));
    }

    public List<Arbitro> listarArbitros() throws PersistenciaException {
        return repositorioArbitros.listar();
    }

    public Optional<Arbitro> buscarArbitroPorId(int id) throws PersistenciaException {
        return repositorioArbitros.buscarPorId(String.valueOf(id));
    }

    public List<Arbitro> buscarArbitros(String nome, String federacao, String categoria) throws PersistenciaException {
        String nomeFiltro = normalizar(nome);
        String federacaoFiltro = normalizar(federacao);
        String categoriaFiltro = normalizar(categoria);

        return repositorioArbitros.listar().stream()
                .filter(arbitro -> nomeFiltro.isBlank() || normalizar(arbitro.getNome()).contains(nomeFiltro))
                .filter(arbitro -> federacaoFiltro.isBlank() || normalizar(arbitro.getFederacao()).contains(federacaoFiltro))
                .filter(arbitro -> categoriaFiltro.isBlank() || categoriaFiltro.equals("todos")
                        || normalizar(arbitro.getCategoria()).equals(categoriaFiltro))
                .collect(Collectors.toList());
    }

    public void validarArbitroPrincipal(Arbitro arbitro) throws RegraNegocioException {
        if (arbitro == null) {
            throw new RegraNegocioException("Cada partida deve ter pelo menos um arbitro principal.");
        }
    }

    public List<Arbitro> listarArbitrosAptosParaPartida(Partida partida) throws PersistenciaException {
        return repositorioArbitros.listar().stream()
                .filter(arbitro -> arbitroPodeAtuar(arbitro, partida))
                .collect(Collectors.toList());
    }

    public boolean arbitroPodeAtuar(Arbitro arbitro, Partida partida) {
        if (arbitro == null || partida == null || partida.getMandante() == null || partida.getVisitante() == null) {
            return false;
        }

        return !arbitro.possuiNacionalidade(partida.getMandante().getNacionalidade())
                && !arbitro.possuiNacionalidade(partida.getVisitante().getNacionalidade());
    }

    public void validarNacionalidadeParaPartida(Arbitro arbitro, Partida partida) throws RegraNegocioException {
        validarArbitroPrincipal(arbitro);

        if (!arbitroPodeAtuar(arbitro, partida)) {
            throw new RegraNegocioException("Arbitros nao podem atuar em partidas de selecoes de sua nacionalidade.");
        }
    }

    private void validarArbitro(String nome, String federacao, String categoria, String experiencia) {
        new Arbitro(0, nome, categoria, federacao, experiencia);
    }

    private void validarDuplicidade(String nome, String federacao, int idIgnorado) throws PersistenciaException {
        String nomeFiltro = normalizar(nome);
        String federacaoFiltro = normalizar(federacao);
        boolean duplicado = repositorioArbitros.listar().stream()
                .filter(arbitro -> arbitro.getId() != idIgnorado)
                .anyMatch(arbitro -> normalizar(arbitro.getNome()).equals(nomeFiltro)
                        && normalizar(arbitro.getFederacao()).equals(federacaoFiltro));
        if (duplicado) {
            throw new IllegalArgumentException("Ja existe um arbitro com esse nome e federacao.");
        }
    }

    private void atualizarArbitroNasPartidas(Arbitro arbitroAtualizado) throws PersistenciaException {
        for (Partida partida : repositorioPartidas.listar()) {
            if (arbitroAtualizado.equals(partida.getArbitroPrincipal())) {
                partida.setArbitroPrincipal(arbitroAtualizado);
                repositorioPartidas.salvar(partida);
            }
        }
    }

    private int proximoId() throws PersistenciaException {
        int maiorId = 0;
        for (Arbitro arbitro : repositorioArbitros.listar()) {
            maiorId = Math.max(maiorId, arbitro.getId());
        }
        return maiorId + 1;
    }

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
    }
}
