package copamundo.estadios.controle;

import copamundo.comum.Partida;
import copamundo.estadios.excecoes.PersistenciaException;
import copamundo.estadios.excecoes.RegraNegocioException;
import copamundo.estadios.modelo.Arbitro;
import copamundo.estadios.persistencia.RepositorioArquivo;

import java.nio.file.Path;
import java.util.List;

public class ArbitroController {
    private final RepositorioArquivo<Arbitro> repositorioArbitros;

    public ArbitroController() {
        repositorioArbitros = new RepositorioArquivo<>(Path.of("dados", "arbitros.dat"));
    }

    public Arbitro cadastrarArbitro(String nome, String federacao, String categoria) throws PersistenciaException {
        Arbitro arbitro = new Arbitro(proximoId(), nome, categoria, federacao);
        repositorioArbitros.salvar(arbitro);
        return arbitro;
    }

    public List<Arbitro> listarArbitros() throws PersistenciaException {
        return repositorioArbitros.listar();
    }

    public void validarArbitroPrincipal(Arbitro arbitro) throws RegraNegocioException {
        if (arbitro == null) {
            throw new RegraNegocioException("Cada partida deve ter pelo menos um arbitro principal.");
        }
    }

    public void validarNacionalidadeParaPartida(Arbitro arbitro, Partida partida) throws RegraNegocioException {
        validarArbitroPrincipal(arbitro);

        if (arbitro.possuiNacionalidade(partida.getMandante().getNacionalidade())
                || arbitro.possuiNacionalidade(partida.getVisitante().getNacionalidade())) {
            throw new RegraNegocioException("Arbitros nao podem atuar em partidas de selecoes de sua nacionalidade.");
        }
    }

    private int proximoId() throws PersistenciaException {
        int maiorId = 0;
        for (Arbitro arbitro : repositorioArbitros.listar()) {
            maiorId = Math.max(maiorId, arbitro.getId());
        }
        return maiorId + 1;
    }
}
