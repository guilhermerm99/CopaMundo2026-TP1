package copamundo.selecoes.visao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaCadastroJogadorController {

    @FXML private TextField campoNome, campoNumero, campoIdade;
    @FXML private ComboBox<String> comboPosicao, comboStatus, comboSelecao;
    @FXML private TableView<Jogador> tabelaJogadores;
    @FXML private TableColumn<Jogador, String> colunaNome, colunaPosicao, colunaSelecao;
    @FXML private TableColumn<Jogador, Integer> colunaNumero, colunaIdade;
    @FXML private TableColumn<Jogador, String> colunaStatus;
    @FXML private Button btnNovo, btnEditar, btnExcluir, btnSalvar;

    private ObservableList<Jogador> listaJogadores = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        comboPosicao.setItems(FXCollections.observableArrayList(
                "Goleiro", "Zagueiro", "Lateral", "Meio-campo", "Atacante"));
        comboStatus.setItems(FXCollections.observableArrayList(
                "Ativo", "Lesionado", "Suspenso"));
        // comboSelecao será preenchido dinamicamente depois (lógica de negócio)

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colunaIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaSelecao.setCellValueFactory(new PropertyValueFactory<>("selecao"));

        tabelaJogadores.setItems(listaJogadores);
    }

    @FXML private void novo() { /* limpar campos */ }
    @FXML private void editar() { /* preencher campos a partir da seleção na tabela */ }
    @FXML private void excluir() { /* remover da lista */ }
    @FXML private void salvar() { /* validar e adicionar/atualizar */ }

    // Classe interna temporária para a tabela (será substituída pela classe do domínio)
    public static class Jogador {
        private String nome, posicao, status, selecao;
        private int numero, idade;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getPosicao() { return posicao; }
        public void setPosicao(String posicao) { this.posicao = posicao; }
        public int getNumero() { return numero; }
        public void setNumero(int numero) { this.numero = numero; }
        public int getIdade() { return idade; }
        public void setIdade(int idade) { this.idade = idade; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getSelecao() { return selecao; }
        public void setSelecao(String selecao) { this.selecao = selecao; }
    }
}