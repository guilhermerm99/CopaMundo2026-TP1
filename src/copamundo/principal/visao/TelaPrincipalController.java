package copamundo.principal.visao;

import copamundo.comum.ControleAcesso;
import copamundo.usuarios.controle.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaPrincipalController {

    @FXML private StackPane areaCentral;
    @FXML private Label labelUsuarioLogado;
    @FXML private Label labelModuloAtivo;

    @FXML private Button btnSelecoes;
    @FXML private Button btnEstadios;
    @FXML private Button btnPartidas;
    @FXML private Button btnUsuarios;
    @FXML private Button btnRelatorios;
    @FXML private VBox cardUsuarios;

    private Button btnAtivo = null;

    private static final String FXML_SELECOES   = "/copamundo/selecoes/visao/TelaMenuSelecoesJogadores.fxml";
    private static final String FXML_ESTADIOS   = "/copamundo/estadios/visao/TelaMenuEstadios.fxml";
    private static final String FXML_PARTIDAS   = "/copamundo/partidas/visao/TelaConsultaPartidas.fxml";
    private static final String FXML_USUARIOS   = "/copamundo/usuarios/visao/TelaUsuarios.fxml";
    private static final String FXML_RELATORIOS = "/copamundo/usuarios/visao/TelaRelatorios.fxml";

    @FXML
    public void initialize() {
        aplicarControlePorPerfil();
    }

    private void aplicarControlePorPerfil() {
        Usuario usuario = ControleAcesso.getUsuarioLogado();

        if (usuario != null) {
            labelUsuarioLogado.setText("Logado como: " + usuario.getNomeUsuario() + "  |  Perfil: " + usuario.getFuncao());
        } else {
            labelUsuarioLogado.setText("Logado como: Administrador");
        }

        // Usuários e Relatórios ficam visíveis apenas para o administrador
        boolean isAdmin = ControleAcesso.ehAdministrador();
        btnUsuarios.setVisible(isAdmin);
        btnUsuarios.setManaged(isAdmin);
        btnRelatorios.setVisible(isAdmin);
        btnRelatorios.setManaged(isAdmin);
        if (cardUsuarios != null) {
            cardUsuarios.setVisible(isAdmin);
            cardUsuarios.setManaged(isAdmin);
        }
    }

    public void abrirUsuariosInicial() {
        carregar(FXML_USUARIOS, "👥  Usuários", btnUsuarios);
    }

    @FXML private void abrirSelecoes()   { carregar(FXML_SELECOES,   "🏆  Seleções e Jogadores", btnSelecoes); }
    @FXML private void abrirEstadios()   { carregar(FXML_ESTADIOS,   "🏟️  Estádios e Árbitros",  btnEstadios); }
    @FXML private void abrirPartidas()   { carregar(FXML_PARTIDAS,   "⚽  Partidas",             btnPartidas); }
    @FXML private void abrirUsuarios()   { carregar(FXML_USUARIOS,   "👥  Usuários",             btnUsuarios); }
    @FXML private void abrirRelatorios() { carregar(FXML_RELATORIOS, "📊  Relatórios",           btnRelatorios); }

    @FXML
    private void sair() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Sair");
        confirm.setHeaderText(null);
        confirm.setContentText("Deseja realmente sair do sistema?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    Parent login = FXMLLoader.load(getClass().getResource("/copamundo/usuarios/visao/TelaLogin.fxml"));
                    Stage stage = (Stage) areaCentral.getScene().getWindow();
                    stage.setScene(new Scene(login));
                    stage.setTitle("Login");
                    stage.show();
                } catch (IOException e) {
                    mostrarErro("Erro", "Não foi possível voltar ao login: " + e.getMessage());
                }
            }
        });
    }

    private void carregar(String fxmlPath, String nomeModulo, Button btnOrigem) {
        try {
            Node modulo = FXMLLoader.load(getClass().getResource(fxmlPath));
            areaCentral.getChildren().setAll(modulo);
            labelModuloAtivo.setText("Módulo ativo: " + nomeModulo);
            destacarBotaoAtivo(btnOrigem);
        } catch (IOException e) {
            mostrarErro("Erro ao carregar módulo", "Não foi possível abrir \"" + nomeModulo + "\".\n\nDetalhe: " + e.getMessage());
        }
    }

    private void destacarBotaoAtivo(Button btn) {
        if (btnAtivo != null)
            btnAtivo.setStyle(btnAtivo.getStyle().replace("-fx-background-color: rgba(255,255,255,0.25);", ""));
        btnAtivo = btn;
        if (btn != null)
            btn.setStyle(btn.getStyle() + "-fx-background-color: rgba(255,255,255,0.25);");
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
