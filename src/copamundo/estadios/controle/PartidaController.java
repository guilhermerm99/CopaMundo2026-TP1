package copamundo.estadios.controle;

import copamundo.comum.Partida;
import copamundo.comum.Selecao;
import copamundo.estadios.excecoes.PersistenciaException;
import copamundo.estadios.excecoes.RegraNegocioException;
import copamundo.estadios.modelo.Arbitro;
import copamundo.estadios.modelo.Estadio;
import copamundo.estadios.persistencia.RepositorioArquivo;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PartidaController {
    private final RepositorioArquivo<Partida> repositorioPartidas;
    private final EstadioController estadioController;
    private final ArbitroController arbitroController;

    public PartidaController() {
        repositorioPartidas = new RepositorioArquivo<>(Path.of("dados", "partidas.dat"));
        estadioController = new EstadioController();
        arbitroController = new ArbitroController();
    }

    public Partida cadastrarPartida(Selecao mandante, Selecao visitante, Estadio estadio, LocalDateTime dataHora)
            throws PersistenciaException, RegraNegocioException {
        Partida partida = new Partida(mandante, visitante, estadio, dataHora);
        estadioController.validarEstadioDisponivel(partida);
        repositorioPartidas.salvar(partida);
        return partida;
    }

    public void designarArbitroPrincipal(Partida partida, Arbitro arbitro)
            throws PersistenciaException, RegraNegocioException {
        if (partida == null) {
            throw new RegraNegocioException("Selecione uma partida.");
        }

        arbitroController.validarNacionalidadeParaPartida(arbitro, partida);
        validarArbitroLivre(partida, arbitro);
        partida.setArbitroPrincipal(arbitro);
        repositorioPartidas.salvar(partida);
    }

    public void removerDesignacao(Partida partida) throws PersistenciaException, RegraNegocioException {
        if (partida == null) {
            throw new RegraNegocioException("Selecione uma partida.");
        }
        throw new RegraNegocioException("Cada partida deve ter pelo menos um arbitro principal. Substitua o arbitro em vez de remover.");
    }

    public void validarPartidaComArbitroPrincipal(Partida partida) throws RegraNegocioException {
        if (partida == null) {
            throw new RegraNegocioException("Selecione uma partida.");
        }
        arbitroController.validarArbitroPrincipal(partida.getArbitroPrincipal());
    }

    public List<Arbitro> listarArbitrosAptosParaPartida(Partida partida) throws PersistenciaException {
        return arbitroController.listarArbitrosAptosParaPartida(partida);
    }

    public List<Partida> listarPartidas() throws PersistenciaException {
        return repositorioPartidas.listar();
    }

    public List<Partida> listarDesignacoes() throws PersistenciaException {
        return repositorioPartidas.listar().stream()
                .filter(partida -> partida.getArbitroPrincipal() != null)
                .collect(Collectors.toList());
    }

    public List<Partida> buscarPartidas(String selecao, Estadio estadio, Arbitro arbitro, Boolean apenasDesignadas)
            throws PersistenciaException {
        String selecaoFiltro = normalizar(selecao);
        return repositorioPartidas.listar().stream()
                .filter(partida -> selecaoFiltro.isBlank()
                        || normalizar(partida.nomeSelecoes()).contains(selecaoFiltro))
                .filter(partida -> estadio == null || estadio.equals(partida.getEstadioPartida()))
                .filter(partida -> arbitro == null || arbitro.equals(partida.getArbitroPrincipal()))
                .filter(partida -> apenasDesignadas == null
                        || !apenasDesignadas
                        || partida.getArbitroPrincipal() != null)
                .collect(Collectors.toList());
    }

    private void validarArbitroLivre(Partida partidaBase, Arbitro arbitro) throws PersistenciaException, RegraNegocioException {
        for (Partida partida : repositorioPartidas.listar()) {
            if (partida.getId().equals(partidaBase.getId())) {
                continue;
            }
            if (arbitro.equals(partida.getArbitroPrincipal())
                    && partida.getDataPartida().equals(partidaBase.getDataPartida())
                    && partida.getHorarioPartida().equals(partidaBase.getHorarioPartida())) {
                throw new RegraNegocioException("Esse arbitro ja esta designado para outra partida no mesmo horario.");
            }
        }
    }

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
    }
}
