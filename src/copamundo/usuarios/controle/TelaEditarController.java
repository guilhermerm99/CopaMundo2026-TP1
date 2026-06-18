package copamundo.usuarios.controle;

import copamundo.principal.visao.TelaPrincipalController;
import copamundo.usuarios.excecoes.PersistenciaException;
import copamundo.usuarios.persistencia.PersistenciaUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class TelaEditarController {

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoCpf;

    @FXML
    private TextField campoEmail;

    @FXML
    private ComboBox<String> comboStatus;

    @FXML
    private ComboBox<String> comboPais;

    @FXML
    private ComboBox<String> comboFuncao;

    @FXML
    private TextField campoNovaSenha;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    private Usuario usuarioSelecionado;
    private String emailOriginal;

    @FXML
    public void initialize() {
        comboStatus.getItems().setAll("ATIVO", "INATIVO");

        comboFuncao.getItems().setAll(
                Arrays.stream(Usuario.Funcao.values())
                        .map(Enum::name)
                        .toList()
        );

        comboPais.getItems().setAll(
                Arrays.stream(Usuario.Pais.values())
                        .map(Enum::name)
                        .toList()
        );
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioSelecionado = usuario;

        if (usuario != null) {
            this.emailOriginal = usuario.getEmailUsuario();

            campoNome.setText(usuario.getNomeUsuario());
            campoCpf.setText(usuario.getCpf());
            campoEmail.setText(usuario.getEmailUsuario());

            comboStatus.setValue(usuario.isAtivo() ? "ATIVO" : "INATIVO");

            comboPais.setValue(usuario.getPais() != null ? usuario.getPais().name() : null);
            comboFuncao.setValue(usuario.getFuncao() != null ? usuario.getFuncao().name() : null);

            campoNovaSenha.setText("");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void salvar(ActionEvent event) {
        if (usuarioSelecionado == null) {
            return;
        }

        String novoCpf = campoCpf.getText();
        String novoEmail = campoEmail.getText();

        PersistenciaUsuarios persistencia = new PersistenciaUsuarios();

        try {
            if (persistencia.existeCpfOuEmailEmOutroUsuario(emailOriginal, novoCpf, novoEmail)) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro ao editar usuário");
                alerta.setHeaderText(null);
                alerta.setContentText("CPF ou e-mail já cadastrado para outro usuário.");
                alerta.showAndWait();
                return;
            }
        } catch (PersistenciaException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText(null);
            alerta.setContentText("Erro ao verificar usuários cadastrados.");
            alerta.showAndWait();
            return;
        }

        usuarioSelecionado.setNomeUsuario(campoNome.getText());
        usuarioSelecionado.setCpf(novoCpf);
        usuarioSelecionado.setEmailUsuario(novoEmail);

        String status = comboStatus.getValue();
        if ("ATIVO".equalsIgnoreCase(status)) {
            usuarioSelecionado.setStatus(Usuario.Status.ATIVO);
        } else {
            usuarioSelecionado.setStatus(Usuario.Status.INATIVO);
        }

        String funcao = comboFuncao.getValue();
        if (funcao != null && !funcao.isBlank()) {
            usuarioSelecionado.setFuncao(Usuario.Funcao.valueOf(funcao));
        }

        String pais = comboPais.getValue();
        if (pais != null && !pais.isBlank()) {
            usuarioSelecionado.setPais(Usuario.Pais.valueOf(pais));
        }

        String novaSenha = campoNovaSenha.getText();

        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            usuarioSelecionado.setSenhaUsuario(novaSenha);
        }

        try {
            persistencia.atualizarUsuario(emailOriginal, usuarioSelecionado);
        } catch (PersistenciaException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro ao salvar");
            alerta.setHeaderText(null);
            alerta.setContentText("Erro ao salvar usuário no arquivo.");
            alerta.showAndWait();
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}