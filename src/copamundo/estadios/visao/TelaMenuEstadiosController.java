package copamundo.estadios.visao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaMenuEstadiosController {
    @FXML
    private void abrirCadastroEstadio() {
        abrirNovaJanela("TelaCadastroEstadios.fxml", "Cadastro de Estadios", 650, 500);
    }

    @FXML
    private void abrirCadastroArbitro() {
        abrirNovaJanela("TelaCadastroArbitro.fxml", "Cadastro de Arbitros", 650, 500);
    }

    @FXML
    private void abrirDesignacao() {
        abrirNovaJanela("TelaDesignacaoArbitro.fxml", "Designacao de Arbitros", 700, 520);
    }

    private void abrirNovaJanela(String fxml, String titulo, int largura, int altura) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root, largura, altura));
            stage.show();
        } catch (IOException erro) {
            mostrarErro("Erro de navegacao", "Nao foi possivel carregar a tela: " + fxml);
        }
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
