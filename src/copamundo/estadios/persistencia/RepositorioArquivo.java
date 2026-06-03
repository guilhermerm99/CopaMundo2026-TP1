package copamundo.estadios.persistencia;

import copamundo.estadios.excecoes.PersistenciaException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioArquivo<T> {
    private final Path arquivo;

    public RepositorioArquivo(Path arquivo) {
        this.arquivo = arquivo;
    }

    public void salvar(T entidade) throws PersistenciaException {
        List<T> entidades = listar();
        String id = obterId(entidade);
        entidades.removeIf(item -> obterId(item).equals(id));
        entidades.add(entidade);
        gravarTodos(entidades);
    }

    public void excluir(String id) throws PersistenciaException {
        List<T> entidades = listar();
        entidades.removeIf(item -> obterId(item).equals(id));
        gravarTodos(entidades);
    }

    public List<T> listar() throws PersistenciaException {
        if (!Files.exists(arquivo)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(arquivo))) {
            Object objeto = entrada.readObject();
            return new ArrayList<>(converterLista(objeto));
        } catch (IOException | ClassNotFoundException erro) {
            throw new PersistenciaException("Nao foi possivel carregar o arquivo " + arquivo, erro);
        }
    }

    public Optional<T> buscarPorId(String id) throws PersistenciaException {
        return listar().stream()
                .filter(item -> obterId(item).equals(id))
                .findFirst();
    }

    private void gravarTodos(List<T> entidades) throws PersistenciaException {
        try {
            Path pasta = arquivo.getParent();
            if (pasta != null) {
                Files.createDirectories(pasta);
            }

            try (ObjectOutputStream saida = new ObjectOutputStream(Files.newOutputStream(arquivo))) {
                saida.writeObject(entidades);
            }
        } catch (IOException erro) {
            throw new PersistenciaException("Nao foi possivel gravar o arquivo " + arquivo, erro);
        }
    }

    private String obterId(T entidade) {
        try {
            Method metodo = entidade.getClass().getMethod("getId");
            return String.valueOf(metodo.invoke(entidade));
        } catch (ReflectiveOperationException erro) {
            throw new IllegalArgumentException("A entidade deve possuir um metodo getId().", erro);
        }
    }

    @SuppressWarnings("unchecked")
    private List<T> converterLista(Object objeto) {
        return (List<T>) objeto;
    }
}
