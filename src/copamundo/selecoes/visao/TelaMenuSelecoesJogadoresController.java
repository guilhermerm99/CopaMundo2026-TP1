package copamundo.selecoes.visao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class TelaMenuSelecoesJogadoresController {

    @FXML private Button btnSelecoes;
    @FXML private Button btnJogadores;
    @FXML private Button btnConsulta;

    @FXML
    private void abrirCadastroSelecoes() {
        abrirNovaJanela("TelaCadastroSelecao.fxml", "Cadastro de Seleções", 650, 500);
    }

    @FXML
    private void abrirCadastroJogadores() {
        abrirNovaJanela("TelaCadastroJogador.fxml", "Cadastro de Jogadores", 750, 550);
    }

    @FXML
    private void abrirConsulta() {
        abrirNovaJanela("TelaConsultaSelecoesJogadores.fxml", "Consultas", 850, 550);
    }

    // Método refatorado para tratar exceções com try-catch (Boas Práticas e Robustez)
    private void abrirNovaJanela(String fxml, String titulo, int largura, int altura) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root, largura, altura));
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro de Navegação", "Não foi possível carregar a tela: " + fxml + "\nVerifique se o arquivo FXML está no mesmo diretório do controlador.");
            e.printStackTrace(); // Imprime o erro técnico no console para debug
        }
    }

    // Alerta de erro para garantir feedback visual em caso de falha
    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}