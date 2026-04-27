package copamundo.selecoes.visao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EstiloUI {
    // Paleta de cores
    public static final Color COR_PRIMARIA = new Color(0x2E7D32);    // Verde escuro
    public static final Color COR_PRIMARIA_HOVER = new Color(0x1B5E20);
    public static final Color COR_FUNDO = new Color(0xF5F5F5);       // Cinza claro
    public static final Color COR_TEXTO = new Color(0x212121);
    public static final Color COR_BOTAO_TEXTO = Color.WHITE;
    public static final Color COR_CABECALHO_TABELA = new Color(0x388E3C);
    public static final Color COR_LINHA_ALTERNADA = new Color(0xE8F5E9);
    public static final Color COR_BORDA = new Color(0xBDBDBD);

    // Fonte padrão
    public static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 13);

    // Configura uma janela com padding e ícone
    public static void configurarJanela(JFrame frame, String titulo, int largura, int altura) {
        frame.setTitle(titulo);
        frame.setSize(largura, altura);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(COR_FUNDO);
    }

    // Cria um botão estilizado com ícone Uniccode à esquerda
    public static JButton criarBotao(String texto, String iconeUnicode) {
        JButton btn = new JButton(iconeUnicode + "  " + texto);
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(COR_BOTAO_TEXTO);
        btn.setBackground(COR_PRIMARIA);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COR_PRIMARIA.darker(), 1, true),
                new EmptyBorder(8, 15, 8, 15)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efeito hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COR_PRIMARIA_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COR_PRIMARIA);
            }
        });
        return btn;
    }

    // Configura um JTextField com placeholder visual
    public static JTextField criarCampoTexto(String placeholder) {
        JTextField campo = new JTextField(20);
        campo.setFont(FONTE_PADRAO);
        campo.setForeground(COR_TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COR_BORDA, 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
        // Placeholder usando TextPrompt (classe interna)
        new TextPrompt(placeholder, campo);
        return campo;
    }

    // Configura JComboBox
    public static JComboBox<String> criarComboBox(String[] itens) {
        JComboBox<String> combo = new JComboBox<>(itens);
        combo.setFont(FONTE_PADRAO);
        combo.setBackground(Color.WHITE);
        return combo;
    }

    // Configura um JTable com estilo
    public static void configurarTabela(JTable tabela, JScrollPane scroll) {
        // Cabeçalho
        JTableHeader header = tabela.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(COR_CABECALHO_TABELA);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                return lbl;
            }
        });

        // Células
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setFont(FONTE_PADRAO);
                if (row % 2 == 0) {
                    lbl.setBackground(Color.WHITE);
                } else {
                    lbl.setBackground(COR_LINHA_ALTERNADA);
                }
                return lbl;
            }
        });
        tabela.setRowHeight(25);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0,0));
        scroll.setBorder(new LineBorder(COR_BORDA, 1));
    }

    // Classe interna para placeholder em JTextField
    static class TextPrompt extends JLabel {
        public TextPrompt(String text, JTextField component) {
            setText(text);
            setFont(component.getFont().deriveFont(Font.ITALIC));
            setForeground(Color.GRAY);
            setBorder(new EmptyBorder(5, 8, 5, 8));
            component.setLayout(new BorderLayout());
            component.add(this, BorderLayout.WEST);
            component.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) { toggle(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { toggle(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { }
                private void toggle() {
                    setVisible(component.getText().isEmpty());
                }
            });
            setVisible(true);
        }
    }
}