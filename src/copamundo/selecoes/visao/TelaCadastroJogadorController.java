package copamundo.selecoes.visao;

import copamundo.comum.Jogador;
import copamundo.comum.Selecao;
import copamundo.comum.StatusJogador;
import copamundo.selecoes.controle.GestaoSelecoesJogadores;
import copamundo.selecoes.excecoes.ElencoCheioException;
import copamundo.selecoes.excecoes.JogadorNaoEncontradoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TelaCadastroJogadorController {

    @FXML private TextField campoNome, campoNumero, campoIdade;
    @FXML private ComboBox<String> comboPosicao, comboStatus, comboSelecao;
    @FXML private TableView<Jogador> tabelaJogadores;
    @FXML private TableColumn<Jogador, String> colunaNome, colunaPosicao, colunaSelecao, colunaStatus;
    @FXML private TableColumn<Jogador, Integer> colunaNumero, colunaIdade;
    @FXML private Button btnNovo, btnEditar, btnExcluir, btnSalvar;

    private final ObservableList<Jogador> listaJogadores = FXCollections.observableArrayList();
    private final GestaoSelecoesJogadores gestao = new GestaoSelecoesJogadores();

    // Guarda o nome original ao editar (identificador)
    private String nomeEmEdicao = null;

    @FXML
    public void initialize() {
        comboPosicao.setItems(FXCollections.observableArrayList(
                "Goleiro", "Zagueiro", "Lateral", "Meio-campo", "Atacante"));
        comboStatus.setItems(FXCollections.observableArrayList(
                "ATIVO", "LESIONADO", "SUSPENSO"));

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colunaIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaSelecao.setCellValueFactory(new PropertyValueFactory<>("selecao"));

        tabelaJogadores.setItems(listaJogadores);

        carregarComboSelecoes();
        carregarTabela();
    }

    @FXML
    private void novo() {
        nomeEmEdicao = null;
        campoNome.clear();
        campoNumero.clear();
        campoIdade.clear();
        comboPosicao.getSelectionModel().clearSelection();
        comboStatus.getSelectionModel().clearSelection();
        comboSelecao.getSelectionModel().clearSelection();
        campoNome.setEditable(true);
        tabelaJogadores.getSelectionModel().clearSelection();
    }

    @FXML
    private void editar() {
        Jogador selecionado = tabelaJogadores.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um jogador na tabela para editar.");
            return;
        }
        nomeEmEdicao = selecionado.getNome();
        campoNome.setText(selecionado.getNome());
        campoNome.setEditable(false); // Nome é o identificador
        campoNumero.setText(String.valueOf(selecionado.getNumero()));
        campoIdade.setText(String.valueOf(selecionado.getIdade()));
        comboPosicao.setValue(selecionado.getPosicao());
        comboStatus.setValue(selecionado.getStatus().name());
        comboSelecao.setValue(selecionado.getPaisSelecao());
    }

    @FXML
    private void salvar() {
        String nome = campoNome.getText().trim();
        String posicao = comboPosicao.getValue();
        String statusStr = comboStatus.getValue();
        String paisSelecao = comboSelecao.getValue();
        String numeroStr = campoNumero.getText().trim();
        String idadeStr = campoIdade.getText().trim();

        if (nome.isBlank() || posicao == null || statusStr == null || paisSelecao == null
                || numeroStr.isBlank() || idadeStr.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Preencha todos os campos antes de salvar.");
            return;
        }

        try {
            int numero = Integer.parseInt(numeroStr);
            int idade = Integer.parseInt(idadeStr);
            StatusJogador status = StatusJogador.valueOf(statusStr);

            Jogador jogador = new Jogador();
            jogador.setNome(nome);
            jogador.setPosicao(posicao);
            jogador.setNumero(numero);
            jogador.setIdade(idade);
            jogador.setStatus(status);
            jogador.setPaisSelecao(paisSelecao);

            if (nomeEmEdicao == null) {
                gestao.cadastrarJogador(jogador);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogador cadastrado com sucesso!");
            } else {
                gestao.editarJogador(nomeEmEdicao, jogador);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogador atualizado com sucesso!");
            }
            carregarTabela();
            novo();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dado Inválido", "Número e Idade devem ser valores numéricos inteiros.");
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

    @FXML
    private void excluir() {
        Jogador selecionado = tabelaJogadores.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um jogador na tabela para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Tem certeza que deseja excluir o jogador \"" + selecionado.getNome() + "\"?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                try {
                    gestao.excluirJogador(selecionado.getNome());
                    carregarTabela();
                    novo();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogador excluído com sucesso.");
                } catch (JogadorNaoEncontradoException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro inesperado", e.getMessage());
                }
            }
        });
    }

    private void carregarComboSelecoes() {
        try {
            List<Selecao> selecoes = gestao.listarSelecoes();
            ObservableList<String> paises = FXCollections.observableArrayList();
            selecoes.forEach(s -> paises.add(s.getPais()));
            comboSelecao.setItems(paises);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar as seleções: " + e.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            listaJogadores.setAll(gestao.listarTodosJogadores());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao carregar", "Não foi possível carregar os jogadores: " + e.getMessage());
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
