package copamundo.estadios.visao;

import copamundo.comum.Partida;
import copamundo.estadios.controle.ArbitroController;
import copamundo.estadios.controle.EstadioController;
import copamundo.estadios.controle.PartidaController;
import copamundo.estadios.modelo.Arbitro;
import copamundo.estadios.modelo.Estadio;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Locale;

public class TelaMenuEstadiosController {
    @FXML private TabPane tabPane;
    @FXML private Tab tabEstadios;
    @FXML private Tab tabArbitros;
    @FXML private Tab tabDesignacoes;
    @FXML private Tab tabConsulta;

    @FXML private TextField txtNomeEstadio;
    @FXML private TextField txtCidadeEstadio;
    @FXML private TextField txtCapacidadeEstadio;
    @FXML private TextField txtBuscaEstadio;
    @FXML private TableView<Estadio> tabelaEstadios;
    @FXML private TableColumn<Estadio, Integer> colunaEstadioId;
    @FXML private TableColumn<Estadio, String> colunaEstadioNome;
    @FXML private TableColumn<Estadio, String> colunaEstadioCidade;
    @FXML private TableColumn<Estadio, Integer> colunaEstadioCapacidade;

    @FXML private TextField txtNomeArbitro;
    @FXML private TextField txtFederacaoArbitro;
    @FXML private ComboBox<String> cbCategoriaArbitro;
    @FXML private ComboBox<String> cbExperienciaArbitro;
    @FXML private TextField txtBuscaArbitro;
    @FXML private TableView<Arbitro> tabelaArbitros;
    @FXML private TableColumn<Arbitro, Integer> colunaArbitroId;
    @FXML private TableColumn<Arbitro, String> colunaArbitroNome;
    @FXML private TableColumn<Arbitro, String> colunaArbitroFederacao;
    @FXML private TableColumn<Arbitro, String> colunaArbitroCategoria;
    @FXML private TableColumn<Arbitro, String> colunaArbitroExperiencia;

    @FXML private ComboBox<Partida> cbPartidaDesignacao;
    @FXML private ComboBox<Arbitro> cbArbitroDesignacao;
    @FXML private Label lblEstadioDesignacao;
    @FXML private TextField txtFiltroSelecaoDesignacao;
    @FXML private Label lblResumoArbitros;
    @FXML private TableView<Partida> tabelaDesignacoes;
    @FXML private TableColumn<Partida, String> colunaDesignacaoPartida;
    @FXML private TableColumn<Partida, String> colunaDesignacaoData;
    @FXML private TableColumn<Partida, String> colunaDesignacaoEstadio;
    @FXML private TableColumn<Partida, String> colunaDesignacaoArbitro;
    @FXML private TableColumn<Partida, String> colunaDesignacaoStatus;

    @FXML private TextField txtConsultaGeral;
    @FXML private TableView<Estadio> tabelaConsultaEstadios;
    @FXML private TableColumn<Estadio, String> colunaConsultaEstadioNome;
    @FXML private TableColumn<Estadio, String> colunaConsultaEstadioCidade;
    @FXML private TableColumn<Estadio, Integer> colunaConsultaEstadioCapacidade;
    @FXML private TableView<Arbitro> tabelaConsultaArbitros;
    @FXML private TableColumn<Arbitro, String> colunaConsultaArbitroNome;
    @FXML private TableColumn<Arbitro, String> colunaConsultaArbitroFederacao;
    @FXML private TableColumn<Arbitro, String> colunaConsultaArbitroCategoria;
    @FXML private TableColumn<Arbitro, String> colunaConsultaArbitroExperiencia;
    @FXML private TableView<Partida> tabelaConsultaDesignacoes;
    @FXML private TableColumn<Partida, String> colunaConsultaDesignacaoPartida;
    @FXML private TableColumn<Partida, String> colunaConsultaDesignacaoArbitro;
    @FXML private TableColumn<Partida, String> colunaConsultaDesignacaoEstadio;

    private final EstadioController estadioController = new EstadioController();
    private final ArbitroController arbitroController = new ArbitroController();
    private final PartidaController partidaController = new PartidaController();

    private final ObservableList<Estadio> estadios = FXCollections.observableArrayList();
    private final ObservableList<Arbitro> arbitros = FXCollections.observableArrayList();
    private final ObservableList<Partida> partidas = FXCollections.observableArrayList();

    private Integer estadioEmEdicao;
    private Integer arbitroEmEdicao;

    @FXML
    private void initialize() {
        configurarTabelas();
        configurarCombos();
        configurarSelecoes();
        carregarTudo();
    }

