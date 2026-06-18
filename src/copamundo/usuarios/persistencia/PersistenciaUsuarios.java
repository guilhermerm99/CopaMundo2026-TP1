package copamundo.usuarios.persistencia;

import copamundo.usuarios.controle.Usuario;
import copamundo.usuarios.excecoes.PersistenciaException;

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

    public void atualizarUsuario(String emailOriginal, Usuario usuarioAtualizado) throws PersistenciaException {
        List<Usuario> usuarios = carregarUsuarios();

        boolean encontrou = false;

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmailUsuario() != null &&
                    usuarios.get(i).getEmailUsuario().equalsIgnoreCase(emailOriginal)) {
                usuarios.set(i, usuarioAtualizado);
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            throw new PersistenciaException("usuário não encontrado para atualização.", null);
        }

        salvarListaUsuarios(usuarios);
    }

    public List<Usuario> carregarUsuarios() throws PersistenciaException {
        criarPastaDados();

        if (!Files.exists(ARQUIVO_USUARIOS)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(ARQUIVO_USUARIOS))) {
            return converterListaUsuarios(entrada.readObject());
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

    private List<Usuario> converterListaUsuarios(Object objeto) throws PersistenciaException {
        if (!(objeto instanceof List<?> itens)) {
            throw new PersistenciaException("arquivo de usuarios possui formato invalido.", null);
        }

        List<Usuario> usuarios = new ArrayList<>();
        for (Object item : itens) {
            if (!(item instanceof Usuario usuario)) {
                throw new PersistenciaException("arquivo de usuarios contem item invalido.", null);
            }
            usuarios.add(usuario);
        }
        return usuarios;
    }

    public boolean existeCpfOuEmailEmOutroUsuario(String emailOriginal, String cpfNovo, String emailNovo)
            throws PersistenciaException {

        List<Usuario> usuarios = carregarUsuarios();

        String emailOriginalNormalizado = normalizarEmail(emailOriginal);
        String emailNovoNormalizado = normalizarEmail(emailNovo);
        String cpfNovoNormalizado = normalizarCpf(cpfNovo);

        for (Usuario usuario : usuarios) {

            String emailUsuario = normalizarEmail(usuario.getEmailUsuario());
            String cpfUsuario = normalizarCpf(usuario.getCpf());

            boolean ehUsuarioSelecionado = emailUsuario.equals(emailOriginalNormalizado);

            if (ehUsuarioSelecionado) {
                continue;
            }

            if (!emailNovoNormalizado.isBlank() && emailUsuario.equals(emailNovoNormalizado)) {
                return true;
            }

            if (!cpfNovoNormalizado.isBlank() && cpfUsuario.equals(cpfNovoNormalizado)) {
                return true;
            }
        }

        return false;
    }

    private String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private String normalizarCpf(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }
}