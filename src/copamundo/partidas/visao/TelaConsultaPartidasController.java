package copamundo.partidas.visao;
import copamundo.comum.*;
import copamundo.estadios.controle.EstadioController;
import copamundo.estadios.modelo.Arbitro;
import copamundo.estadios.modelo.Estadio;
import copamundo.partidas.repositorio.PartidaRepositorio;
import copamundo.selecoes.persistencia.PersistenciaSelecoesJogadores;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import static copamundo.partidas.repositorio.PartidaRepositorio.listaPartidas;

public class TelaConsultaPartidasController {
    // AJUSTAR METODOS
    @FXML
    private TableView<Partida> tabelaInteira;

    @FXML
    private ComboBox<Fase> seletorFase;

    @FXML
    private Button btnFiltrarConsulta;

    @FXML
    private Button btnLimparFiltros;

    @FXML
    private Button btnTelaCadastroPartidas;

    @FXML
    private Button btnTelaRegistroResultados;

    @FXML
    private TableColumn<Partida, String> colunaData;

    @FXML
    private TableColumn<Partida, Estadio> colunaEstadio;

    @FXML
    private TableColumn<Partida, Fase> colunaFase;

    @FXML
    private TableColumn<Partida, String> colunaHorario;

    @FXML
    private TableColumn<Partida, Resultado> colunaPlacar;

    @FXML
    private TableColumn<Partida, String> colunaSelecoes;

    @FXML
    private TableColumn<Partida, StatusPartida> colunaStatus;

    @FXML
    private TableColumn<Partida, Void> colunaEditar;

    @FXML
    private TableColumn<Partida, Void> colunaExcluir;

    @FXML
    private ComboBox<Selecao> seletorSelecao;

    @FXML
    private ComboBox<StatusPartida> seletorStatus;

    @FXML
    private TextField textoData;

    @FXML
    void filtrarListaPartidas(javafx.event.ActionEvent event) {
        carregarTabela();
    }

    public void initialize() {
        try {
            PersistenciaSelecoesJogadores selecoesObjeto = new PersistenciaSelecoesJogadores();
            List<Selecao> listaSelecoes = selecoesObjeto.carregarSelecoes();
            List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

            seletorFase.getItems().addAll(Fase.values());
            seletorStatus.getItems().addAll(StatusPartida.values());
            seletorSelecao.getItems().addAll(listaSelecoes);

            colunaData.setCellValueFactory(
                    new PropertyValueFactory<>("dataPatida")
            );

            colunaEstadio.setCellValueFactory(
                    new PropertyValueFactory<>("estadioPartida")
            );

            colunaFase.setCellValueFactory(
                    new PropertyValueFactory<>("fase")
            );

            colunaHorario.setCellValueFactory(
                    new PropertyValueFactory<>("horarioPartida")
            );

            colunaPlacar.setCellValueFactory(
                    new PropertyValueFactory<>("resultado")
            );

            colunaSelecoes.setCellValueFactory(cellData ->
                    new SimpleStringProperty(
                            cellData.getValue().nomeSelecoes()
                    )
            );

            colunaStatus.setCellValueFactory(
                    new PropertyValueFactory<>("status")
            );

            colunaExcluir.setCellFactory(param -> new TableCell<>() {

                private final Button btnExcluir = new Button("Excluir");

                {
                    btnExcluir.setOnAction(event -> {

                        Partida partida = getTableView().getItems().get(getIndex());

                        for (int i = 0; i < listaPartidas.size(); i++) {
                            if (listaPartidas.get(i).getId().equals(partida.getId())) {
                                listaPartidas.remove(i);
                                try {
                                    PartidaRepositorio.salvarListaPartidas(listaPartidas);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                break;
                            }
                        }

                        tabelaInteira.getItems().remove(partida);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnExcluir);
                    }
                }
            });


            colunaEditar.setCellFactory(param -> new TableCell<>() {

                private final Button btnEditar = new Button("Editar");

                {
                    btnEditar.setOnAction(event -> {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("TelaModalEditarPartida.fxml"));

                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();

                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnEditar);
                    }
                }
            });

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        carregarTabela();
    }


    private void carregarTabela() {

        try {
            List<Partida> lista = PartidaRepositorio.carregarListaPartidas();

            String data = textoData.getText();
            Fase fase = seletorFase.getValue();
            Selecao selecao = seletorSelecao.getValue();
            StatusPartida status = seletorStatus.getValue();

            if (data != "") {
                lista = listaPorData(data, lista);
            }

            if (fase != null) {
                lista = listaPorFase(fase, lista);
            }

            if (selecao != null) {
                lista = listaPorSelecao(selecao, lista);
            }

            if (status != null) {
                lista = listaPorStatusPartida(status, lista);
            }

            ObservableList<Partida> dados = FXCollections.observableArrayList(lista);

            tabelaInteira.setItems(dados);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void limparFiltros(javafx.event.ActionEvent event) {
        // carrega a lista inteira sem filtragem
        try {
            List<Partida> lista = PartidaRepositorio.carregarListaPartidas();

            ObservableList<Partida> dados = FXCollections.observableArrayList(lista);

            tabelaInteira.setItems(dados);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irTelaCadastroPartidas(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("TelaCadastroPartidas.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void irTelaRegistroResultados(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("TelaRegistroResultados.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

/////////////////////////////////////////////////////

    public List<Partida>  listaPorSelecao (Selecao selecao, List<Partida> lista) {
        ArrayList<Partida> listaFiltradaPartidas = new ArrayList<Partida>();

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getSelecao1().equals(selecao) || lista.get(i).getSelecao2().equals(selecao)) {
                listaFiltradaPartidas.add(lista.get(i));
            }
        }
        return listaFiltradaPartidas;
    }

    public List<Partida> listaPorData (String data, List<Partida> lista) {
        ArrayList<Partida> listaFiltradaPartidas = new ArrayList<Partida>();

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getDataPartida().equals(data)) {
                listaFiltradaPartidas.add(lista.get(i));
            }
        }
        return listaFiltradaPartidas;
    }

    public static List<Partida>  listaPorFase(Fase fase, List<Partida> lista) {
        ArrayList<Partida> listaFiltradaPartidas = new ArrayList<Partida>();

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getFase().equals(fase)) {
                listaFiltradaPartidas.add(lista.get(i));
            }

        }
        return listaFiltradaPartidas;
    }

    public List<Partida> listaPorStatusPartida (StatusPartida status, List<Partida> lista) {
        ArrayList<Partida> listaFiltradaPartidas = new ArrayList<Partida>();

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getStatusPartida().equals(status)) {
                listaFiltradaPartidas.add(lista.get(i));
            }

        }
        return listaFiltradaPartidas;
    }
}
