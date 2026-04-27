package copamundo.selecoes.visao;

import javax.swing.*;
import java.awt.*;

public class TelaMenuSelecoesJogadores extends JFrame {
    public TelaMenuSelecoesJogadores() {
        EstiloUI.configurarJanela(this, "⚽ Módulo Seleções e Jogadores", 500, 300);

        // Painel central com GridBagLayout
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(EstiloUI.COR_FUNDO);

        // Título
        JLabel lblTitulo = new JLabel("Gestão de Seleções e Jogadores");
        lblTitulo.setFont(EstiloUI.FONTE_TITULO);
        lblTitulo.setForeground(EstiloUI.COR_PRIMARIA);

        // Botões
        JButton btnSelecoes = EstiloUI.criarBotao("Seleções", "🏆");
        JButton btnJogadores = EstiloUI.criarBotao("Jogadores", "👤");
        JButton btnConsulta = EstiloUI.criarBotao("Consultas", "🔍");

        // Adiciona ao painel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(lblTitulo, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(8, 0, 8, 0);
        painel.add(btnSelecoes, gbc);

        gbc.gridy = 2;
        painel.add(btnJogadores, gbc);

        gbc.gridy = 3;
        painel.add(btnConsulta, gbc);

        add(painel);

        // Ações
        btnSelecoes.addActionListener(e -> new TelaCadastroSelecao().setVisible(true));
        btnJogadores.addActionListener(e -> new TelaCadastroJogador().setVisible(true));
        btnConsulta.addActionListener(e -> new TelaConsultaSelecoesJogadores().setVisible(true));
    }
}