package copamundo.selecoes.visao;

import copamundo.comum.Selecao;
import copamundo.selecoes.controle.GestaoSelecoesJogadores;
import copamundo.selecoes.excecoes.SelecaoNaoEncontradaException;
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

    private final ObservableList<Selecao> listaSelecoes = FXCollections.observableArrayList();
    private final GestaoSelecoesJogadores gestao = new GestaoSelecoesJogadores();

    // Guarda o país original ao editar (identificador imutável)
    private String paisEmEdicao = null;

    @FXML
    public void initialize() {
        comboGrupo.setItems(FXCollections.observableArrayList(
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"));

        colunaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        colunaTecnico.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        tabelaSelecoes.setItems(listaSelecoes);

        carregarTabela();
    }

    @FXML
    private void novo() {
        paisEmEdicao = null;
        campoPais.clear();
        campoTecnico.clear();
        comboGrupo.getSelectionModel().clearSelection();
        campoPais.setEditable(true);
        tabelaSelecoes.getSelectionModel().clearSelection();
    }

    @FXML
    private void editar() {
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma seleção na tabela para editar.");
            return;
        }
        paisEmEdicao = selecionada.getPais();
        campoPais.setText(selecionada.getPais());
        campoPais.setEditable(false); // País é o identificador, não pode mudar
        comboGrupo.setValue(selecionada.getGrupo());
        campoTecnico.setText(selecionada.getTecnico());
    }

    @FXML
    private void excluir() {
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma seleção na tabela para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Tem certeza que deseja excluir a seleção \"" + selecionada.getPais() + "\"?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                try {
                    gestao.excluirSelecao(selecionada.getPais());
                    carregarTabela();
                    novo();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Seleção excluída com sucesso.");
                } catch (SelecaoNaoEncontradaException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
                } catch (IllegalStateException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Não permitido", e.getMessage());
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro inesperado", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void salvar() {
        String pais = campoPais.getText().trim();
        String tecnico = campoTecnico.getText().trim();
        String grupo = comboGrupo.getValue();

        if (pais.isBlank() || tecnico.isBlank() || grupo == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Preencha todos os campos antes de salvar.");
            return;
        }

        try {
            if (paisEmEdicao == null) {
                // Cadastro novo
                Selecao nova = new Selecao(pais, grupo, tecnico);
                gestao.cadastrarSelecao(nova);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Seleção cadastrada com sucesso!");
            } else {
                // Edição
                Selecao novosDados = new Selecao(paisEmEdicao, grupo, tecnico);
                gestao.editarSelecao(paisEmEdicao, novosDados);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Seleção atualizada com sucesso!");
            }
            carregarTabela();
            novo();
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dado Inválido", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            listaSelecoes.setAll(gestao.listarSelecoes());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao carregar", "Não foi possível carregar as seleções: " + e.getMessage());
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
