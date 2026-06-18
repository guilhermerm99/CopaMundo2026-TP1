package copamundo.selecoes.persistencia;

import copamundo.selecoes.excecoes.PersistenciaException;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class RepositorioArquivo<T extends Serializable & Identificavel> {

    private final Path arquivo;

    public RepositorioArquivo(Path arquivo) {
        this.arquivo = arquivo;
    }

    public void salvar(T entidade) throws PersistenciaException {
        List<T> lista = listar();
        String id = entidade.getId();
        lista.removeIf(item -> item.getId().equals(id));
        lista.add(entidade);
        gravarTodos(lista);
    }

    public void excluir(String id) throws PersistenciaException {
        List<T> lista = listar();
        lista.removeIf(item -> item.getId().equals(id));
        gravarTodos(lista);
    }

    @SuppressWarnings("unchecked")
    public List<T> listar() throws PersistenciaException {
        if (!Files.exists(arquivo)) return new ArrayList<>();
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(arquivo))) {
            return new ArrayList<>((List<T>) entrada.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao ler arquivo: " + arquivo, e);
        }
    }

    public Optional<T> buscarPorId(String id) throws PersistenciaException {
        return listar().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    private void gravarTodos(List<T> lista) throws PersistenciaException {
        try {
            Path pasta = arquivo.getParent();
            if (pasta != null) Files.createDirectories(pasta);
            try (ObjectOutputStream saida = new ObjectOutputStream(Files.newOutputStream(arquivo))) {
                saida.writeObject(lista);
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao gravar arquivo: " + arquivo, e);
        }
    }
}
