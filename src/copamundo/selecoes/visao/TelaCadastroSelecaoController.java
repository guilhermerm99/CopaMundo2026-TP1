package copamundo.selecoes.visao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaCadastroSelecaoController {

    @FXML private TextField campoPais, campoTecnico;
    @FXML private ComboBox<String> comboGrupo;
    @FXML private TableView<Selecao> tabelaSelecoes; // precisaremos de uma classe modelo
    @FXML private TableColumn<Selecao, String> colunaPais, colunaGrupo, colunaTecnico;
    @FXML private Button btnNovo, btnEditar, btnExcluir, btnSalvar;

    private ObservableList<Selecao> listaSelecoes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        comboGrupo.setItems(FXCollections.observableArrayList("A", "B", "C", "D", "E", "F", "G", "H"));
        colunaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        colunaTecnico.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        tabelaSelecoes.setItems(listaSelecoes);
    }

    @FXML private void novo() { /* limpar campos */ }
    @FXML private void editar() { /* preencher campos a partir da seleção */ }
    @FXML private void excluir() { /* remover da lista */ }
    @FXML private void salvar() { /* validar e adicionar/atualizar */ }

    // Classe interna para representação na tabela (ou usar a do domínio depois)
    public static class Selecao {
        private String pais, grupo, tecnico;
        // getters e setters obrigatórios para PropertyValueFactory
        public String getPais() { return pais; }
        public void setPais(String pais) { this.pais = pais; }
        public String getGrupo() { return grupo; }
        public void setGrupo(String grupo) { this.grupo = grupo; }
        public String getTecnico() { return tecnico; }
        public void setTecnico(String tecnico) { this.tecnico = tecnico; }
    }
}