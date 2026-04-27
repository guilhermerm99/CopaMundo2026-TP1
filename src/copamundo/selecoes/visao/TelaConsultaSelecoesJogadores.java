package copamundo.selecoes.visao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaConsultaSelecoesJogadores extends JFrame {
    private JTable tabelaSelecoes, tabelaJogadores;
    private DefaultTableModel modelSelecoes, modelJogadores;
    private JComboBox<String> comboGrupoFiltro, comboPosicaoFiltro, comboStatusFiltro;

    public TelaConsultaSelecoesJogadores() {
        setTitle("Consultas");
        setSize(800, 500);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();

        // Aba Seleções
        JPanel painelSelecoes = new JPanel(new BorderLayout());
        JPanel filtroSelecoes = new JPanel();
        filtroSelecoes.add(new JLabel("Grupo:"));
        comboGrupoFiltro = new JComboBox<>(new String[]{"Todos","A","B","C","D","E","F","G","H"});
        filtroSelecoes.add(comboGrupoFiltro);
        JButton btnFiltrarSelecao = new JButton("Filtrar");
        filtroSelecoes.add(btnFiltrarSelecao);

        String[] colSel = {"País", "Grupo", "Técnico"};
        modelSelecoes = new DefaultTableModel(colSel, 0);
        tabelaSelecoes = new JTable(modelSelecoes);
        painelSelecoes.add(filtroSelecoes, BorderLayout.NORTH);
        painelSelecoes.add(new JScrollPane(tabelaSelecoes), BorderLayout.CENTER);

        // Aba Jogadores
        JPanel painelJogadores = new JPanel(new BorderLayout());
        JPanel filtroJogadores = new JPanel();
        filtroJogadores.add(new JLabel("Posição:"));
        comboPosicaoFiltro = new JComboBox<>(new String[]{"Todas","Goleiro","Zagueiro","Lateral","Meio-campo","Atacante"});
        filtroJogadores.add(comboPosicaoFiltro);
        filtroJogadores.add(new JLabel("Status:"));
        comboStatusFiltro = new JComboBox<>(new String[]{"Todos","Ativo","Lesionado","Suspenso"});
        filtroJogadores.add(comboStatusFiltro);
        JButton btnFiltrarJogador = new JButton("Filtrar");
        filtroJogadores.add(btnFiltrarJogador);

        String[] colJog = {"Nome", "Posição", "Nº", "Idade", "Status", "Seleção"};
        modelJogadores = new DefaultTableModel(colJog, 0);
        tabelaJogadores = new JTable(modelJogadores);
        painelJogadores.add(filtroJogadores, BorderLayout.NORTH);
        painelJogadores.add(new JScrollPane(tabelaJogadores), BorderLayout.CENTER);

        abas.addTab("Seleções", painelSelecoes);
        abas.addTab("Jogadores", painelJogadores);
        add(abas);

        btnFiltrarSelecao.addActionListener(e -> {});
        btnFiltrarJogador.addActionListener(e -> {});
    }
}
