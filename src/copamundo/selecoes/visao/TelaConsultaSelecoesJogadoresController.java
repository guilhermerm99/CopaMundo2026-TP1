package copamundo.selecoes.visao;

import copamundo.comum.Selecao;
import copamundo.comum.Jogador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaConsultaSelecoesJogadoresController {

    // Filtros Seleções
    @FXML private TextField campoBuscaPais;
    @FXML private TextField campoBuscaTecnico;
    @FXML private ComboBox<String> comboGrupoFiltro;

    // Tabela Seleções
    @FXML private TableView<Selecao> tabelaSelecoes;
    @FXML private TableColumn<Selecao, String> colunaPaisConsulta;
    @FXML private TableColumn<Selecao, String> colunaGrupoConsulta;
    @FXML private TableColumn<Selecao, String> colunaTecnicoConsulta;

    // Filtros Jogadores
    @FXML private TextField campoBuscaNomeJogador;
    @FXML private ComboBox<String> comboPosicaoFiltro;
    @FXML private ComboBox<String> comboStatusFiltro;
    @FXML private ComboBox<String> comboSelecaoFiltro;   // Novo filtro por seleção

    // Tabela Jogadores
    @FXML private TableView<Jogador> tabelaJogadores;
    @FXML private TableColumn<Jogador, String> colunaNomeJogador;
    @FXML private TableColumn<Jogador, String> colunaPosicaoJogador;
    @FXML private TableColumn<Jogador, Integer> colunaNumeroJogador;
    @FXML private TableColumn<Jogador, Integer> colunaIdadeJogador;
    @FXML private TableColumn<Jogador, String> colunaStatusJogador;
    @FXML private TableColumn<Jogador, String> colunaSelecaoJogador;

    @FXML private Button btnFiltrarSelecao, btnLimparFiltrosSelecoes;
    @FXML private Button btnFiltrarJogador, btnLimparFiltrosJogadores;

    private ObservableList<Selecao> listaSelecoes = FXCollections.observableArrayList();
    private ObservableList<Jogador> listaJogadores = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar ComboBox de grupo (Seleções)
        comboGrupoFiltro.setItems(FXCollections.observableArrayList("Todos","A","B","C","D","E","F","G","H"));
        comboGrupoFiltro.setValue("Todos");

        // Configurar colunas da tabela Seleções
        colunaPaisConsulta.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaGrupoConsulta.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        colunaTecnicoConsulta.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        tabelaSelecoes.setItems(listaSelecoes);

        // Configurar filtros Jogadores
        comboPosicaoFiltro.setItems(FXCollections.observableArrayList("Todas","Goleiro","Zagueiro","Lateral","Meio-campo","Atacante"));
        comboPosicaoFiltro.setValue("Todas");
        comboStatusFiltro.setItems(FXCollections.observableArrayList("Todos","Ativo","Lesionado","Suspenso"));
        comboStatusFiltro.setValue("Todos");
        // comboSelecaoFiltro será preenchido dinamicamente quando houver dados

        // Configurar colunas da tabela Jogadores
        colunaNomeJogador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicaoJogador.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaNumeroJogador.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colunaIdadeJogador.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colunaStatusJogador.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaSelecaoJogador.setCellValueFactory(new PropertyValueFactory<>("selecao"));
        tabelaJogadores.setItems(listaJogadores);
    }

    @FXML private void filtrarSelecoes() { /* ... */ }
    @FXML private void limparFiltrosSelecoes() { /* ... */ }
    @FXML private void filtrarJogadores() { /* ... */ }
    @FXML private void limparFiltrosJogadores() { /* ... */ }
}