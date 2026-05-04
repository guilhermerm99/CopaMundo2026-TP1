package copamundo.usuarios.controle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class UsuarioController implements Initializable {
    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;

    @FXML private ComboBox<String> cbStatus;
    @FXML private ComboBox<String> cbPais;
    @FXML private ComboBox<String> cbFuncao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbStatus.getItems().addAll("Ativo", "Inativo");
        cbPais.getItems().addAll();
        cbFuncao.getItems().addAll("Administrador", "Organizador", "Árbitro");
    }

    @FXML
    private void onSalvar() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(txtNome.getText());
        novoUsuario.setCpf(txtCpf.getText());
        novoUsuario.setEmail(txtEmail.getText());
        novoUsuario.setStatus(cbStatus.getValue());
        novoUsuario.setStatus(cbPais.getValue());
        novoUsuario.setStatus(cbFuncao.getValue());
        novoUsuario.setSenha(txtSenha.getText());

        System.out.println("Usuário salvo.");
    }

    @FXML
    private void onCancelar() {
        txtNome.clear();
        txtCpf.clear();
        txtEmail.clear();
        cbStatus.setValue(null);
        cbPais.setValue(null);
        cbFuncao.setValue(null);
        txtEmail.clear();
    }
}