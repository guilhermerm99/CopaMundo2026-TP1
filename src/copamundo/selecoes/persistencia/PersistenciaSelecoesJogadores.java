package copamundo.selecoes.persistencia;

import copamundo.comum.Jogador;
import copamundo.comum.Selecao;
import copamundo.selecoes.excecoes.PersistenciaException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class PersistenciaSelecoesJogadores {

    private static final Path ARQUIVO_SELECOES  = Paths.get("dados", "selecoes.dat");
    private static final Path ARQUIVO_JOGADORES = Paths.get("dados", "jogadores.dat");

    private final RepositorioArquivo<Selecao> repositorioSelecoes;
    private final RepositorioArquivo<Jogador> repositorioJogadores;

    public PersistenciaSelecoesJogadores() {
        this.repositorioSelecoes  = new RepositorioArquivo<>(ARQUIVO_SELECOES);
        this.repositorioJogadores = new RepositorioArquivo<>(ARQUIVO_JOGADORES);
    }

    public void salvarSelecao(Selecao selecao) throws PersistenciaException {
        repositorioSelecoes.salvar(selecao);
    }

    public void excluirSelecao(String pais) throws PersistenciaException {
        repositorioSelecoes.excluir(pais);
    }

    public List<Selecao> carregarSelecoes() throws PersistenciaException {
        return repositorioSelecoes.listar();
    }

    public Optional<Selecao> buscarSelecaoPorPais(String pais) throws PersistenciaException {
        return repositorioSelecoes.buscarPorId(pais);
    }

    public void salvarJogador(Jogador jogador) throws PersistenciaException {
        repositorioJogadores.salvar(jogador);
    }

    public void excluirJogador(String id) throws PersistenciaException {
        repositorioJogadores.excluir(id);
    }

    public List<Jogador> carregarJogadores() throws PersistenciaException {
        return repositorioJogadores.listar();
    }

    public Optional<Jogador> buscarJogadorPorNome(String id) throws PersistenciaException {
        return repositorioJogadores.buscarPorId(id);
    }
}
