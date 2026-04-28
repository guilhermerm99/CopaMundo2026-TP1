package copamundo.selecoes.visao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaCadastroSelecaoController {

    @FXML private TextField campoPais, campoTecnico;
    @FXML private ComboBox<String> comboGrupo;
    @FXML private TableView<Selecao> tabelaSelecoes;
    @FXML private TableColumn<Selecao, String> colunaPais, colunaGrupo, colunaTecnico;
    @FXML private Button btnNovo, btnEditar, btnExcluir, btnSalvar;

    private ObservableList<Selecao> listaSelecoes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Atualizado para os 12 grupos da Copa 2026 (A até L)
        comboGrupo.setItems(FXCollections.observableArrayList(
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
        ));

        colunaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        colunaTecnico.setCellValueFactory(new PropertyValueFactory<>("tecnico"));

        tabelaSelecoes.setItems(listaSelecoes);
    }

    @FXML
    private void novo() {
        campoPais.clear();
        campoTecnico.clear();
        comboGrupo.getSelectionModel().clearSelection();
    }

    @FXML
    private void editar() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Protótipo", "A funcionalidade de edição (preencher os campos a partir da tabela) será implementada na Etapa 3.");
    }

    @FXML
    private void excluir() {
        mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "O alerta de confirmação antes da exclusão definitiva será implementado na Etapa 3.");
    }

    @FXML
    private void salvar() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Protótipo", "A validação de regras de negócio e a persistência em arquivo serão implementadas na Etapa 3.");
    }

    // Método auxiliar do Mentor para não repetir código de alerta
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

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