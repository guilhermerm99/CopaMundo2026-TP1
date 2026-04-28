package copamundo.selecoes.visao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaConsultaSelecoesJogadoresController {

    @FXML private ComboBox<String> comboGrupoFiltro;
    @FXML private ComboBox<String> comboPosicaoFiltro;
    @FXML private ComboBox<String> comboStatusFiltro;

    @FXML private TableView<TelaCadastroSelecaoController.Selecao> tabelaSelecoes;
    @FXML private TableColumn<TelaCadastroSelecaoController.Selecao, String> colunaPaisConsulta;
    @FXML private TableColumn<TelaCadastroSelecaoController.Selecao, String> colunaGrupoConsulta;
    @FXML private TableColumn<TelaCadastroSelecaoController.Selecao, String> colunaTecnicoConsulta;

    @FXML private TableView<TelaCadastroJogadorController.Jogador> tabelaJogadores;
    @FXML private TableColumn<TelaCadastroJogadorController.Jogador, String> colunaNomeJogador;
    @FXML private TableColumn<TelaCadastroJogadorController.Jogador, String> colunaPosicaoJogador;
    @FXML private TableColumn<TelaCadastroJogadorController.Jogador, Integer> colunaNumeroJogador;
    @FXML private TableColumn<TelaCadastroJogadorController.Jogador, Integer> colunaIdadeJogador;
    @FXML private TableColumn<TelaCadastroJogadorController.Jogador, String> colunaStatusJogador;
    @FXML private TableColumn<TelaCadastroJogadorController.Jogador, String> colunaSelecaoJogador;

    @FXML private Button btnFiltrarSelecao, btnFiltrarJogador;

    private ObservableList<TelaCadastroSelecaoController.Selecao> listaSelecoes = FXCollections.observableArrayList();
    private ObservableList<TelaCadastroJogadorController.Jogador> listaJogadores = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Atualizado para contemplar todos os 12 grupos da Copa de 2026
        comboGrupoFiltro.setItems(FXCollections.observableArrayList(
                "Todos", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"));
        comboGrupoFiltro.setValue("Todos");

        comboPosicaoFiltro.setItems(FXCollections.observableArrayList(
                "Todas", "Goleiro", "Zagueiro", "Lateral", "Meio-campo", "Atacante"));
        comboPosicaoFiltro.setValue("Todas");

        comboStatusFiltro.setItems(FXCollections.observableArrayList(
                "Todos", "Ativo", "Lesionado", "Suspenso"));
        comboStatusFiltro.setValue("Todos");

        // Configurar colunas da tabela de seleções
        colunaPaisConsulta.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaGrupoConsulta.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        colunaTecnicoConsulta.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        tabelaSelecoes.setItems(listaSelecoes);

        // Configurar colunas da tabela de jogadores
        colunaNomeJogador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicaoJogador.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaNumeroJogador.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colunaIdadeJogador.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colunaStatusJogador.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaSelecaoJogador.setCellValueFactory(new PropertyValueFactory<>("selecao"));
        tabelaJogadores.setItems(listaJogadores);
    }

    @FXML
    private void filtrarSelecoes() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Filtro de Seleções", "A busca por grupo será implementada na Etapa 3, conectando com a camada de persistência.");
    }

    @FXML
    private void filtrarJogadores() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Filtro de Jogadores", "A busca por posição e status será implementada na Etapa 3, conectando com a camada de persistência.");
    }

    // Método auxiliar para exibir os alertas de protótipo exigidos
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}