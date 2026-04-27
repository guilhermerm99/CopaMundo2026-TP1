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
        setTitle("Cadastro de Jogadores");
        setSize(700, 450);
        setLocationRelativeTo(null);

        JPanel painelCampos = new JPanel(new GridLayout(7, 2, 5, 5));
        painelCampos.add(new JLabel("Nome:"));
        campoNome = new JTextField(15);
        painelCampos.add(campoNome);

        painelCampos.add(new JLabel("Posição:"));
        comboPosicao = new JComboBox<>(new String[]{"Goleiro", "Zagueiro", "Lateral", "Meio-campo", "Atacante"});
        painelCampos.add(comboPosicao);

        painelCampos.add(new JLabel("Número:"));
        campoNumero = new JTextField(5);
        painelCampos.add(campoNumero);

        painelCampos.add(new JLabel("Idade:"));
        campoIdade = new JTextField(3);
        painelCampos.add(campoIdade);

        painelCampos.add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Lesionado", "Suspenso"});
        painelCampos.add(comboStatus);

        painelCampos.add(new JLabel("Seleção:"));
        comboSelecao = new JComboBox<>();
        painelCampos.add(comboSelecao);

        String[] colunas = {"Nome", "Posição", "Nº", "Idade", "Status", "Seleção"};
        model = new DefaultTableModel(colunas, 0);
        tabela = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel painelBotoes = new JPanel();
        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnSalvar = new JButton("Salvar");

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnSalvar);

        btnNovo.addActionListener(e -> {});
        btnEditar.addActionListener(e -> {});
        btnExcluir.addActionListener(e -> {});
        btnSalvar.addActionListener(e -> {});

        setLayout(new BorderLayout());
        add(painelCampos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }
}
