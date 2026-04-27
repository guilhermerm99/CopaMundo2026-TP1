package copamundo.selecoes.visao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaCadastroSelecao extends JFrame {
    private JTextField campoPais, campoTecnico;
    private JComboBox<String> comboGrupo;
    private JTable tabela;
    private DefaultTableModel model;

    public TelaCadastroSelecao() {
        setTitle("Cadastro de Seleções");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 5, 5));
        painelCampos.add(new JLabel("País:"));
        campoPais = new JTextField(15);
        painelCampos.add(campoPais);

        painelCampos.add(new JLabel("Grupo:"));
        comboGrupo = new JComboBox<>(new String[]{"A", "B", "C", "D", "E", "F", "G", "H"});
        painelCampos.add(comboGrupo);

        painelCampos.add(new JLabel("Técnico:"));
        campoTecnico = new JTextField(15);
        painelCampos.add(campoTecnico);

        String[] colunas = {"País", "Grupo", "Técnico"};
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
