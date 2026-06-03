package copamundo.selecoes.visao;

import copamundo.comum.Jogador;
import copamundo.comum.Selecao;
import copamundo.comum.StatusJogador;
import copamundo.selecoes.controle.GestaoSelecoesJogadores;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

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
    @FXML private ComboBox<String> comboSelecaoFiltro;

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

    private final ObservableList<Selecao> listaSelecoes = FXCollections.observableArrayList();
    private final ObservableList<Jogador> listaJogadores = FXCollections.observableArrayList();
    private final GestaoSelecoesJogadores gestao = new GestaoSelecoesJogadores();

    @FXML
    public void initialize() {
        // Filtros de Seleções
        comboGrupoFiltro.setItems(FXCollections.observableArrayList(
                "Todos", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"));
        comboGrupoFiltro.setValue("Todos");

        colunaPaisConsulta.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaGrupoConsulta.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        colunaTecnicoConsulta.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        tabelaSelecoes.setItems(listaSelecoes);

        // Filtros de Jogadores
        comboPosicaoFiltro.setItems(FXCollections.observableArrayList(
                "Todas", "Goleiro", "Zagueiro", "Lateral", "Meio-campo", "Atacante"));
        comboPosicaoFiltro.setValue("Todas");

        comboStatusFiltro.setItems(FXCollections.observableArrayList(
                "Todos", "ATIVO", "LESIONADO", "SUSPENSO"));
        comboStatusFiltro.setValue("Todos");

        colunaNomeJogador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicaoJogador.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaNumeroJogador.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colunaIdadeJogador.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colunaStatusJogador.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaSelecaoJogador.setCellValueFactory(new PropertyValueFactory<>("selecao"));
        tabelaJogadores.setItems(listaJogadores);

        carregarComboSelecaoFiltro();
        carregarTodasSelecoes();
        carregarTodosJogadores();
    }

    @FXML
    private void filtrarSelecoes() {
        try {
            String pais = campoBuscaPais.getText().trim();
            String tecnico = campoBuscaTecnico.getText().trim();
            String grupo = comboGrupoFiltro.getValue();

            List<Selecao> resultado;

            // Aplica filtros em cascata
            if (!pais.isBlank()) {
                resultado = gestao.buscarSelecoesPorPais(pais);
            } else if (!tecnico.isBlank()) {
                resultado = gestao.buscarSelecoesPorTecnico(tecnico);
            } else {
                resultado = gestao.buscarSelecoesPorGrupo(grupo);
            }

            listaSelecoes.setAll(resultado);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao filtrar", e.getMessage());
        }
    }

    @FXML
    private void limparFiltrosSelecoes() {
        campoBuscaPais.clear();
        campoBuscaTecnico.clear();
        comboGrupoFiltro.setValue("Todos");
        carregarTodasSelecoes();
    }

    @FXML
    private void filtrarJogadores() {
        try {
            String nome = campoBuscaNomeJogador.getText().trim();
            String posicao = comboPosicaoFiltro.getValue();
            String statusStr = comboStatusFiltro.getValue();
            String paisSelecao = comboSelecaoFiltro.getValue();

            List<Jogador> resultado = gestao.listarTodosJogadores();

            if (!nome.isBlank()) {
                resultado = gestao.buscarJogadoresPorNome(nome);
            }
            if (posicao != null && !posicao.equalsIgnoreCase("Todas")) {
                final String pos = posicao;
                resultado = resultado.stream()
                        .filter(j -> j.getPosicao().equalsIgnoreCase(pos))
                        .collect(java.util.stream.Collectors.toList());
            }
            if (statusStr != null && !statusStr.equalsIgnoreCase("Todos")) {
                StatusJogador status = StatusJogador.valueOf(statusStr);
                resultado = resultado.stream()
                        .filter(j -> j.getStatus() == status)
                        .collect(java.util.stream.Collectors.toList());
            }
            if (paisSelecao != null && !paisSelecao.isBlank()) {
                final String pais = paisSelecao;
                resultado = resultado.stream()
                        .filter(j -> j.getPaisSelecao().equalsIgnoreCase(pais))
                        .collect(java.util.stream.Collectors.toList());
            }

            listaJogadores.setAll(resultado);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao filtrar", e.getMessage());
        }
    }

    @FXML
    private void limparFiltrosJogadores() {
        campoBuscaNomeJogador.clear();
        comboPosicaoFiltro.setValue("Todas");
        comboStatusFiltro.setValue("Todos");
        comboSelecaoFiltro.getSelectionModel().clearSelection();
        carregarTodosJogadores();
    }

    private void carregarTodasSelecoes() {
        try {
            listaSelecoes.setAll(gestao.listarSelecoes());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar as seleções.");
        }
    }

    private void carregarTodosJogadores() {
        try {
            listaJogadores.setAll(gestao.listarTodosJogadores());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar os jogadores.");
        }
    }

    private void carregarComboSelecaoFiltro() {
        try {
            ObservableList<String> paises = FXCollections.observableArrayList();
            paises.add(""); // opção em branco = todos
            gestao.listarSelecoes().forEach(s -> paises.add(s.getPais()));
            comboSelecaoFiltro.setItems(paises);
        } catch (Exception e) {
            // Silencioso — não crítico
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
