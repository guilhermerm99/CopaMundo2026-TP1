package copamundo.usuarios.controle;

import copamundo.usuarios.excecoes.PersistenciaException;
import copamundo.usuarios.persistencia.PersistenciaUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.Serializable;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class TelaUsuariosController implements Serializable {
    @FXML
    private TextField campoNomeUsuario;
    @FXML
    private TextField campoCpfUsuario;
    @FXML
    private ComboBox<Usuario.Funcao> campoFuncao;
    @FXML
    private ComboBox<Usuario.Pais> campoPais;
    @FXML
    private ComboBox<Usuario.Status> campoStatus;

    @FXML
    private TableView<Usuario> tabelaUsuarios;
    @FXML
    private TableColumn<Usuario, String> colunaNome;
    @FXML
    private TableColumn<Usuario, String> colunaCpf;

    // CORREÇÃO: Colunas configuradas com os tipos corretos dos Enums
    @FXML
    private TableColumn<Usuario, Usuario.Funcao> colunaFuncao;
    @FXML
    private TableColumn<Usuario, Usuario.Pais> colunaPais;
    @FXML
    private TableColumn<Usuario, Usuario.Status> colunaStatus;

    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnRelatorios;

    private void esconderComponentesDeGerenciamento() {
        btnCadastrar.setVisible(false);
        btnCadastrar.setManaged(false);

        btnEditar.setVisible(false);
        btnEditar.setManaged(false);

        btnExcluir.setVisible(false);
        btnExcluir.setManaged(false);

        System.out.println("Componentes de modificação ocultados para usuário comum.");
    }

    @FXML
    private void initialize() {
        Usuario usuarioLogado = TelaLoginController.getUsuarioLogado();

        if (usuarioLogado != null && usuarioLogado.getFuncao() != Usuario.Funcao.ADMINISTRADOR) {
            esconderComponentesDeGerenciamento();
        }

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
        colunaCpf.setCellValueFactory(new PropertyValueFactory<>("Cpf"));
        colunaFuncao.setCellValueFactory(new PropertyValueFactory<>("funcao"));
        colunaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // adicionado: alimenta os comboBoxes da tela com as opções do enum

        if (campoFuncao != null) {
            campoFuncao.setItems(FXCollections.observableArrayList(Usuario.Funcao.values()));
        }
        if (campoPais != null) {
            campoPais.setItems(FXCollections.observableArrayList(Usuario.Pais.values()));
        }
        if (campoStatus != null) {
            campoStatus.setItems(FXCollections.observableArrayList(Usuario.Status.values()));
        }
        carregarTabela(Usuario.usuarios);
    }

    @FXML
    private void buscarUsuarios() {
        List<Usuario> usuariosFiltrados = new ArrayList<>(Usuario.usuarios);

        String nomeDigitado = campoNomeUsuario.getText();
        String cpfDigitado = campoCpfUsuario.getText();

        // proteção contra NullPointerException caso os campos não estejam linkados no FXML
        Usuario.Funcao funcaoSelecionada = (campoFuncao != null) ? campoFuncao.getValue() : null;
        Usuario.Pais paisSelecionado = (campoPais != null) ? campoPais.getValue() : null;
        Usuario.Status statusSelecionado = (campoStatus != null) ? campoStatus.getValue() : null;

        // filtra por nome apenas se o usuário digitou algo
        if (nomeDigitado != null && !nomeDigitado.trim().isEmpty()) {
            usuariosFiltrados.removeIf(u -> u.getNomeUsuario() == null ||
                    !u.getNomeUsuario().toLowerCase().contains(nomeDigitado.toLowerCase()));
        }

        // filtra por CPF apenas se o usuário digitou algo
        if (cpfDigitado != null && !cpfDigitado.trim().isEmpty()) {
            String cpfLimpo = cpfDigitado.replace(".", "").replace("-", "").trim();
            usuariosFiltrados.removeIf(u -> u.getCpf() == null ||
                    !u.getCpf().replace(".", "").replace("-", "").equals(cpfLimpo));
        }

        // filtra por Função apenas se selecionado
        if (funcaoSelecionada != null && funcaoSelecionada !=  Usuario.Funcao.TODOS) {
            usuariosFiltrados.removeIf(u -> u.getFuncao() != funcaoSelecionada);
        }

        // filtra por País apenas se selecionado
        if (paisSelecionado != null && paisSelecionado !=  Usuario.Pais.TODOS) {
            usuariosFiltrados.removeIf(u -> u.getPais() != paisSelecionado);
        }

        // filtra por Status apenas se selecionado
        if (statusSelecionado != null && statusSelecionado !=  Usuario.Status.TODOS) {
            usuariosFiltrados.removeIf(u -> u.getStatus() != statusSelecionado);
        }

        carregarTabela(usuariosFiltrados);
    }

    @FXML
    private void telaCadastro(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/copamundo/usuarios/visao/TelaCadastro.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void carregarTabela(List<Usuario> lista) {
        ObservableList<Usuario> dadosJavaFX = FXCollections.observableArrayList(lista);
        tabelaUsuarios.setItems(dadosJavaFX);
    }

    // AÇÃO DO BOTÃO EDITAR (conecta o usuário selecionado à TelaEditarController)
    @FXML
    private void telaEditar(ActionEvent event) throws IOException {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            Mensagens.mostrarMensagem(
                    Alert.AlertType.INFORMATION,
                    "Nenhum usuário foi selecionado na tabela para editar.",
                    "Nenhum usuário foi selecionado na tabela para editar."
            );
            System.out.println("Nenhum usuário foi selecionado na tabela para editar.");
            return;
        }

        this.usuarioSelecionado = selecionado;

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/copamundo/usuarios/visao/TelaEditarUsuario.fxml")
        );
        Parent root = loader.load();

        // obtém a instância do controller da tela de edição e envia o usuário selecionado
        TelaEditarController controllerEdicao = loader.getController();
        controllerEdicao.setUsuario(selecionado);

        // abre como uma janela modal separada para manter a consistência com o fechar da edição
        Stage stageEdicao = new Stage();
        stageEdicao.setScene(new Scene(root));
        stageEdicao.setTitle("Editar Usuário");
        stageEdicao.initModality(Modality.WINDOW_MODAL);
        stageEdicao.initOwner(((Node) event.getSource()).getScene().getWindow());

        // pausa a execução desta tela até que a tela de edição seja fechada
        stageEdicao.showAndWait();

        // recarrega a tabela com as alterações que foram salvas no arquivo pela outra tela
        try {
            PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
            Usuario.usuarios = persistencia.carregarUsuarios();
            carregarTabela(Usuario.usuarios);
        } catch (PersistenciaException e) {
            System.out.println("Erro ao recarregar a tabela após edição: " + e.getMessage());
        }
    }

    // AÇÃO DO BOTÃO EXCLUIR (conecta o usuário selecionado à exclusão de persistência)
    @FXML
    private void telaExcluir(ActionEvent event) {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            Mensagens.mostrarMensagem(
                    Alert.AlertType.WARNING,
                    "Nenhum usuário selecionado",
                    "Nenhum usuário foi selecionado na tabela para excluir."
            );
            System.out.println("Nenhum usuário foi selecionado na tabela para excluir.");
            return;
        }

        this.usuarioSelecionado = selecionado;

        if (confirmarExclusaoUsuario()) {
            try {
                // remove da persistência (arquivo físico) utilizando o e-mail do objeto selecionado
                PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
                persistencia.excluirUsuario(selecionado.getEmailUsuario());

                // remove da lista estática local na memória RAM
                Usuario.usuarios.remove(selecionado);

                // atualiza a visualização da tabela
                carregarTabela(Usuario.usuarios);
                this.usuarioSelecionado = null;
                System.out.println("Usuário excluído com sucesso.");

            } catch (PersistenciaException e) {
                System.out.println("Erro ao excluir usuário do arquivo de persistência: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleMenuUsuarios() {}

    @FXML
    private void handleMenuRelatorios() {}

    private Usuario usuarioSelecionado;

    public TelaUsuariosController() {}

    // MÉTODOS DE LISTAGEM E BUSCA

    public List<Usuario> carregarUsuariosNaTabela() {
        return listarUsuarios();
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(Usuario.usuarios);
    }

    public Usuario buscarUsuarioPorNome(String nome) {
        for (Usuario usuario : listarUsuarios()) {
            if (usuario.getNomeUsuario() != null && usuario.getNomeUsuario().equals(nome)) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        for (Usuario usuario : listarUsuarios()) {
            if (usuario.getEmailUsuario() != null && usuario.getEmailUsuario().equalsIgnoreCase(email)) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorIdentificacao(String cpf) {
        String cpfLimpo = limparCpf(cpf);
        for (Usuario usuario : listarUsuarios()) {
            if (limparCpf(usuario.getCpf()).equals(cpfLimpo)) {
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> filtrarUsuariosPorPerfil(Usuario.Funcao funcao) {
        List<Usuario> usuariosFiltrados = new ArrayList<>();
        for (Usuario usuario : listarUsuarios()) {
            if (usuario.getFuncao() == funcao) {
                usuariosFiltrados.add(usuario);
            }
        }
        return usuariosFiltrados;
    }

    public List<Usuario> filtrarUsuariosPorPais(Usuario.Pais pais) {
        List<Usuario> usuariosFiltrados = new ArrayList<>();
        for (Usuario usuario : listarUsuarios()) {
            if (usuario.getPais() == pais) {
                usuariosFiltrados.add(usuario);
            }
        }
        return usuariosFiltrados;
    }

    public List<Usuario> filtrarUsuariosPorStatus(Usuario.Status status) {
        List<Usuario> usuariosFiltrados = new ArrayList<>();
        for (Usuario usuario : listarUsuarios()) {
            if (usuario.getStatus() == status) {
                usuariosFiltrados.add(usuario);
            }
        }
        return usuariosFiltrados;
    }

    public List<Usuario> ordenarUsuariosPorNome() {
        List<Usuario> usuariosOrdenados = new ArrayList<>(listarUsuarios());
        usuariosOrdenados.sort(Comparator.comparing(
                Usuario::getNomeUsuario,
                Comparator.nullsLast(String::compareToIgnoreCase)
        ));
        return usuariosOrdenados;
    }

    public List<Usuario> limparFiltrosUsuarios() {
        return listarUsuarios();
    }

    public List<Usuario> atualizarTabelaUsuarios() {
        return listarUsuarios();
    }

    // MÉTODOS DE NAVEGAÇÃO E SELEÇÃO

    public void selecionarUsuario(Usuario usuario) {
        this.usuarioSelecionado = usuario;
    }

    public Usuario getUsuarioSelecionado() {
        return this.usuarioSelecionado;
    }

    public void abrirFormularioCadastroUsuario() {
        System.out.println("Redirecionando para a tela de cadastro de novo usuário...");
    }

    public void abrirFormularioEdicaoUsuario() {
        if (this.usuarioSelecionado != null) {
            System.out.println("Redirecionando para a tela de edição do usuário: " + usuarioSelecionado.getNomeUsuario());
        } else {
            Mensagens.mostrarMensagem(
                    Alert.AlertType.WARNING,
                    "Nenhum usuário selecionado",
                    "Nenhum usuário foi selecionado na tabela para editar."
            );
            System.out.println("Nenhum usuário foi selecionado na tabela para editar.");
        }
    }

    public void voltarTelaLogin() {
        System.out.println("Retornando para a tela de login...");
    }

    // MÉTODOS DE EXCLUSÃO

    public boolean confirmarExclusaoUsuario() {
        if (this.usuarioSelecionado == null) {
            Mensagens.mostrarMensagem(
                    Alert.AlertType.WARNING,
                    "Nenhum usuário selecionado",
                    "Selecione um usuário antes de excluir."
            );
            return false;
        }

        return Mensagens.confirmar(
                "Confirmar exclusão",
                "Deseja realmente excluir este usuário?"
        );
    }

    public boolean excluirUsuario() {
        if (this.usuarioSelecionado != null && confirmarExclusaoUsuario()) {
            Usuario.usuarios.remove(this.usuarioSelecionado);
            this.usuarioSelecionado = null;
            return true;
        }
        return false;
    }

    public boolean excluirLogicamenteUsuario() {
        if (this.usuarioSelecionado != null) {
            return this.usuarioSelecionado.desativarUsuario();
        }
        return false;
    }

    public List<Usuario> atualizarTabelaAposExclusao() {
        return listarUsuarios();
    }

    private String limparCpf(String Cpf) {
        if (Cpf == null) {
            return "";
        }
        return Cpf.replace(".", "").replace("-", "").trim();
    }
}