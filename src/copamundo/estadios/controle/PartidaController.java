package copamundo.estadios.controle;

import copamundo.comum.Partida;
import copamundo.comum.Selecao;
import copamundo.estadios.excecoes.PersistenciaException;
import copamundo.estadios.excecoes.RegraNegocioException;
import copamundo.estadios.modelo.Arbitro;
import copamundo.estadios.modelo.Estadio;
import copamundo.estadios.persistencia.RepositorioArquivo;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        partida.setArbitroPrincipal(arbitro);
        repositorioPartidas.salvar(partida);
    }

    public Partida criarPartidaParaDesignacao(Estadio estadio, LocalDate data, Arbitro arbitro)
            throws PersistenciaException, RegraNegocioException {
        Selecao mandante = new Selecao("Selecao A", "Pais A");
        Selecao visitante = new Selecao("Selecao B", "Pais B");
        LocalDateTime dataHora = LocalDateTime.of(data, LocalTime.NOON);
        Partida partida = cadastrarPartida(mandante, visitante, estadio, dataHora);
        designarArbitroPrincipal(partida, arbitro);
        return partida;
    }

    public List<Partida> listarPartidas() throws PersistenciaException {
        return repositorioPartidas.listar();
    }
}
