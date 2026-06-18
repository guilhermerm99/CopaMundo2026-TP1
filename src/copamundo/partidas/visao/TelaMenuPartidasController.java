package copamundo.partidas.visao;

import copamundo.usuarios.controle.TelaLoginController;
import copamundo.usuarios.controle.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

public class TelaMenuPartidasController {

    @FXML
    private Tab tabCadastroPartidas;

    @FXML
    private Tab tabConsultaPartidas;

    @FXML
    private Tab tabRegistroResultados;


    private void desativarTab() {
        tabCadastroPartidas.setDisable(true);
        tabRegistroResultados.setDisable(true);
    }

    private void ativarTab() {
        tabCadastroPartidas.setDisable(false);
        tabRegistroResultados.setDisable(false);
    }

        
    @FXML
    public void initialize() {

        Usuario usuarioLogado = TelaLoginController.getUsuarioLogado();

        if (usuarioLogado != null && usuarioLogado.getFuncao() == Usuario.Funcao.ARBITRO) {
            desativarTab();
        }
        else {
            ativarTab();
        }
        
        Parent cadastroPartidas = null;
        try {
            cadastroPartidas = FXMLLoader.load(
                    getClass().getResource("TelaCadastroPartidas.fxml")
            );

            Parent consultaPartidas = FXMLLoader.load(
                    getClass().getResource("TelaConsultaPartidas.fxml")
            );

            Parent registroResultados = FXMLLoader.load(
                    getClass().getResource("TelaRegistroResultados.fxml")
            );

            tabCadastroPartidas.setContent(cadastroPartidas);
            tabConsultaPartidas.setContent(consultaPartidas);
            tabRegistroResultados.setContent(registroResultados);

        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

    }

}