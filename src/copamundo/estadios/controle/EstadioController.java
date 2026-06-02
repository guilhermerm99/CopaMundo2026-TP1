package copamundo.estadios.controle;

import copamundo.comum.Partida;
import copamundo.estadios.excecoes.PersistenciaException;
import copamundo.estadios.excecoes.RegraNegocioException;
import copamundo.estadios.modelo.Estadio;
import copamundo.estadios.persistencia.RepositorioArquivo;

import java.nio.file.Path;
import java.util.List;

public class EstadioController {
    private final RepositorioArquivo<Estadio> repositorioEstadios;
    private final RepositorioArquivo<Partida> repositorioPartidas;

    public EstadioController() {
        Path pastaDados = Path.of("dados");
        repositorioEstadios = new RepositorioArquivo<>(pastaDados.resolve("estadios.dat"));
        repositorioPartidas = new RepositorioArquivo<>(pastaDados.resolve("partidas.dat"));
    }

    public Estadio cadastrarEstadio(String nome, String cidade, int capacidade) throws PersistenciaException {
        Estadio estadio = new Estadio(proximoId(), nome, cidade, capacidade);
        repositorioEstadios.salvar(estadio);
        return estadio;
    }

    public List<Estadio> listarEstadios() throws PersistenciaException {
        return repositorioEstadios.listar();
    }

    public void validarEstadioDisponivel(Partida novaPartida) throws PersistenciaException, RegraNegocioException {
        for (Partida partidaExistente : repositorioPartidas.listar()) {
            if (partidaExistente.aconteceNoMesmoEstadioEHorario(novaPartida)) {
                throw new RegraNegocioException("Um estadio nao pode sediar duas partidas no mesmo horario.");
            }
        }
    }

    private int proximoId() throws PersistenciaException {
        int maiorId = 0;
        for (Estadio estadio : repositorioEstadios.listar()) {
            maiorId = Math.max(maiorId, estadio.getId());
        }
        return maiorId + 1;
    }
}
