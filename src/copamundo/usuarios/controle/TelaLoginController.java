package copamundo.usuarios.controle;

import copamundo.usuarios.excecoes.PersistenciaException;
import copamundo.usuarios.persistencia.PersistenciaUsuarios;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TelaLoginController {
    @FXML
    private TextField campoEmail;
    @FXML
    private PasswordField campoSenha;
    private String email;
    private String senha;
    @FXML
    private void entrar() {
        this.email = campoEmail.getText();
        this.senha = campoSenha.getText();

        autenticarUsuario(this.email, this.senha);

    }

    @FXML
    private void telaCadastro(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/copamundo/usuarios/visao/TelaCadastro.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void telaUsuarios(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/copamundo/usuarios/visao/TelaUsuarios.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    private static Usuario usuarioLogado;
    private Date ultimoLogin;

    private int limiteTentativas = 3;
    private Map<String, Integer> tentativasLogin = new HashMap<>();

    // construtor vazio
    public TelaLoginController() {}

    // construtor 2
    /*public TelaLoginController(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }*/

    // setters
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    // getters
    public String getEmail() {
        return email;
    }
    public String getSenha() {
        return senha;
    }
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    public Date getUltimoLogin() {
        return ultimoLogin;
    }

    // métodos
    public Usuario fazerLogin(String email, String senha) {
        try {
            PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
            Optional<Usuario> usuarioEncontrado = persistencia.buscarUsuarioPorEmail(email);

            if (usuarioEncontrado.isPresent() && usuarioEncontrado.get().validarSenha(senha)) {
                return usuarioEncontrado.get();
            }
        } catch (PersistenciaException e) {
            System.out.println("erro ao fazer login: " + e.getMessage());
        }

        return null;
    }

    public boolean verificarAdministradorInicial() {
        for (Usuario usuario : Usuario.usuarios) {
            if (usuario.isAdministrador() && usuario.isAtivo()) {
                return true;
            }
        }

        return false;
    }

    public boolean cadastrado() {
        return !Usuario.usuarios.isEmpty();
    }

    public boolean cadastrado(String email) {
        for (Usuario usuario : Usuario.usuarios) {
            if (usuario.getEmailUsuario() != null &&
                    usuario.getEmailUsuario().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }

    public void abrirTelaLogin() {
        System.out.println("Tela de login aberta.");
    }

    public boolean autenticarUsuario() {
        return autenticarUsuario(this.email, this.senha);
    }

    public boolean autenticarUsuario(String email, String senha) {
        if (email == null || senha == null || email.trim().isEmpty() || senha.trim().isEmpty()) {
            System.out.println("Preencha e-mail e senha.");
            return false;
        }

        Usuario usuario = buscarUsuarioLogin(email);

        if (usuario == null) {
            System.out.println("E-mail inválido!");
            registrarTentativaLogin(email);
            return false;
        }

        if (!verificarStatusUsuario(usuario)) {
            System.out.println("Usuário inativo ou bloqueado.");
            return false;
        }

        if (!usuario.validarSenha(senha)) {
            System.out.println("Senha inválida!");
            registrarTentativaLogin(email);
            bloquearUsuarioPorTentativas(usuario, email);
            return false;
        }

        usuarioLogado = usuario;
        this.email = email;
        this.senha = senha;

        zerarTentativasLogin(email);
        registrarUltimoLogin();

        System.out.println("Login realizado com sucesso.");
        abrirTelaPrincipalPorPerfil(usuario);

        if (usuario.isTrocaSenhaObrigatoria()) {
            abrirTelaTrocaSenha();
        }

        return true;
    }

    public boolean validarCamposLogin(String email, String senha) {
        for (Usuario usuario : Usuario.usuarios) {
            if (usuario.getEmailUsuario() != null &&
                    usuario.getEmailUsuario().equalsIgnoreCase(email)) {
                if (usuario.validarSenha(senha)) {
                    System.out.println("Dados de login válidos");
                    return true;
                } else {
                    System.out.println("Senha inválida!");
                    return false;
                }
            }
        }

        System.out.println("E-mail inválido!");
        return false;
    }

    // usar de usuário
    public boolean verificarSenha(String senha) {
        if (usuarioLogado == null) {
            return false;
        }

        return usuarioLogado.validarSenha(senha);
    }

    public boolean verificarStatusUsuario() {
        if (usuarioLogado == null) {
            return false;
        }

        return verificarStatusUsuario(usuarioLogado);
    }

    public boolean verificarStatusUsuario(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        return usuario.isAtivo();
    }

    public Usuario.Funcao verificarPerfilUsuario() {
        if (usuarioLogado == null) {
            return null;
        }

        return usuarioLogado.getFuncao();
    }

    public void registrarTentativaLogin() {
        registrarTentativaLogin(this.email);
    }

    public void registrarTentativaLogin(String email) {
        if (email == null) {
            return;
        }

        email = email.trim().toLowerCase();

        int tentativas = 0;

        if (tentativasLogin.containsKey(email)) {
            tentativas = tentativasLogin.get(email);
        }

        tentativas++;
        tentativasLogin.put(email, tentativas);

        System.out.println("Tentativa de login registrada: " + tentativas);
    }

    public boolean bloquearUsuarioPorTentativas() {
        if (usuarioLogado == null) {
            return false;
        }

        return bloquearUsuarioPorTentativas(usuarioLogado, usuarioLogado.getEmailUsuario());
    }

    public boolean bloquearUsuarioPorTentativas(Usuario usuario, String email) {
        if (usuario == null || email == null) {
            return false;
        }

        email = email.trim().toLowerCase();

        int tentativas = 0;

        if (tentativasLogin.containsKey(email)) {
            tentativas = tentativasLogin.get(email);
        }

        if (tentativas >= limiteTentativas) {
            usuario.setStatus(Usuario.Status.INATIVO);
            System.out.println("Usuário bloqueado por excesso de tentativas.");
            return true;
        }

        return false;
    }

    public void zerarTentativasLogin() {
        zerarTentativasLogin(this.email);
    }

    public void zerarTentativasLogin(String email) {
        if (email == null) {
            return;
        }

        tentativasLogin.remove(email.trim().toLowerCase());
    }

    public void registrarUltimoLogin() {
        this.ultimoLogin = new Date();
    }

    public void abrirTelaPrincipalPorPerfil() {
        if (usuarioLogado != null) {
            abrirTelaPrincipalPorPerfil(usuarioLogado);
        }
    }

    public void abrirTelaPrincipalPorPerfil(Usuario usuario) {
        if (usuario == null) {
            System.out.println("Nenhum usuário logado.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/copamundo/usuarios/visao/TelaUsuarios.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) campoEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            System.out.println("Tela principal aberta para o perfil: " + usuario.getFuncao());

        } catch (IOException e) {
            System.out.println("Erro ao carregar a tela principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void abrirTelaTrocaSenha() {
        System.out.println("Abrindo tela de troca de senha.");
    }

    public boolean redefinirSenha(String senhaAtual, String novaSenha) {
        if (usuarioLogado == null) {
            return false;
        }

        if (!usuarioLogado.validarSenha(senhaAtual)) {
            System.out.println("Senha atual incorreta.");
            return false;
        }

        if (novaSenha == null || novaSenha.length() < 6 || novaSenha.length() > 20) {
            System.out.println("Nova senha inválida.");
            return false;
        }

        usuarioLogado.setSenhaUsuario(novaSenha);
        System.out.println("Senha redefinida com sucesso.");
        return true;
    }

    public void sairDoSistema() {
        usuarioLogado = null;
        email = null;
        senha = null;

        System.out.println("Usuário saiu do sistema.");
    }

    private Usuario buscarUsuarioLogin(String email) {
        if (email == null) {
            return null;
        }

        for (Usuario usuario : Usuario.usuarios) {
            if (usuario.getEmailUsuario() != null &&
                    usuario.getEmailUsuario().equalsIgnoreCase(email.trim())) {
                return usuario;
            }
        }

        return null;
    }
}