package copamundo.partidas.visao;

import com.itextpdf.io.IOException;
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


    @FXML
    public void initialize() throws IOException {

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