package copamundo.selecoes.visao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TelaMenuSelecoesJogadoresController {

    @FXML private Button btnSelecoes;
    @FXML private Button btnJogadores;
    @FXML private Button btnConsulta;

    @FXML
    private void abrirCadastroSelecoes() throws Exception {
        abrirNovaJanela("TelaCadastroSelecao.fxml", "Cadastro de Seleções", 650, 500);
    }

    @FXML
    private void abrirCadastroJogadores() throws Exception {
        abrirNovaJanela("TelaCadastroJogador.fxml", "Cadastro de Jogadores", 750, 550);
    }

    @FXML
    private void abrirConsulta() throws Exception {
        abrirNovaJanela("TelaConsultaSelecoesJogadores.fxml", "Consultas", 850, 550);
    }

    private void abrirNovaJanela(String fxml, String titulo, int largura, int altura) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root, largura, altura));
        stage.show();
    }
}