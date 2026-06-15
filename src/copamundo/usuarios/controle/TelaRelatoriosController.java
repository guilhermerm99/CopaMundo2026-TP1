package copamundo.usuarios.controle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.NumberAxis;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TelaRelatoriosController {

    private Relatorios relatoriosLogic = new Relatorios();

    @FXML
    private ComboBox<String> comboTipoRelatorio;

    @FXML
    private ComboBox<String> comboSelecao;

    @FXML
    private DatePicker dateInicio;

    @FXML
    private DatePicker dateFim;

    @FXML
    private Label lblNumeroPartidas;

    @FXML
    private Label lblPartidasFinalizadas;

    @FXML
    private Label lblTotalGols;

    @FXML
    private Label lblMediaGols;

    @FXML
    private Label lblPartidaMaisGols;

    @FXML
    private Label lblMaiorGoleada;

    @FXML
    private Label lblSelecaoAnalisada;

    @FXML
    private PieChart graficoStatus;

    @FXML
    private BarChart<String, Number> graficoFases;

    @FXML
    private TextArea txtRelatorio;

    @FXML
    private void initialize() {
        relatoriosLogic.carregarPartidasTeste();

        comboTipoRelatorio.setItems(FXCollections.observableArrayList(
                "Geral",
                "Usuários",
                "Partidas",
                "Desempenho"
        ));

        comboSelecao.setItems(FXCollections.observableArrayList(
                "Brasil",
                "Argentina",
                "França",
                "Alemanha"
        ));

        comboTipoRelatorio.setValue("Geral");
        comboSelecao.setValue("Brasil");

        atualizarGraficos();
    }

    @FXML
    private void handleMenuUsuarios(ActionEvent event) throws IOException {
        trocarTela(event, "/copamundo/usuarios/visao/TelaUsuarios.fxml");
    }

    @FXML
    private void handleMenuRelatorios(ActionEvent event) throws IOException {
        trocarTela(event, "/copamundo/usuarios/visao/TelaRelatorios.fxml");
    }

    @FXML
    private void handleGerarRelatorio(ActionEvent event) {
        aplicarFiltrosNaLogica();

        boolean gerou = relatoriosLogic.gerarRelatorio();

        if (!gerou) {
            txtRelatorio.setText("Não foi possível gerar o relatório. Verifique os filtros.");
            return;
        }

        atualizarCards();
        atualizarGraficos();
        atualizarDestaques();

        txtRelatorio.setText(relatoriosLogic.getConteudoUltimoRelatorio());
    }

    @FXML
    private void handleLimparRelatorios(ActionEvent event) {
        comboTipoRelatorio.setValue("Geral");
        comboSelecao.setValue("Brasil");
        dateInicio.setValue(null);
        dateFim.setValue(null);

        relatoriosLogic.limparFiltrosRelatorio();

        lblNumeroPartidas.setText("0");
        lblPartidasFinalizadas.setText("0");
        lblTotalGols.setText("0");
        lblMediaGols.setText("0.00");

        lblPartidaMaisGols.setText("Nenhum relatório gerado.");
        lblMaiorGoleada.setText("Nenhum relatório gerado.");
        lblSelecaoAnalisada.setText("Nenhuma seleção selecionada.");

        txtRelatorio.clear();

        graficoStatus.getData().clear();
        graficoFases.getData().clear();

        relatoriosLogic.carregarPartidasTeste();
        atualizarGraficos();
    }

    @FXML
    private void handleGerarPDF(ActionEvent event) {
        aplicarFiltrosNaLogica();

        boolean gerou = relatoriosLogic.gerarRelatorio();

        if (!gerou) {
            txtRelatorio.setText("Não foi possível gerar o relatório para PDF.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório em PDF");
        fileChooser.setInitialFileName("Relatorio_Copa.pdf");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivo PDF", "*.pdf")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File arquivoDestino = fileChooser.showSaveDialog(stage);

        if (arquivoDestino != null) {
            boolean sucesso = relatoriosLogic.gerarArquivoPDF(arquivoDestino.getAbsolutePath());

            if (sucesso) {
                txtRelatorio.setText(relatoriosLogic.getConteudoUltimoRelatorio());
                atualizarCards();
                atualizarGraficos();
                atualizarDestaques();

                System.out.println("Operação concluída. Arquivo salvo em: " + arquivoDestino.getAbsolutePath());
            } else {
                txtRelatorio.setText("Falha na operação de salvar o PDF.");
            }
        }
    }

    private void aplicarFiltrosNaLogica() {
        String tipo = comboTipoRelatorio.getValue();
        String selecao = comboSelecao.getValue();

        if (tipo == null || tipo.trim().isEmpty()) {
            tipo = "Geral";
        }

        if (selecao == null || selecao.trim().isEmpty()) {
            selecao = "Brasil";
        }

        relatoriosLogic.setTipoRelatorio(tipo);
        relatoriosLogic.setDesempenhoSelecoes(selecao);

        if (dateInicio.getValue() != null) {
            Date inicio = Date.from(dateInicio.getValue()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());

            relatoriosLogic.setDataInicio(inicio);
        } else {
            relatoriosLogic.setDataInicio(null);
        }

        if (dateFim.getValue() != null) {
            Date fim = Date.from(dateFim.getValue()
                    .atTime(23, 59, 59)
                    .atZone(ZoneId.systemDefault())
                    .toInstant());

            relatoriosLogic.setDataFim(fim);
        } else {
            relatoriosLogic.setDataFim(null);
        }
    }

    private void atualizarCards() {
        List<Relatorios.PartidaRelatorio> partidas = relatoriosLogic.filtrarPartidasPorPeriodo();

        int numeroPartidas = partidas.size();
        int finalizadas = 0;
        int totalGols = 0;

        for (Relatorios.PartidaRelatorio partida : partidas) {
            if (partida.getStatus() != null && partida.getStatus().equalsIgnoreCase("finalizada")) {
                finalizadas++;
                totalGols += partida.getGolsSelecaoA() + partida.getGolsSelecaoB();
            }
        }

        double media = 0;

        if (finalizadas > 0) {
            media = (double) totalGols / finalizadas;
        }

        lblNumeroPartidas.setText(String.valueOf(numeroPartidas));
        lblPartidasFinalizadas.setText(String.valueOf(finalizadas));
        lblTotalGols.setText(String.valueOf(totalGols));
        lblMediaGols.setText(String.format("%.2f", media));
    }

    private void atualizarGraficos() {
        atualizarGraficoStatus();
        atualizarGraficoFases();
    }

    private void atualizarGraficoStatus() {
        graficoStatus.getData().clear();

        Map<String, Integer> porStatus = relatoriosLogic.contarPartidasPorStatus();

        for (String status : porStatus.keySet()) {
            graficoStatus.getData().add(
                    new PieChart.Data(status, porStatus.get(status))
            );
        }
    }

    private void atualizarGraficoFases() {
        graficoFases.getData().clear();

        Map<String, Integer> porFase = relatoriosLogic.contarPartidasPorFase();

        XYChart.Series<String, Number> serie = new XYChart.Series<>();

        int maiorValor = 0;

        for (String fase : porFase.keySet()) {
            int quantidade = porFase.get(fase);

            if (quantidade > maiorValor) {
                maiorValor = quantidade;
            }

            serie.getData().add(new XYChart.Data<>(fase, quantidade));
        }

        NumberAxis eixoY = (NumberAxis) graficoFases.getYAxis();
        eixoY.setAutoRanging(false);
        eixoY.setLowerBound(0);
        eixoY.setUpperBound(maiorValor + 1);
        eixoY.setTickUnit(1);

        graficoFases.setCategoryGap(12);
        graficoFases.setBarGap(3);

        graficoFases.getData().add(serie);
    }

    private void atualizarDestaques() {
        List<Relatorios.PartidaRelatorio> partidas = relatoriosLogic.filtrarPartidasPorPeriodo();

        Relatorios.PartidaRelatorio partidaMaisGols = null;
        Relatorios.PartidaRelatorio maiorGoleada = null;

        int maiorQuantidadeGols = -1;
        int maiorDiferencaGols = -1;

        for (Relatorios.PartidaRelatorio partida : partidas) {
            if (partida.getStatus() == null || !partida.getStatus().equalsIgnoreCase("finalizada")) {
                continue;
            }

            int totalGols = partida.getGolsSelecaoA() + partida.getGolsSelecaoB();
            int diferenca = Math.abs(partida.getGolsSelecaoA() - partida.getGolsSelecaoB());

            if (totalGols > maiorQuantidadeGols) {
                maiorQuantidadeGols = totalGols;
                partidaMaisGols = partida;
            }

            if (diferenca > maiorDiferencaGols) {
                maiorDiferencaGols = diferenca;
                maiorGoleada = partida;
            }
        }

        if (partidaMaisGols != null) {
            lblPartidaMaisGols.setText("Partida com mais gols: " + descreverPartida(partidaMaisGols));
        } else {
            lblPartidaMaisGols.setText("Partida com mais gols: nenhuma partida finalizada.");
        }

        if (maiorGoleada != null) {
            lblMaiorGoleada.setText("Maior goleada: " + descreverPartida(maiorGoleada));
        } else {
            lblMaiorGoleada.setText("Maior goleada: nenhuma partida finalizada.");
        }

        lblSelecaoAnalisada.setText("Seleção analisada: " + comboSelecao.getValue());
    }

    private String descreverPartida(Relatorios.PartidaRelatorio partida) {
        return partida.getSelecaoA() + " " +
                partida.getGolsSelecaoA() + " x " +
                partida.getGolsSelecaoB() + " " +
                partida.getSelecaoB();
    }

    private void trocarTela(ActionEvent event, String caminhoFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}