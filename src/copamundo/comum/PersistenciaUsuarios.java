package copamundo.comum;




import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersistenciaUsuarios {

    private static final Path PASTA_DADOS =
            Paths.get(System.getProperty("user.dir"), "dados");

    private static final Path ARQUIVO_USUARIOS =
            PASTA_DADOS.resolve("usuarios.dat");

    public void salvarUsuario(Usuario usuario) throws PersistenciaException {
        List<Usuario> usuarios = carregarUsuarios();

        boolean encontrou = false;

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmailUsuario().equalsIgnoreCase(usuario.getEmailUsuario())) {
                usuarios.set(i, usuario);
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            usuarios.add(usuario);
        }

        salvarListaUsuarios(usuarios);
    }

    public List<Usuario> carregarUsuarios() throws PersistenciaException {
        criarPastaDados();

        if (!Files.exists(ARQUIVO_USUARIOS)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(ARQUIVO_USUARIOS))) {
            return (List<Usuario>) entrada.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("erro ao carregar usuários do arquivo.", e);
        }
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email) throws PersistenciaException {
        List<Usuario> usuarios = carregarUsuarios();

        for (Usuario usuario : usuarios) {
            if (usuario.getEmailUsuario() != null &&
                    usuario.getEmailUsuario().equalsIgnoreCase(email)) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }

    public void excluirUsuario(String email) throws PersistenciaException {
        List<Usuario> usuarios = carregarUsuarios();

        usuarios.removeIf(usuario ->
                usuario.getEmailUsuario() != null &&
                        usuario.getEmailUsuario().equalsIgnoreCase(email)
        );

        salvarListaUsuarios(usuarios);
    }

    private void salvarListaUsuarios(List<Usuario> usuarios) throws PersistenciaException {
        criarPastaDados();

        try (ObjectOutputStream saida = new ObjectOutputStream(Files.newOutputStream(ARQUIVO_USUARIOS))) {
            saida.writeObject(usuarios);
        } catch (IOException e) {
            throw new PersistenciaException("erro ao salvar usuários no arquivo.", e);
        }
    }

    private void criarPastaDados() throws PersistenciaException {
        try {
            Files.createDirectories(PASTA_DADOS);
        } catch (IOException e) {
            throw new PersistenciaException("erro ao criar pasta de dados.", e);
        }
    }
}