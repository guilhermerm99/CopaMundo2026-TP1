package copamundo.usuarios.controle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class TelaLoginController {
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;

    @FXML
    private void onEntrar() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            exibirMensagem("Erro", "Por favor, preencha todos os campos");
            return;
        }
        if (verificarSenha(senha)) {
            System.out.println("Bem-vindo(a) de volta!");
            irParaTelaInicial();
        } else {
            exibirMensagem("Falha no login", "Senha ou email incorretos.");
        }
    }

    private void irParaTelaInicial() {
    }

    private void exibirMensagem(String titulo, String mensagem) {
        Alerta alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    public void onCadastrarse() {
    }

}
