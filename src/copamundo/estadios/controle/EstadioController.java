package copamundo.estadios.controle;

import copamundo.comum.Partida;
import copamundo.estadios.excecoes.PersistenciaException;
import copamundo.estadios.excecoes.RegraNegocioException;
import copamundo.estadios.modelo.Estadio;
import copamundo.estadios.persistencia.RepositorioArquivo;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class EstadioController {
    private final RepositorioArquivo<Estadio> repositorioEstadios;
    private final RepositorioArquivo<Partida> repositorioPartidas;

    public EstadioController() {
        Path pastaDados = Path.of("dados");
        repositorioEstadios = new RepositorioArquivo<>(pastaDados.resolve("estadios.dat"));
        repositorioPartidas = new RepositorioArquivo<>(pastaDados.resolve("partidas.dat"));
    }

    public Estadio cadastrarEstadio(String nome, String cidade, int capacidade) throws PersistenciaException {
        validarEstadio(nome, cidade, capacidade);
        validarDuplicidade(nome, cidade, 0);
        Estadio estadio = new Estadio(proximoId(), nome, cidade, capacidade);
        repositorioEstadios.salvar(estadio);
        return estadio;
    }

    public Estadio editarEstadio(int id, String nome, String cidade, int capacidade)
            throws PersistenciaException, RegraNegocioException {
        buscarEstadioPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Estadio nao encontrado."));
        validarEstadio(nome, cidade, capacidade);
        validarDuplicidade(nome, cidade, id);

        Estadio estadio = new Estadio(id, nome, cidade, capacidade);
        repositorioEstadios.salvar(estadio);
        atualizarEstadioNasPartidas(estadio);
        return estadio;
    }

    public void excluirEstadio(int id) throws PersistenciaException, RegraNegocioException {
        Estadio estadio = buscarEstadioPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Estadio nao encontrado."));

        boolean possuiPartida = repositorioPartidas.listar().stream()
                .anyMatch(partida -> estadio.equals(partida.getEstadioPartida()));
        if (possuiPartida) {
            throw new RegraNegocioException("Nao e possivel excluir um estadio vinculado a partidas.");
        }

        repositorioEstadios.excluir(String.valueOf(id));
    }

    public List<Estadio> listarEstadios() throws PersistenciaException {
        return repositorioEstadios.listar();
    }

    public Optional<Estadio> buscarEstadioPorId(int id) throws PersistenciaException {
        return repositorioEstadios.buscarPorId(String.valueOf(id));
    }

    public List<Estadio> buscarEstadios(String nome, String cidade) throws PersistenciaException {
        String nomeFiltro = normalizar(nome);
        String cidadeFiltro = normalizar(cidade);

        return repositorioEstadios.listar().stream()
                .filter(estadio -> nomeFiltro.isBlank() || normalizar(estadio.getNome()).contains(nomeFiltro))
                .filter(estadio -> cidadeFiltro.isBlank() || normalizar(estadio.getCidade()).contains(cidadeFiltro))
                .collect(Collectors.toList());
    }

    public void validarEstadioDisponivel(Partida novaPartida) throws PersistenciaException, RegraNegocioException {
        for (Partida partidaExistente : repositorioPartidas.listar()) {
            if (partidaExistente.getId().equals(novaPartida.getId())) {
                continue;
            }
            if (partidaExistente.aconteceNoMesmoEstadioEHorario(novaPartida)) {
                throw new RegraNegocioException("Um estadio nao pode sediar duas partidas no mesmo horario.");
            }
        }
    }

    private void validarEstadio(String nome, String cidade, int capacidade) {
        new Estadio(0, nome, cidade, capacidade);
    }

    private void validarDuplicidade(String nome, String cidade, int idIgnorado) throws PersistenciaException {
        String nomeFiltro = normalizar(nome);
        String cidadeFiltro = normalizar(cidade);
        boolean duplicado = repositorioEstadios.listar().stream()
                .filter(estadio -> estadio.getId() != idIgnorado)
                .anyMatch(estadio -> normalizar(estadio.getNome()).equals(nomeFiltro)
                        && normalizar(estadio.getCidade()).equals(cidadeFiltro));
        if (duplicado) {
            throw new IllegalArgumentException("Já existe um estádio com esse nome nessa localização.");
        }
    }

    private void atualizarEstadioNasPartidas(Estadio estadioAtualizado) throws PersistenciaException {
        for (Partida partida : repositorioPartidas.listar()) {
            if (estadioAtualizado.equals(partida.getEstadioPartida())) {
                partida.setEstadioPartida(estadioAtualizado);
                repositorioPartidas.salvar(partida);
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

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
    }
}
