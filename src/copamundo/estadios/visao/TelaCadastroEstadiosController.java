package copamundo.estadios.visao;

import copamundo.estadios.controle.EstadioController;
import copamundo.estadios.modelo.Estadio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaCadastroEstadiosController {
    @FXML private TextField txtNome;
    @FXML private TextField txtCidade;
    @FXML private TextField txtCapacidade;
    @FXML private TableView<Estadio> tabelaEstadios;
    @FXML private TableColumn<Estadio, String> colunaNome;
    @FXML private TableColumn<Estadio, String> colunaCidade;
    @FXML private TableColumn<Estadio, Integer> colunaCapacidade;

    private final EstadioController controller = new EstadioController();
    private final ObservableList<Estadio> estadios = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colunaCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
        tabelaEstadios.setItems(estadios);
        carregarEstadios();
    }

    @FXML
    private void salvarEstadio() {
        try {
            int capacidade = Integer.parseInt(txtCapacidade.getText().trim());
            controller.cadastrarEstadio(txtNome.getText(), txtCidade.getText(), capacidade);
            carregarEstadios();
            limparCampos();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Estadio cadastrado com sucesso.");
        } catch (NumberFormatException erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Informe uma capacidade numerica valida.");
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        txtNome.clear();
        txtCidade.clear();
        txtCapacidade.clear();
        tabelaEstadios.getSelectionModel().clearSelection();
    }

    private void carregarEstadios() {
        try {
            estadios.setAll(controller.listarEstadios());
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
