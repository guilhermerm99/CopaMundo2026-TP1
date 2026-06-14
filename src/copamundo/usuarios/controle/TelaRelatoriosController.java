package copamundo.usuarios.controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class TelaRelatoriosController {
    private RelatoriosController relatoriosLogic = new RelatoriosController();

    @FXML
    private void handleMenuUsuarios(ActionEvent event) throws IOException {
        trocarTela(event, "/copamundo/usuarios/visao/TelaUsuarios.fxml");
    }

    @FXML
    private void handleMenuRelatorios(ActionEvent event) throws IOException {
        trocarTela(event, "/copamundo/usuarios/visao/TelaRelatorios.fxml");
    }

    @FXML
    private void handleGerarPDF(ActionEvent event) {
        relatoriosLogic.gerarRelatorio();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório em PDF");
        fileChooser.setInitialFileName("Relatorio_Copa.pdf");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivo PDF", "*.pdf")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File arquivoDestino = fileChooser.showSaveDialog(stage);

        if (arquivoDestino != null) {
            boolean sucesso = relatoriosLogic.gerarArquivoPDF(arquivoDestino.getAbsolutePath());

            if (sucesso) {
                System.out.println("Operação concluída. Arquivo salvo em: " + arquivoDestino.getAbsolutePath());
            } else {
                System.out.println("Falha na operação de salvar o PDF.");
            }
        }
    }

    private void trocarTela(ActionEvent event, String caminhoFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}