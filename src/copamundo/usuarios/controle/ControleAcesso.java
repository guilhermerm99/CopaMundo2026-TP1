package copamundo.usuarios.controle;

import javafx.scene.Node;

public class ControleAcesso {

    public static Usuario getUsuarioLogado() {
        return TelaLoginController.getUsuarioLogado();
    }

    public static boolean estaLogado() {
        return getUsuarioLogado() != null;
    }

    public static boolean ehAdministrador() {
        Usuario usuario = getUsuarioLogado();
        return usuario != null && usuario.isAtivo() && usuario.isAdministrador();
    }

    public static boolean ehOrganizador() {
        Usuario usuario = getUsuarioLogado();
        return usuario != null && usuario.isAtivo() && usuario.getFuncao() == Usuario.Funcao.ORGANIZADOR;
    }

    public static boolean ehArbitro() {
        Usuario usuario = getUsuarioLogado();
        return usuario != null && usuario.isAtivo() && usuario.getFuncao() == Usuario.Funcao.ARBITRO;
    }

    public static boolean podeGerenciarUsuarios() {
        return ehAdministrador();
    }

    public static boolean podeGerenciarPartidas() {
        return ehAdministrador() || ehOrganizador();
    }

    public static boolean podeVisualizarPartidas() {
        return ehAdministrador() || ehOrganizador() || ehArbitro();
    }

    public static boolean podeRegistrarResultado() {
        return ehAdministrador() || ehArbitro();
    }

    public static boolean podeGerarRelatorios() {
        return ehAdministrador();
    }

    public static boolean podeGerenciarSelecoes() {
        return ehAdministrador() || ehOrganizador();
    }

    public static boolean podeGerenciarEstadios() {
        return ehAdministrador() || ehOrganizador();
    }

    public static void esconderSeNaoPode(boolean permitido, Node... componentes) {
        if (permitido) {
            return;
        }

        for (Node componente : componentes) {
            if (componente != null) {
                componente.setVisible(false);
                componente.setManaged(false);
            }
        }
    }

    public static boolean bloquearAcesso(boolean permitido) {
        if (!permitido) {
            System.out.println("Acesso negado para o perfil do usuário logado.");
            return true;
        }

        return false;
    }
}