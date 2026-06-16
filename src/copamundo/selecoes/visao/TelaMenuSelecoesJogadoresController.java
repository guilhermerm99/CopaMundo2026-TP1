package copamundo.selecoes.visao;

import copamundo.comum.Jogador;
import copamundo.comum.Selecao;
import copamundo.comum.StatusJogador;
import copamundo.selecoes.controle.GestaoSelecoesJogadores;
import copamundo.selecoes.excecoes.ElencoCheioException;
import copamundo.selecoes.excecoes.JogadorNaoEncontradoException;
import copamundo.selecoes.excecoes.SelecaoNaoEncontradaException;
import copamundo.comum.ControleAcesso;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.stream.Collectors;

public class TelaMenuSelecoesJogadoresController {

    // Aba Seleções
    @FXML private TextField campoPais, campoTecnico;
    @FXML private ComboBox<String> comboGrupo;
    @FXML private TableView<Selecao> tabelaSelecoes;
    @FXML private TableColumn<Selecao, String> colunaPais, colunaGrupo, colunaTecnico;
    @FXML private Label labelContadorJogadores;
    @FXML private HBox botoesSelecao;

    // Aba Jogadores
    @FXML private TextField campoNome, campoNumero, campoIdade;
    @FXML private ComboBox<String> comboPosicao, comboStatus, comboSelecao;
    @FXML private TableView<Jogador> tabelaJogadores;
    @FXML private TableColumn<Jogador, String> colunaNome, colunaPosicao, colunaSelecao;
    @FXML private TableColumn<Jogador, String> colunaStatusFormatado;
    @FXML private TableColumn<Jogador, Integer> colunaNumero, colunaIdade;
    @FXML private HBox botoesJogador;

    // Aba Consulta
    @FXML private TextField campoBuscaPais, campoBuscaTecnico, campoBuscaNomeJogador;
    @FXML private ComboBox<String> comboGrupoFiltro, comboPosicaoFiltro, comboStatusFiltro, comboSelecaoFiltro;
    @FXML private TableView<Selecao> tabelaConsultaSelecoes;
    @FXML private TableColumn<Selecao, String> colunaPaisConsulta, colunaGrupoConsulta, colunaTecnicoConsulta;
    @FXML private TableView<Jogador> tabelaConsultaJogadores;
    @FXML private TableColumn<Jogador, String> colunaNomeJogador, colunaPosicaoJogador, colunaStatusJogador, colunaSelecaoJogador;
    @FXML private TableColumn<Jogador, Integer> colunaNumeroJogador, colunaIdadeJogador;

    @FXML private TabPane tabPane;

    private final ObservableList<Selecao> listaSelecoes          = FXCollections.observableArrayList();
    private final ObservableList<Jogador> listaJogadores         = FXCollections.observableArrayList();
    private final ObservableList<Selecao> listaConsultaSelecoes  = FXCollections.observableArrayList();
    private final ObservableList<Jogador> listaConsultaJogadores = FXCollections.observableArrayList();

    private final GestaoSelecoesJogadores gestao = new GestaoSelecoesJogadores();
    private String paisEmEdicao      = null;
    private String idJogadorEmEdicao = null;

    @FXML
    public void initialize() {
        configurarAbaSelecoes();
        configurarAbaJogadores();
        configurarAbaConsulta();
        carregarTabelaSelecoes();
        carregarTabelaJogadores();
        carregarComboSelecoes();
        aplicarControlePorPerfil();
    }

    private void aplicarControlePorPerfil() {
        boolean podeGerenciar = ControleAcesso.podeGerenciarSelecoes();
        ControleAcesso.esconderSeNaoPode(podeGerenciar, botoesSelecao, botoesJogador);
    }

