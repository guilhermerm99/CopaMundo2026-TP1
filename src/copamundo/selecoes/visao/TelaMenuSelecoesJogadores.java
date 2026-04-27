package copamundo.selecoes.visao;

import javax.swing.*;

public class TelaMenuSelecoesJogadores extends JFrame {
    public TelaMenuSelecoesJogadores() {
        setTitle("Módulo Seleções e Jogadores");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton btnSelecoes = new JButton("Cadastro de Seleções");
        JButton btnJogadores = new JButton("Cadastro de Jogadores");
        JButton btnConsulta = new JButton("Consultas");

        panel.add(btnSelecoes);
        panel.add(btnJogadores);
        panel.add(btnConsulta);
        add(panel);

        btnSelecoes.addActionListener(e -> new TelaCadastroSelecao().setVisible(true));
        btnJogadores.addActionListener(e -> new TelaCadastroJogador().setVisible(true));
        btnConsulta.addActionListener(e -> new TelaConsultaSelecoesJogadores().setVisible(true));
    }
}
