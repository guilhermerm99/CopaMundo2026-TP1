package copamundo.estadios.visao;

import copamundo.comum.Partida;
import copamundo.estadios.controle.ArbitroController;
import copamundo.estadios.controle.EstadioController;
import copamundo.estadios.controle.PartidaController;
import copamundo.estadios.modelo.Arbitro;
import copamundo.estadios.modelo.Estadio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class TelaDesignacaoArbitroController {
    @FXML private ComboBox<Estadio> cbEstadio;
    @FXML private ComboBox<Arbitro> cbArbitro;
    @FXML private ComboBox<Partida> cbPartida;
    @FXML private DatePicker dpData;
    @FXML private TableView<Partida> tabelaDesignacoes;
    @FXML private TableColumn<Partida, Estadio> colunaEstadio;
    @FXML private TableColumn<Partida, Arbitro> colunaArbitro;
    @FXML private TableColumn<Partida, String> colunaPartida;
    @FXML private TableColumn<Partida, LocalDateTime> colunaData;

    private final EstadioController estadioController = new EstadioController();
    private final ArbitroController arbitroController = new ArbitroController();
    private final PartidaController partidaController = new PartidaController();
    private final ObservableList<Partida> partidas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colunaEstadio.setCellValueFactory(new PropertyValueFactory<>("estadio"));
        colunaArbitro.setCellValueFactory(new PropertyValueFactory<>("arbitroPrincipal"));
        colunaPartida.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("dataHora"));
        tabelaDesignacoes.setItems(partidas);
        carregarDados();
    }

    @FXML
    private void designarArbitro() {
        try {
            Arbitro arbitro = cbArbitro.getValue();
            Partida partida = cbPartida.getValue();

            if (partida == null) {
                if (cbEstadio.getValue() == null || dpData.getValue() == null) {
                    throw new IllegalArgumentException("Selecione estadio e data para criar a partida.");
                }
                partida = partidaController.criarPartidaParaDesignacao(cbEstadio.getValue(), dpData.getValue(), arbitro);
            } else {
                partidaController.designarArbitroPrincipal(partida, arbitro);
            }

            carregarDados();
            limparCampos();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Arbitro designado com sucesso.");
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        cbEstadio.getSelectionModel().clearSelection();
        cbArbitro.getSelectionModel().clearSelection();
        cbPartida.getSelectionModel().clearSelection();
        dpData.setValue(null);
    }

    private void carregarDados() {
        try {
            cbEstadio.setItems(FXCollections.observableArrayList(estadioController.listarEstadios()));
            cbArbitro.setItems(FXCollections.observableArrayList(arbitroController.listarArbitros()));
            partidas.setAll(partidaController.listarPartidas());
            cbPartida.setItems(FXCollections.observableArrayList(partidas));
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