    private void configurarAbaSelecoes() {
        comboGrupo.setItems(FXCollections.observableArrayList(
                "A","B","C","D","E","F","G","H","I","J","K","L"));
        colunaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        colunaTecnico.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        tabelaSelecoes.setItems(listaSelecoes);

        // Clicar em uma linha já preenche o formulário
        tabelaSelecoes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, selecionada) -> {
            if (selecionada != null) preencherFormularioSelecao(selecionada);
        });
    }

    private void preencherFormularioSelecao(Selecao selecao) {
        paisEmEdicao = selecao.getPais();
        campoPais.setText(selecao.getPais());
        campoPais.setEditable(false);
        comboGrupo.setValue(selecao.getGrupo());
        campoTecnico.setText(selecao.getTecnico());
        try {
            int total = gestao.contarJogadoresDaSelecao(selecao.getPais());
            if (labelContadorJogadores != null) {
                String aviso = total < GestaoSelecoesJogadores.MIN_JOGADORES
                        ? "  ⚠️ mínimo: " + GestaoSelecoesJogadores.MIN_JOGADORES : "";
                labelContadorJogadores.setText("Jogadores: " + total + " / " + GestaoSelecoesJogadores.MAX_JOGADORES + aviso);
            }
        } catch (Exception e) { /* silencioso */ }
    }

    @FXML private void novoSelecao() {
        paisEmEdicao = null;
        campoPais.clear();
        campoTecnico.clear();
        comboGrupo.getSelectionModel().clearSelection();
        campoPais.setEditable(true);
        tabelaSelecoes.getSelectionModel().clearSelection();
        if (labelContadorJogadores != null) labelContadorJogadores.setText("");
    }

    @FXML private void editarSelecao() {
        Selecao selecao = tabelaSelecoes.getSelectionModel().getSelectedItem();
        if (selecao == null) { alerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma seleção para editar."); return; }
        preencherFormularioSelecao(selecao);
    }

    @FXML private void excluirSelecao() {
        if (ControleAcesso.bloquearAcesso(ControleAcesso.podeGerenciarSelecoes())) {
            alerta(Alert.AlertType.WARNING, "Acesso Negado", "Seu perfil não permite excluir seleções."); return;
        }
        Selecao selecao = tabelaSelecoes.getSelectionModel().getSelectedItem();
        if (selecao == null) { alerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma seleção para excluir."); return; }
        confirmar("Excluir seleção \"" + selecao.getPais() + "\"?", () -> {
            try {
                gestao.excluirSelecao(selecao.getPais());
                carregarTabelaSelecoes();
                carregarComboSelecoes();
                novoSelecao();
                alerta(Alert.AlertType.INFORMATION, "Sucesso", "Seleção excluída.");
            } catch (SelecaoNaoEncontradaException | IllegalStateException e) {
                alerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
            } catch (Exception e) {
                alerta(Alert.AlertType.ERROR, "Erro inesperado", e.getMessage());
            }
        });
    }

    @FXML private void salvarSelecao() {
        if (ControleAcesso.bloquearAcesso(ControleAcesso.podeGerenciarSelecoes())) {
            alerta(Alert.AlertType.WARNING, "Acesso Negado", "Seu perfil não permite salvar seleções."); return;
        }
        String pais    = campoPais.getText().trim();
        String tecnico = campoTecnico.getText().trim();
        String grupo   = comboGrupo.getValue();
        if (pais.isBlank() || tecnico.isBlank() || grupo == null) {
            alerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Preencha todos os campos."); return;
        }
        try {
            if (paisEmEdicao == null) {
                gestao.cadastrarSelecao(new Selecao(pais, grupo, tecnico));
                alerta(Alert.AlertType.INFORMATION, "Sucesso", "Seleção cadastrada!");
            } else {
                gestao.editarSelecao(paisEmEdicao, new Selecao(paisEmEdicao, grupo, tecnico));
                alerta(Alert.AlertType.INFORMATION, "Sucesso", "Seleção atualizada!");
            }
            carregarTabelaSelecoes();
            carregarComboSelecoes();
            novoSelecao();
        } catch (IllegalArgumentException e) {
            alerta(Alert.AlertType.WARNING, "Dado Inválido", e.getMessage());
        } catch (Exception e) {
            alerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    private void configurarAbaJogadores() {
        comboPosicao.setItems(FXCollections.observableArrayList("Goleiro","Zagueiro","Lateral","Meio-campo","Atacante"));
        comboStatus.setItems(FXCollections.observableArrayList("ATIVO","LESIONADO","SUSPENSO"));

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colunaIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colunaStatusFormatado.setCellValueFactory(new PropertyValueFactory<>("statusFormatado"));
        colunaSelecao.setCellValueFactory(new PropertyValueFactory<>("selecao"));
        tabelaJogadores.setItems(listaJogadores);

        // Clicar em uma linha já preenche o formulário
        tabelaJogadores.getSelectionModel().selectedItemProperty().addListener((obs, anterior, jogador) -> {
            if (jogador != null) preencherFormularioJogador(jogador);
        });
    }

    private void preencherFormularioJogador(Jogador jogador) {
        idJogadorEmEdicao = jogador.getId();
        campoNome.setText(jogador.getNome());
        campoNome.setEditable(false);
        campoNumero.setText(String.valueOf(jogador.getNumero()));
        campoIdade.setText(String.valueOf(jogador.getIdade()));
        comboPosicao.setValue(jogador.getPosicao());
        comboStatus.setValue(jogador.getStatus().name());
        comboSelecao.setValue(jogador.getPaisSelecao());
    }

    @FXML private void novoJogador() {
        idJogadorEmEdicao = null;
        campoNome.clear();
        campoNumero.clear();
        campoIdade.clear();
        comboPosicao.getSelectionModel().clearSelection();
        comboStatus.getSelectionModel().clearSelection();
        comboSelecao.getSelectionModel().clearSelection();
        campoNome.setEditable(true);
        tabelaJogadores.getSelectionModel().clearSelection();
    }

    @FXML private void editarJogador() {
        Jogador jogador = tabelaJogadores.getSelectionModel().getSelectedItem();
        if (jogador == null) { alerta(Alert.AlertType.WARNING, "Atenção", "Selecione um jogador para editar."); return; }
        preencherFormularioJogador(jogador);
    }

    @FXML private void excluirJogador() {
        if (ControleAcesso.bloquearAcesso(ControleAcesso.podeGerenciarSelecoes())) {
            alerta(Alert.AlertType.WARNING, "Acesso Negado", "Seu perfil não permite excluir jogadores."); return;
        }
        Jogador jogador = tabelaJogadores.getSelectionModel().getSelectedItem();
        if (jogador == null) { alerta(Alert.AlertType.WARNING, "Atenção", "Selecione um jogador para excluir."); return; }
        confirmar("Excluir jogador \"" + jogador.getNome() + "\"?", () -> {
            try {
                gestao.excluirJogador(jogador.getId());
                carregarTabelaJogadores();
                novoJogador();
                alerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogador excluído.");
            } catch (JogadorNaoEncontradoException | IllegalStateException e) {
                alerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
            } catch (Exception e) {
                alerta(Alert.AlertType.ERROR, "Erro inesperado", e.getMessage());
            }
        });
    }

    @FXML private void salvarJogador() {
        if (ControleAcesso.bloquearAcesso(ControleAcesso.podeGerenciarSelecoes())) {
            alerta(Alert.AlertType.WARNING, "Acesso Negado", "Seu perfil não permite salvar jogadores."); return;
        }
        String nome        = campoNome.getText().trim();
        String posicao     = comboPosicao.getValue();
        String statusStr   = comboStatus.getValue();
        String paisSelecao = comboSelecao.getValue();
        String numeroStr   = campoNumero.getText().trim();
        String idadeStr    = campoIdade.getText().trim();

        if (nome.isBlank() || posicao == null || statusStr == null || paisSelecao == null
                || numeroStr.isBlank() || idadeStr.isBlank()) {
            alerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Preencha todos os campos."); return;
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
                alerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogador cadastrado!");
            } else {
                gestao.editarJogador(idJogadorEmEdicao, jogador);
                alerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogador atualizado!");
            }
            carregarTabelaJogadores();
            novoJogador();
        } catch (NumberFormatException e) {
            alerta(Alert.AlertType.WARNING, "Dado Inválido", "Número e Idade devem ser inteiros.");
        } catch (ElencoCheioException e) {
            alerta(Alert.AlertType.WARNING, "Elenco Cheio", e.getMessage());
        } catch (JogadorNaoEncontradoException e) {
            alerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        } catch (IllegalArgumentException e) {
            alerta(Alert.AlertType.WARNING, "Dado Inválido", e.getMessage());
        } catch (Exception e) {
            alerta(Alert.AlertType.ERROR, "Erro inesperado", e.getMessage());
        }
    }

    private void configurarAbaConsulta() {
        comboGrupoFiltro.setItems(FXCollections.observableArrayList("Todos","A","B","C","D","E","F","G","H","I","J","K","L"));
        comboGrupoFiltro.setValue("Todos");
        comboPosicaoFiltro.setItems(FXCollections.observableArrayList("Todas","Goleiro","Zagueiro","Lateral","Meio-campo","Atacante"));
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
        colunaStatusJogador.setCellValueFactory(new PropertyValueFactory<>("statusFormatado"));
        colunaSelecaoJogador.setCellValueFactory(new PropertyValueFactory<>("selecao"));
        tabelaConsultaJogadores.setItems(listaConsultaJogadores);

        // Atualiza a consulta toda vez que o usuário entra nessa aba
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, anterior, novaAba) -> {
            if (novaAba != null && novaAba.getText().contains("Consulta")) {
                carregarComboSelecaoFiltro();
                filtrarSelecoes();
                filtrarJogadores();
            }
        });
    }

    @FXML private void filtrarSelecoes() {
        try {
            listaConsultaSelecoes.setAll(gestao.buscarSelecoes(
                    campoBuscaPais.getText().trim(),
                    campoBuscaTecnico.getText().trim(),
                    comboGrupoFiltro.getValue()));
        } catch (Exception e) {
            alerta(Alert.AlertType.ERROR, "Erro ao filtrar", e.getMessage());
        }
    }

    @FXML private void limparFiltrosSelecoes() {
        campoBuscaPais.clear();
        campoBuscaTecnico.clear();
        comboGrupoFiltro.setValue("Todos");
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
            alerta(Alert.AlertType.ERROR, "Erro ao filtrar", e.getMessage());
        }
    }

    @FXML private void limparFiltrosJogadores() {
        campoBuscaNomeJogador.clear();
        comboPosicaoFiltro.setValue("Todas");
        comboStatusFiltro.setValue("Todos");
        comboSelecaoFiltro.getSelectionModel().clearSelection();
        filtrarJogadores();
    }

    private void carregarTabelaSelecoes() {
        try { listaSelecoes.setAll(gestao.listarSelecoes()); }
        catch (Exception e) { alerta(Alert.AlertType.ERROR, "Erro", e.getMessage()); }
    }

    private void carregarTabelaJogadores() {
        try { listaJogadores.setAll(gestao.listarTodosJogadores()); }
        catch (Exception e) { alerta(Alert.AlertType.ERROR, "Erro", e.getMessage()); }
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
        Alert caixa = new Alert(Alert.AlertType.CONFIRMATION);
        caixa.setTitle("Confirmação");
        caixa.setHeaderText(null);
        caixa.setContentText(mensagem);
        caixa.showAndWait().ifPresent(resposta -> { if (resposta == ButtonType.OK) acao.run(); });
    }

    private void alerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert caixa = new Alert(tipo);
        caixa.setTitle(titulo);
        caixa.setHeaderText(null);
        caixa.setContentText(mensagem);
        caixa.showAndWait();
    }
}
