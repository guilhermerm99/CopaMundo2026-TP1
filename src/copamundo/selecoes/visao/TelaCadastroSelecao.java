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
        EstiloUI.configurarJanela(this, "Cadastro de Seleções", 650, 500);

        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelPrincipal.setBackground(EstiloUI.COR_FUNDO);

        // Formulário (topo)
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBackground(Color.WHITE);
        painelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COR_BORDA, 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // País
        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(new JLabel("País:"), gbc);
        gbc.gridx = 1;
        campoPais = EstiloUI.criarCampoTexto("Digite o nome do país");
        painelForm.add(campoPais, gbc);

        // Grupo
        gbc.gridx = 0; gbc.gridy = 1;
        painelForm.add(new JLabel("Grupo:"), gbc);
        gbc.gridx = 1;
        comboGrupo = EstiloUI.criarComboBox(new String[]{"A", "B", "C", "D", "E", "F", "G", "H"});
        painelForm.add(comboGrupo, gbc);

        // Técnico
        gbc.gridx = 0; gbc.gridy = 2;
        painelForm.add(new JLabel("Técnico:"), gbc);
        gbc.gridx = 1;
        campoTecnico = EstiloUI.criarCampoTexto("Nome do técnico");
        painelForm.add(campoTecnico, gbc);

        // Botões do formulário
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
        String[] colunas = {"País", "Grupo", "Técnico"};
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

        // Listeners vazios (Etapa 1)
        btnNovo.addActionListener(e -> {});
        btnEditar.addActionListener(e -> {});
        btnExcluir.addActionListener(e -> {});
        btnSalvar.addActionListener(e -> {});
    }
}