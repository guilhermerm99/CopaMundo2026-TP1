package copamundo.usuarios.controle;

import copamundo.comum.PersistenciaException;
import copamundo.comum.PersistenciaUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TextField campoSenhaAnterior;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    private Usuario usuarioSelecionado;

    @FXML
    public void initialize() {
        comboStatus.getItems().addAll("ATIVO", "INATIVO");
        comboFuncao.getItems().addAll("ADMINISTRADOR", "ORGANIZADOR");
        comboPais.getItems().addAll("AFRICADOSUL", "AUSTRALIA", "BELGICA", "BRASIL");
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioSelecionado = usuario;

        if (usuario != null) {
            campoNome.setText(usuario.getNomeUsuario());
            campoCpf.setText(usuario.getCpf());
            campoEmail.setText(usuario.getEmailUsuario());
            comboStatus.setValue(usuario.isAtivo() ? "ATIVO" : "INATIVO");
            comboPais.setValue(usuario.getPais() != null ? usuario.getPais().toString() : "");
            comboFuncao.setValue(usuario.getFuncao() != null ? usuario.getFuncao().toString() : "");
            campoNovaSenha.setText("");
            campoSenhaAnterior.setText("");
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

        usuarioSelecionado.setNomeUsuario(campoNome.getText());
        usuarioSelecionado.setCpf(campoCpf.getText());
        usuarioSelecionado.setEmailUsuario(campoEmail.getText());

        if ("ATIVO".equalsIgnoreCase(comboStatus.getValue())) {
            usuarioSelecionado.setStatus(Usuario.Status.ATIVO);
        } else {
            usuarioSelecionado.setStatus(Usuario.Status.INATIVO);
        }

        if (comboFuncao.getValue() != null) {
            usuarioSelecionado.setFuncao(Usuario.Funcao.valueOf(comboFuncao.getValue()));
        }

        if (comboPais.getValue() != null) {
            usuarioSelecionado.setPais(Usuario.Pais.valueOf(comboPais.getValue()));
        }

        String novaSenha = campoNovaSenha.getText();
        String senhaAnterior = campoSenhaAnterior.getText();
        if (novaSenha != null && !novaSenha.trim().isEmpty() && usuarioSelecionado.validacaoSenhaAnterior(senhaAnterior)) {
            usuarioSelecionado.setSenhaUsuario(novaSenha);
        }

        try {
            PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
            persistencia.salvarUsuario(usuarioSelecionado);
        } catch (PersistenciaException e) {
            System.out.println("erro ao salvar usuários no arquivo.: " + e.getMessage());
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}