    @FXML private void abrirAbaEstadios() { tabPane.getSelectionModel().select(tabEstadios); }
    @FXML private void abrirAbaArbitros() { tabPane.getSelectionModel().select(tabArbitros); }
    @FXML private void abrirAbaDesignacoes() { tabPane.getSelectionModel().select(tabDesignacoes); }
    @FXML private void abrirAbaConsulta() { tabPane.getSelectionModel().select(tabConsulta); }

    @FXML
    private void recarregarTudo() {
        carregarTudo();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Atualizado", "Dados recarregados com sucesso.");
    }

    @FXML
    private void novoEstadio() {
        estadioEmEdicao = null;
        txtNomeEstadio.clear();
        txtCidadeEstadio.clear();
        txtCapacidadeEstadio.clear();
        tabelaEstadios.getSelectionModel().clearSelection();
        txtNomeEstadio.requestFocus();
    }

    @FXML
    private void salvarEstadio() {
        try {
            int capacidade = FormatadorCampos.lerCapacidade(txtCapacidadeEstadio.getText());
            if (estadioEmEdicao == null) {
                estadioController.cadastrarEstadio(txtNomeEstadio.getText(), txtCidadeEstadio.getText(), capacidade);
            } else {
                estadioController.editarEstadio(estadioEmEdicao, txtNomeEstadio.getText(), txtCidadeEstadio.getText(), capacidade);
            }
            carregarTudo();
            novoEstadio();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Estadio salvo com sucesso.");
        } catch (NumberFormatException erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Informe uma capacidade numerica valida. Exemplo: 34534 ou 34.534.");
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void excluirEstadio() {
        Estadio selecionado = tabelaEstadios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atencao", "Selecione um estadio para excluir.");
            return;
        }

        try {
            estadioController.excluirEstadio(selecionado.getId());
            carregarTudo();
            novoEstadio();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Estadio excluido com sucesso.");
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void filtrarEstadios() {
        try {
            String filtro = txtBuscaEstadio.getText();
            estadios.setAll(estadioController.buscarEstadios(filtro, filtro));
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void limparFiltroEstadios() {
        txtBuscaEstadio.clear();
        carregarEstadios();
    }

    @FXML
    private void novoArbitro() {
        arbitroEmEdicao = null;
        txtNomeArbitro.clear();
        txtFederacaoArbitro.clear();
        cbCategoriaArbitro.getSelectionModel().clearSelection();
        cbExperienciaArbitro.getSelectionModel().clearSelection();
        tabelaArbitros.getSelectionModel().clearSelection();
        txtNomeArbitro.requestFocus();
    }

    @FXML
    private void salvarArbitro() {
        try {
            if (arbitroEmEdicao == null) {
                arbitroController.cadastrarArbitro(
                        txtNomeArbitro.getText(),
                        txtFederacaoArbitro.getText(),
                        cbCategoriaArbitro.getValue(),
                        cbExperienciaArbitro.getValue()
                );
            } else {
                arbitroController.editarArbitro(
                        arbitroEmEdicao,
                        txtNomeArbitro.getText(),
                        txtFederacaoArbitro.getText(),
                        cbCategoriaArbitro.getValue(),
                        cbExperienciaArbitro.getValue()
                );
            }
            carregarTudo();
            novoArbitro();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Arbitro salvo com sucesso.");
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void excluirArbitro() {
        Arbitro selecionado = tabelaArbitros.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atencao", "Selecione um arbitro para excluir.");
            return;
        }

        try {
            arbitroController.excluirArbitro(selecionado.getId());
            carregarTudo();
            novoArbitro();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Arbitro excluido com sucesso.");
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void filtrarArbitros() {
        try {
            String filtro = txtBuscaArbitro.getText();
            arbitros.setAll(arbitroController.buscarArbitros(filtro, filtro, null));
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void limparFiltroArbitros() {
        txtBuscaArbitro.clear();
        carregarArbitros();
    }

    @FXML
    private void designarArbitro() {
        try {
            partidaController.designarArbitroPrincipal(cbPartidaDesignacao.getValue(), cbArbitroDesignacao.getValue());
            carregarTudo();
            limparDesignacao();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Designacao registrada com sucesso.");
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void validarPartidaSelecionada() {
        try {
            partidaController.validarPartidaComArbitroPrincipal(cbPartidaDesignacao.getValue());
            mostrarAlerta(Alert.AlertType.INFORMATION, "Partida valida", "A partida possui arbitro principal designado.");
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void limparArbitroSelecionado() {
        cbArbitroDesignacao.getSelectionModel().clearSelection();
    }

    @FXML
    private void filtrarDesignacoes() {
        try {
            partidas.setAll(partidaController.buscarPartidas(
                    txtFiltroSelecaoDesignacao.getText(),
                    null,
                    null,
                    false
            ));
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void limparDesignacao() {
        cbPartidaDesignacao.getSelectionModel().clearSelection();
        cbArbitroDesignacao.getSelectionModel().clearSelection();
        lblEstadioDesignacao.setText("Selecione uma partida");
        txtFiltroSelecaoDesignacao.clear();
        tabelaDesignacoes.getSelectionModel().clearSelection();
        carregarPartidas();
        atualizarArbitrosAptos(null);
    }

    @FXML
    private void consultarTudo() {
        try {
            String filtro = txtConsultaGeral.getText();
            tabelaConsultaEstadios.setItems(FXCollections.observableArrayList(estadioController.buscarEstadios(filtro, filtro)));
            tabelaConsultaArbitros.setItems(FXCollections.observableArrayList(arbitroController.buscarArbitros(filtro, filtro, null)));
            tabelaConsultaDesignacoes.setItems(FXCollections.observableArrayList(buscarDesignacoesConsulta(filtro)));
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    @FXML
    private void limparConsulta() {
        txtConsultaGeral.clear();
        tabelaConsultaEstadios.setItems(FXCollections.observableArrayList(estadios));
        tabelaConsultaArbitros.setItems(FXCollections.observableArrayList(arbitros));
        tabelaConsultaDesignacoes.setItems(FXCollections.observableArrayList(filtrarDesignadas(partidas)));
    }

    private void configurarTabelas() {
        colunaEstadioId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaEstadioNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaEstadioCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colunaEstadioCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
        tabelaEstadios.setItems(estadios);

        colunaArbitroId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaArbitroNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaArbitroFederacao.setCellValueFactory(new PropertyValueFactory<>("federacao"));
        colunaArbitroCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colunaArbitroExperiencia.setCellValueFactory(new PropertyValueFactory<>("experiencia"));
        tabelaArbitros.setItems(arbitros);

        colunaDesignacaoPartida.setCellValueFactory(item -> texto(item.getValue().nomeSelecoes()));
        colunaDesignacaoData.setCellValueFactory(item -> texto(item.getValue().getDataHora()));
        colunaDesignacaoEstadio.setCellValueFactory(item -> texto(formatarEstadio(item.getValue().getEstadioPartida())));
        colunaDesignacaoArbitro.setCellValueFactory(item -> texto(formatarArbitro(item.getValue().getArbitroPrincipal())));
        colunaDesignacaoStatus.setCellValueFactory(item -> texto(String.valueOf(item.getValue().getStatusPartida())));
        tabelaDesignacoes.setItems(partidas);

        colunaConsultaEstadioNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaConsultaEstadioCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colunaConsultaEstadioCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
        colunaConsultaArbitroNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaConsultaArbitroFederacao.setCellValueFactory(new PropertyValueFactory<>("federacao"));
        colunaConsultaArbitroCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colunaConsultaArbitroExperiencia.setCellValueFactory(new PropertyValueFactory<>("experiencia"));
        colunaConsultaDesignacaoPartida.setCellValueFactory(item -> texto(item.getValue().nomeSelecoes()));
        colunaConsultaDesignacaoArbitro.setCellValueFactory(item -> texto(formatarArbitro(item.getValue().getArbitroPrincipal())));
        colunaConsultaDesignacaoEstadio.setCellValueFactory(item -> texto(formatarEstadio(item.getValue().getEstadioPartida())));
    }

    private void configurarCombos() {
        cbCategoriaArbitro.setItems(FXCollections.observableArrayList("Principal", "Assistente", "VAR", "Quarto arbitro"));
        cbExperienciaArbitro.setItems(FXCollections.observableArrayList(opcoesExperiencia()));
    }

    private void configurarSelecoes() {
        cbPartidaDesignacao.getSelectionModel().selectedItemProperty().addListener((obs, anterior, partida) -> {
            atualizarArbitrosAptos(partida);
            if (partida != null) {
                tabelaDesignacoes.getSelectionModel().select(partida);
                lblEstadioDesignacao.setText(formatarEstadio(partida.getEstadioPartida()));
            } else {
                lblEstadioDesignacao.setText("Selecione uma partida");
            }
        });

        tabelaEstadios.getSelectionModel().selectedItemProperty().addListener((obs, anterior, selecionado) -> {
            if (selecionado != null) {
                estadioEmEdicao = selecionado.getId();
                txtNomeEstadio.setText(selecionado.getNome());
                txtCidadeEstadio.setText(selecionado.getCidade());
                txtCapacidadeEstadio.setText(String.format(Locale.forLanguageTag("pt-BR"), "%,d", selecionado.getCapacidade()).replace(",", "."));
            }
        });

        tabelaArbitros.getSelectionModel().selectedItemProperty().addListener((obs, anterior, selecionado) -> {
            if (selecionado != null) {
                arbitroEmEdicao = selecionado.getId();
                txtNomeArbitro.setText(selecionado.getNome());
                txtFederacaoArbitro.setText(selecionado.getFederacao());
                cbCategoriaArbitro.setValue(selecionado.getCategoria());
                cbExperienciaArbitro.setValue(selecionado.getExperiencia());
            }
        });

        tabelaDesignacoes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, selecionada) -> {
            if (selecionada != null) {
                cbPartidaDesignacao.setValue(selecionada);
                atualizarArbitrosAptos(selecionada);
                if (selecionada.getArbitroPrincipal() != null) {
                    cbArbitroDesignacao.setValue(selecionada.getArbitroPrincipal());
                }
                lblEstadioDesignacao.setText(formatarEstadio(selecionada.getEstadioPartida()));
            }
        });
    }

    private void carregarTudo() {
        carregarEstadios();
        carregarArbitros();
        carregarPartidas();
        atualizarCombosDeApoio();
        limparConsulta();
    }

    private void carregarEstadios() {
        try {
            estadios.setAll(estadioController.listarEstadios());
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    private void carregarArbitros() {
        try {
            arbitros.setAll(arbitroController.listarArbitros());
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    private void carregarPartidas() {
        try {
            partidas.setAll(partidaController.listarPartidas());
        } catch (Exception erro) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", erro.getMessage());
        }
    }

    private void atualizarCombosDeApoio() {
        cbPartidaDesignacao.setItems(FXCollections.observableArrayList(partidas));
        atualizarArbitrosAptos(cbPartidaDesignacao.getValue());
    }

    private void atualizarArbitrosAptos(Partida partida) {
        try {
            if (partida == null) {
                cbArbitroDesignacao.setItems(FXCollections.observableArrayList(arbitros));
                lblResumoArbitros.setText("Selecione uma partida para ver os arbitros aptos.");
                return;
            }

            List<Arbitro> aptos = partidaController.listarArbitrosAptosParaPartida(partida);
            if (partida.getArbitroPrincipal() != null && !aptos.contains(partida.getArbitroPrincipal())) {
                aptos = new java.util.ArrayList<>(aptos);
                aptos.add(partida.getArbitroPrincipal());
            }

            cbArbitroDesignacao.setItems(FXCollections.observableArrayList(aptos));
            if (partida.getArbitroPrincipal() != null) {
                cbArbitroDesignacao.setValue(partida.getArbitroPrincipal());
            } else {
                cbArbitroDesignacao.getSelectionModel().clearSelection();
            }

            String status = partida.getArbitroPrincipal() == null ? "sem arbitro principal" : "com arbitro principal";
            lblResumoArbitros.setText(partida.nomeSelecoes() + " - " + status + ". Estadio: "
                    + formatarEstadio(partida.getEstadioPartida()) + ". Arbitros aptos: " + aptos.size() + ".");
        } catch (Exception erro) {
            lblResumoArbitros.setText("Nao foi possivel calcular arbitros aptos: " + erro.getMessage());
        }
    }

    private List<Partida> filtrarDesignadas(List<Partida> origem) {
        return origem.stream()
                .filter(partida -> partida.getArbitroPrincipal() != null)
                .toList();
    }

    private List<Partida> buscarDesignacoesConsulta(String filtro) throws Exception {
        String texto = normalizar(filtro);
        return partidaController.listarDesignacoes().stream()
                .filter(partida -> texto.isBlank()
                        || normalizar(partida.nomeSelecoes()).contains(texto)
                        || normalizar(formatarArbitro(partida.getArbitroPrincipal())).contains(texto)
                        || normalizar(formatarEstadio(partida.getEstadioPartida())).contains(texto)
                        || normalizar(partida.getDataHora()).contains(texto))
                .toList();
    }

    private SimpleStringProperty texto(String valor) {
        return new SimpleStringProperty(valor == null ? "" : valor);
    }

    private String formatarEstadio(Estadio estadio) {
        return estadio == null ? "Sem estadio" : estadio.getNome() + " - " + estadio.getCidade();
    }

    private String formatarArbitro(Arbitro arbitro) {
        return arbitro == null ? "Sem arbitro" : arbitro.getNome() + " (" + arbitro.getFederacao() + ")";
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem == null ? "Operacao nao concluida." : mensagem);
        alerta.showAndWait();
    }

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
    }

    private List<String> opcoesExperiencia() {
        return List.of(
                "1 ano",
                "2 anos",
                "3 anos",
                "4 anos",
                "5 anos",
                "6 anos",
                "7 anos",
                "8 anos",
                "9 anos",
                "10 anos",
                "+10 anos"
        );
    }
}
