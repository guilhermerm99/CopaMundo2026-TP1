package copamundo.estadios.visao;

import copamundo.estadios.controle.ArbitroController;
import copamundo.estadios.modelo.Arbitro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaCadastroArbitroController {
    @FXML private TextField txtNome;
    @FXML private TextField txtFederacao;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private TableView<Arbitro> tabelaArbitros;
    @FXML private TableColumn<Arbitro, String> colunaNome;
    @FXML private TableColumn<Arbitro, String> colunaFederacao;
    @FXML private TableColumn<Arbitro, String> colunaCategoria;

    private final ArbitroController controller = new ArbitroController();
    private final ObservableList<Arbitro> arbitros = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        cbCategoria.setItems(FXCollections.observableArrayList("Principal", "Assistente", "VAR"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaFederacao.setCellValueFactory(new PropertyValueFactory<>("federacao"));
        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        tabelaArbitros.setItems(arbitros);
        carregarArbitros();
    }

    @FXML
    private void salvarArbitro() {
        try {
            controller.cadastrarArbitro(txtNome.getText(), txtFederacao.getText(), cbCategoria.getValue());
            carregarArbitros();
            limparCampos();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Arbitro cadastrado com sucesso.");
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        txtNome.clear();
        txtFederacao.clear();
        cbCategoria.getSelectionModel().clearSelection();
        tabelaArbitros.getSelectionModel().clearSelection();
    }

    private void carregarArbitros() {
        try {
            arbitros.setAll(controller.listarArbitros());
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
