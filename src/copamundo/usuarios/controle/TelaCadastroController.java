package copamundo.usuarios.controle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaCadastroController {
    @FXML
    private TextField campoNomeUsuario;
    @FXML
    private TextField campoCpfUsuario;
    @FXML
    private TextField campoEmailUsuario;
    @FXML
    private PasswordField campoSenhaUsuario;
    @FXML
    private ComboBox<Usuario.Status> campoStatus;
    @FXML
    private ComboBox<Usuario.Funcao> campoFuncao;
    @FXML
    private ComboBox<Usuario.Pais> campoPais;
    @FXML
    private void initialize() {
        campoFuncao.setItems(
                FXCollections.observableArrayList(Usuario.Funcao.values())
        );

        campoPais.setItems(
                FXCollections.observableArrayList(Usuario.Pais.values())
        );
        campoStatus.setItems(
                FXCollections.observableArrayList(Usuario.Status.values())
        );
    }
    @FXML
    private void entrar(ActionEvent event) {
        String nomeUsuario = campoNomeUsuario.getText();
        String Cpf = campoCpfUsuario.getText();
        String emailUsuario = campoEmailUsuario.getText();
        Usuario.Status status = campoStatus.getValue();
        Usuario.Pais pais = campoPais.getValue();
        Usuario.Funcao funcao = campoFuncao.getValue();
        String senhaUsuario = campoSenhaUsuario.getText();

        Usuario novoUsuario = new Usuario(nomeUsuario, emailUsuario, senhaUsuario, status, pais, funcao, Cpf);

        boolean salvoComSucesso = Usuario.salvarUsuarioNoBanco(novoUsuario);

        if (salvoComSucesso) {
            System.out.println("Usuário salvo com sucesso! Total na lista: " + Usuario.usuarios.size());
            try {
                telaUsuario(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Falha na validação dos dados. Verifique os campos.");
        }
    }

    @FXML
    private void telaUsuario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/copamundo/usuarios/visao/TelaLogin.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void voltarTelaUsuarios(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/copamundo/usuarios/visao/TelaUsuarios.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
