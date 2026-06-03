package copamundo.selecoes.visao;

import copamundo.comum.Jogador;
import copamundo.comum.Selecao;
import copamundo.comum.StatusJogador;
import copamundo.selecoes.controle.GestaoSelecoesJogadores;
import copamundo.selecoes.excecoes.ElencoCheioException;
import copamundo.selecoes.excecoes.JogadorNaoEncontradoException;
import copamundo.selecoes.excecoes.SelecaoNaoEncontradaException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TelaMenuSelecoesJogadoresController {

    // ===== ABA SELEÇÕES =====
    @FXML private TextField campoPais, campoTecnico;
    @FXML private ComboBox<String> comboGrupo;
    @FXML private TableView<Selecao> tabelaSelecoes;
    @FXML private TableColumn<Selecao, String> colunaPais, colunaGrupo, colunaTecnico;
    @FXML private Label labelContadorJogadores; // melhoria 10

    // ===== ABA JOGADORES =====
    @FXML private TextField campoNome, campoNumero, campoIdade;
    @FXML private ComboBox<String> comboPosicao, comboStatus, comboSelecao;
    @FXML private TableView<Jogador> tabelaJogadores;
    @FXML private TableColumn<Jogador, String> colunaNome, colunaPosicao, colunaSelecao;
    @FXML private TableColumn<Jogador, String> colunaStatusFormatado; // melhoria 9
    @FXML private TableColumn<Jogador, Integer> colunaNumero, colunaIdade;

    // ===== ABA CONSULTA =====
    @FXML private TextField campoBuscaPais, campoBuscaTecnico, campoBuscaNomeJogador;
    @FXML private ComboBox<String> comboGrupoFiltro, comboPosicaoFiltro, comboStatusFiltro, comboSelecaoFiltro;
    @FXML private TableView<Selecao> tabelaConsultaSelecoes;
    @FXML private TableColumn<Selecao, String> colunaPaisConsulta, colunaGrupoConsulta, colunaTecnicoConsulta;
    @FXML private TableView<Jogador> tabelaConsultaJogadores;
    @FXML private TableColumn<Jogador, String> colunaNomeJogador, colunaPosicaoJogador, colunaStatusJogador, colunaSelecaoJogador;
    @FXML private TableColumn<Jogador, Integer> colunaNumeroJogador, colunaIdadeJogador;

    @FXML private TabPane tabPane;

    private final ObservableList<Selecao> listaSelecoes   = FXCollections.observableArrayList();
    private final ObservableList<Jogador> listaJogadores  = FXCollections.observableArrayList();
    private final ObservableList<Selecao> listaConsultaSelecoes  = FXCollections.observableArrayList();
    private final ObservableList<Jogador> listaConsultaJogadores = FXCollections.observableArrayList();

    private final GestaoSelecoesJogadores gestao = new GestaoSelecoesJogadores();
    private String paisEmEdicao  = null;
    private String idJogadorEmEdicao = null; // melhoria 1: guarda o ID composto

    @FXML
    public void initialize() {
        configurarAbaSelecoes();
        configurarAbaJogadores();
        configurarAbaConsulta();
        carregarTabelaSelecoes();
        carregarTabelaJogadores();
        carregarComboSelecoes();
    }

    // ==================== ABA SELEÇÕES ====================

    private void configurarAbaSelecoes() {
        comboGrupo.setItems(FXCollections.observableArrayList(
                "A","B","C","D","E","F","G","H","I","J","K","L"));
        colunaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        colunaTecnico.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        tabelaSelecoes.setItems(listaSelecoes);

        // Melhoria 8: clicar na linha preenche o formulário automaticamente
        tabelaSelecoes.getSelectionModel().selectedItemProperty().addListener((obs, old, selecionada) -> {
            if (selecionada != null) preencherFormularioSelecao(selecionada);
        });
    }

    /** Melhoria 10: atualiza contador de jogadores ao selecionar uma seleção */
    private void preencherFormularioSelecao(Selecao s) {
        paisEmEdicao = s.getPais();
        campoPais.setText(s.getPais());
        campoPais.setEditable(false);
        comboGrupo.setValue(s.getGrupo());
        campoTecnico.setText(s.getTecnico());
        try {
            int total = gestao.contarJogadoresDaSelecao(s.getPais());
            if (labelContadorJogadores != null) {
                labelContadorJogadores.setText("Jogadores: " + total + " / " + GestaoSelecoesJogadores.MAX_JOGADORES
                        + (total < GestaoSelecoesJogadores.MIN_JOGADORES
                                ? "  ⚠️ mínimo: " + GestaoSelecoesJogadores.MIN_JOGADORES : ""));
            }
        } catch (Exception e) { /* silencioso */ }
    }

    @FXML private void novoSelecao() {
        paisEmEdicao = null;
        campoPais.clear(); campoTecnico.clear();
        comboGrupo.getSelectionModel().clearSelection();
        campoPais.setEditable(true);
        tabelaSelecoes.getSelectionModel().clearSelection();
        if (labelContadorJogadores != null) labelContadorJogadores.setText("");
    }

    @FXML private void editarSelecao() {
        Selecao s = tabelaSelecoes.getSelectionModel().getSelectedItem();
        if (s == null) { mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma seleção para editar."); return; }
        preencherFormularioSelecao(s);
    }

    @FXML private void excluirSelecao() {
        Selecao s = tabelaSelecoes.getSelectionModel().getSelectedItem();
        if (s == null) { mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma seleção para excluir."); return; }
        confirmar("Excluir seleção \"" + s.getPais() + "\"?", () -> {
            try {
                gestao.excluirSelecao(s.getPais());
                carregarTabelaSelecoes(); carregarComboSelecoes(); novoSelecao();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Seleção excluída.");
            } catch (SelecaoNaoEncontradaException | IllegalStateException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro inesperado", e.getMessage());
            }
        });
    }

    @FXML private void salvarSelecao() {
        String pais   = campoPais.getText().trim();
        String tecnico = campoTecnico.getText().trim();
        String grupo  = comboGrupo.getValue();
        if (pais.isBlank() || tecnico.isBlank() || grupo == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Preencha todos os campos."); return;
        }
        try {
            if (paisEmEdicao == null) {
                gestao.cadastrarSelecao(new Selecao(pais, grupo, tecnico));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Seleção cadastrada!");
            } else {
                gestao.editarSelecao(paisEmEdicao, new Selecao(paisEmEdicao, grupo, tecnico));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Seleção atualizada!");
            }
            carregarTabelaSelecoes(); carregarComboSelecoes(); novoSelecao();
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dado Inválido", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    // ==================== ABA JOGADORES ====================

    private void configurarAbaJogadores() {
        comboPosicao.setItems(FXCollections.observableArrayList(
                "Goleiro","Zagueiro","Lateral","Meio-campo","Atacante"));
        // Melhoria 9: exibe status legível
        comboStatus.setItems(FXCollections.observableArrayList("ATIVO","LESIONADO","SUSPENSO"));

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colunaIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        // Melhoria 9: usa getStatusFormatado() no lugar de getStatus()
        colunaStatusFormatado.setCellValueFactory(new PropertyValueFactory<>("statusFormatado"));
        colunaSelecao.setCellValueFactory(new PropertyValueFactory<>("selecao"));
        tabelaJogadores.setItems(listaJogadores);

        // Melhoria 8: clicar na linha preenche o formulário
        tabelaJogadores.getSelectionModel().selectedItemProperty().addListener((obs, old, j) -> {
            if (j != null) preencherFormularioJogador(j);
        });
    }

    private void preencherFormularioJogador(Jogador j) {
        idJogadorEmEdicao = j.getId();
        campoNome.setText(j.getNome());
        campoNome.setEditable(false);
        campoNumero.setText(String.valueOf(j.getNumero()));
        campoIdade.setText(String.valueOf(j.getIdade()));
        comboPosicao.setValue(j.getPosicao());
        comboStatus.setValue(j.getStatus().name());
        comboSelecao.setValue(j.getPaisSelecao());
    }

    @FXML private void novoJogador() {
        idJogadorEmEdicao = null;
        campoNome.clear(); campoNumero.clear(); campoIdade.clear();
        comboPosicao.getSelectionModel().clearSelection();
        comboStatus.getSelectionModel().clearSelection();
        comboSelecao.getSelectionModel().clearSelection();
        campoNome.setEditable(true);
        tabelaJogadores.getSelectionModel().clearSelection();
    }

    @FXML private void editarJogador() {
        Jogador j = tabelaJogadores.getSelectionModel().getSelectedItem();
        if (j == null) { mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um jogador para editar."); return; }
        preencherFormularioJogador(j);
    }

    @FXML private void excluirJogador() {
        Jogador j = tabelaJogadores.getSelectionModel().getSelectedItem();
        if (j == null) { mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um jogador para excluir."); return; }
        confirmar("Excluir jogador \"" + j.getNome() + "\"?", () -> {
            try {
                gestao.excluirJogador(j.getId());
                carregarTabelaJogadores(); novoJogador();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogador excluído.");
            } catch (JogadorNaoEncontradoException | IllegalStateException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro inesperado", e.getMessage());
            }
        });
    }

    @FXML private void salvarJogador() {
        String nome       = campoNome.getText().trim();
        String posicao    = comboPosicao.getValue();
        String statusStr  = comboStatus.getValue();
        String paisSelecao = comboSelecao.getValue();
        String numeroStr  = campoNumero.getText().trim();
        String idadeStr   = campoIdade.getText().trim();

        if (nome.isBlank() || posicao == null || statusStr == null || paisSelecao == null
                || numeroStr.isBlank() || idadeStr.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Preencha todos os campos."); return;
        }
        try {
            Jogador jogador = new Jogador();
            jogador.setNome(nome);
            jogador.setPosicao(posicao);
            jogador.setNumero(Integer.parseInt(numeroStr));
            jogador.setIdade(Integer.parseInt(idadeStr));
            jogador.setStatus(StatusJogador.valueOf(statusStr));
            jogador.setPaisSelecao(paisSelecao);

            if (idJogadorEmEdicao == null) {
                gestao.cadastrarJogador(jogador);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogador cadastrado!");
            } else {
                gestao.editarJogador(idJogadorEmEdicao, jogador);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogador atualizado!");
            }
            carregarTabelaJogadores(); novoJogador();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dado Inválido", "Número e Idade devem ser inteiros.");
        } catch (ElencoCheioException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Elenco Cheio", e.getMessage());
        } catch (JogadorNaoEncontradoException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dado Inválido", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro inesperado", e.getMessage());
        }
    }

    // ==================== ABA CONSULTA ====================

    private void configurarAbaConsulta() {
        comboGrupoFiltro.setItems(FXCollections.observableArrayList(
                "Todos","A","B","C","D","E","F","G","H","I","J","K","L"));
        comboGrupoFiltro.setValue("Todos");
        comboPosicaoFiltro.setItems(FXCollections.observableArrayList(
                "Todas","Goleiro","Zagueiro","Lateral","Meio-campo","Atacante"));
        comboPosicaoFiltro.setValue("Todas");
        comboStatusFiltro.setItems(FXCollections.observableArrayList("Todos","ATIVO","LESIONADO","SUSPENSO"));
        comboStatusFiltro.setValue("Todos");

        colunaPaisConsulta.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaGrupoConsulta.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        colunaTecnicoConsulta.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        tabelaConsultaSelecoes.setItems(listaConsultaSelecoes);

        colunaNomeJogador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicaoJogador.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaNumeroJogador.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colunaIdadeJogador.setCellValueFactory(new PropertyValueFactory<>("idade"));
        // Melhoria 9: status formatado também na consulta
        colunaStatusJogador.setCellValueFactory(new PropertyValueFactory<>("statusFormatado"));
        colunaSelecaoJogador.setCellValueFactory(new PropertyValueFactory<>("selecao"));
        tabelaConsultaJogadores.setItems(listaConsultaJogadores);

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, old, newTab) -> {
            if (newTab != null && newTab.getText().contains("Consulta")) {
                carregarComboSelecaoFiltro();
                filtrarSelecoes();
                filtrarJogadores();
            }
        });
    }

    // Melhoria 7: filtros combinados
    @FXML private void filtrarSelecoes() {
        try {
            listaConsultaSelecoes.setAll(gestao.buscarSelecoes(
                    campoBuscaPais.getText().trim(),
                    campoBuscaTecnico.getText().trim(),
                    comboGrupoFiltro.getValue()));
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao filtrar", e.getMessage());
        }
    }

    @FXML private void limparFiltrosSelecoes() {
        campoBuscaPais.clear(); campoBuscaTecnico.clear(); comboGrupoFiltro.setValue("Todos");
        filtrarSelecoes();
    }

    @FXML private void filtrarJogadores() {
        try {
            String statusStr = comboStatusFiltro.getValue();
            StatusJogador status = (statusStr == null || statusStr.equalsIgnoreCase("Todos"))
                    ? null : StatusJogador.valueOf(statusStr);
            listaConsultaJogadores.setAll(gestao.buscarJogadores(
                    campoBuscaNomeJogador.getText().trim(),
                    comboPosicaoFiltro.getValue(),
                    status,
                    comboSelecaoFiltro.getValue()));
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao filtrar", e.getMessage());
        }
    }

    @FXML private void limparFiltrosJogadores() {
        campoBuscaNomeJogador.clear(); comboPosicaoFiltro.setValue("Todas");
        comboStatusFiltro.setValue("Todos"); comboSelecaoFiltro.getSelectionModel().clearSelection();
        filtrarJogadores();
    }

    // ==================== HELPERS ====================

    private void carregarTabelaSelecoes() {
        try { listaSelecoes.setAll(gestao.listarSelecoes()); }
        catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage()); }
    }

    private void carregarTabelaJogadores() {
        try { listaJogadores.setAll(gestao.listarTodosJogadores()); }
        catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage()); }
    }

    private void carregarComboSelecoes() {
        try {
            List<String> paises = gestao.listarSelecoes().stream()
                    .map(Selecao::getPais).collect(Collectors.toList());
            comboSelecao.setItems(FXCollections.observableArrayList(paises));
        } catch (Exception e) { /* silencioso */ }
    }

    private void carregarComboSelecaoFiltro() {
        try {
            List<String> paises = gestao.listarSelecoes().stream()
                    .map(Selecao::getPais).collect(Collectors.toList());
            paises.add(0, "");
            comboSelecaoFiltro.setItems(FXCollections.observableArrayList(paises));
        } catch (Exception e) { /* silencioso */ }
    }

    private void confirmar(String mensagem, Runnable acao) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação"); alert.setHeaderText(null); alert.setContentText(mensagem);
        alert.showAndWait().ifPresent(r -> { if (r == ButtonType.OK) acao.run(); });
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo); alert.setHeaderText(null); alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
