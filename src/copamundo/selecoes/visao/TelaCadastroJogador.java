package copamundo.selecoes.visao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaCadastroJogador extends JFrame {
    private JTextField campoNome, campoNumero, campoIdade;
    private JComboBox<String> comboPosicao, comboStatus, comboSelecao;
    private JTable tabela;
    private DefaultTableModel model;

    public TelaCadastroJogador() {
        EstiloUI.configurarJanela(this, "Cadastro de Jogadores", 750, 550);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelPrincipal.setBackground(EstiloUI.COR_FUNDO);

        // Formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBackground(Color.WHITE);
        painelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA, 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        campoNome = EstiloUI.criarCampoTexto("Nome completo do jogador");
        painelForm.add(campoNome, gbc);

        // Posição
        gbc.gridx = 0; gbc.gridy = 1;
        painelForm.add(new JLabel("Posição:"), gbc);
        gbc.gridx = 1;
        comboPosicao = EstiloUI.criarComboBox(new String[]{"Goleiro", "Zagueiro", "Lateral", "Meio-campo", "Atacante"});
        painelForm.add(comboPosicao, gbc);

        // Número
        gbc.gridx = 0; gbc.gridy = 2;
        painelForm.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1;
        campoNumero = EstiloUI.criarCampoTexto("Ex: 10");
        painelForm.add(campoNumero, gbc);

        // Idade
        gbc.gridx = 0; gbc.gridy = 3;
        painelForm.add(new JLabel("Idade:"), gbc);
        gbc.gridx = 1;
        campoIdade = EstiloUI.criarCampoTexto("Idade do jogador");
        painelForm.add(campoIdade, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 4;
        painelForm.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        comboStatus = EstiloUI.criarComboBox(new String[]{"Ativo", "Lesionado", "Suspenso"});
        painelForm.add(comboStatus, gbc);

        // Seleção
        gbc.gridx = 0; gbc.gridy = 5;
        painelForm.add(new JLabel("Seleção:"), gbc);
        gbc.gridx = 1;
        comboSelecao = new JComboBox<>(); // Será preenchido dinamicamente depois
        comboSelecao.setFont(EstiloUI.FONTE_PADRAO);
        painelForm.add(comboSelecao, gbc);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        painelBotoes.setBackground(Color.WHITE);
        JButton btnNovo = EstiloUI.criarBotao("Novo", "➕");
        JButton btnEditar = EstiloUI.criarBotao("Editar", "✏️");
        JButton btnExcluir = EstiloUI.criarBotao("Excluir", "🗑️");
        JButton btnSalvar = EstiloUI.criarBotao("Salvar", "💾");
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnSalvar);

        // Tabela
        String[] colunas = {"Nome", "Posição", "Nº", "Idade", "Status", "Seleção"};
        model = new DefaultTableModel(colunas, 0);
        tabela = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabela);
        EstiloUI.configurarTabela(tabela, scroll);

        // Montagem
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(painelForm, BorderLayout.CENTER);
        painelTopo.add(painelBotoes, BorderLayout.SOUTH);
        painelPrincipal.add(painelTopo, BorderLayout.NORTH);
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        add(painelPrincipal);

        // Listeners vazios
        btnNovo.addActionListener(e -> {});
        btnEditar.addActionListener(e -> {});
        btnExcluir.addActionListener(e -> {});
        btnSalvar.addActionListener(e -> {});
    }
}