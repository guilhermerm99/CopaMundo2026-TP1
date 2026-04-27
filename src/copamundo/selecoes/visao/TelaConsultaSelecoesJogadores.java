package copamundo.selecoes.visao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaConsultaSelecoesJogadores extends JFrame {
    private JTable tabelaSelecoes, tabelaJogadores;
    private DefaultTableModel modelSelecoes, modelJogadores;
    private JComboBox<String> comboGrupoFiltro, comboPosicaoFiltro, comboStatusFiltro;

    public TelaConsultaSelecoesJogadores() {
        EstiloUI.configurarJanela(this, "Consultas", 850, 550);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(EstiloUI.FONTE_PADRAO);

        // Aba Seleções
        JPanel painelSelecoes = new JPanel(new BorderLayout(10, 10));
        painelSelecoes.setBackground(EstiloUI.COR_FUNDO);
        JPanel filtroSelecoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtroSelecoes.setBackground(EstiloUI.COR_FUNDO);
        filtroSelecoes.add(new JLabel("Grupo:"));
        comboGrupoFiltro = EstiloUI.criarComboBox(new String[]{"Todos","A","B","C","D","E","F","G","H"});
        filtroSelecoes.add(comboGrupoFiltro);
        JButton btnFiltrarSelecao = EstiloUI.criarBotao("Filtrar", "🔍");
        filtroSelecoes.add(btnFiltrarSelecao);

        String[] colSel = {"País", "Grupo", "Técnico"};
        modelSelecoes = new DefaultTableModel(colSel, 0);
        tabelaSelecoes = new JTable(modelSelecoes);
        JScrollPane scrollSel = new JScrollPane(tabelaSelecoes);
        EstiloUI.configurarTabela(tabelaSelecoes, scrollSel);

        painelSelecoes.add(filtroSelecoes, BorderLayout.NORTH);
        painelSelecoes.add(scrollSel, BorderLayout.CENTER);

        // Aba Jogadores
        JPanel painelJogadores = new JPanel(new BorderLayout(10, 10));
        painelJogadores.setBackground(EstiloUI.COR_FUNDO);
        JPanel filtroJogadores = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtroJogadores.setBackground(EstiloUI.COR_FUNDO);
        filtroJogadores.add(new JLabel("Posição:"));
        comboPosicaoFiltro = EstiloUI.criarComboBox(new String[]{"Todas","Goleiro","Zagueiro","Lateral","Meio-campo","Atacante"});
        filtroJogadores.add(comboPosicaoFiltro);
        filtroJogadores.add(new JLabel("Status:"));
        comboStatusFiltro = EstiloUI.criarComboBox(new String[]{"Todos","Ativo","Lesionado","Suspenso"});
        filtroJogadores.add(comboStatusFiltro);
        JButton btnFiltrarJogador = EstiloUI.criarBotao("Filtrar", "🔍");
        filtroJogadores.add(btnFiltrarJogador);

        String[] colJog = {"Nome", "Posição", "Nº", "Idade", "Status", "Seleção"};
        modelJogadores = new DefaultTableModel(colJog, 0);
        tabelaJogadores = new JTable(modelJogadores);
        JScrollPane scrollJog = new JScrollPane(tabelaJogadores);
        EstiloUI.configurarTabela(tabelaJogadores, scrollJog);

        painelJogadores.add(filtroJogadores, BorderLayout.NORTH);
        painelJogadores.add(scrollJog, BorderLayout.CENTER);

        abas.addTab("🏆 Seleções", painelSelecoes);
        abas.addTab("👤 Jogadores", painelJogadores);
        add(abas);

        btnFiltrarSelecao.addActionListener(e -> {});
        btnFiltrarJogador.addActionListener(e -> {});
    }
}