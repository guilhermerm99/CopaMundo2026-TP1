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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import copamundo.comum.Partida;
import copamundo.partidas.repositorio.PartidaRepositorio;

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
    private Label lblTituloCard1;

    @FXML
    private Label lblTituloCard2;

    @FXML
    private Label lblTituloCard3;

    @FXML
    private Label lblTituloCard4;

    @FXML
    private Label lblTituloGrafico;

    @FXML
    private void initialize() {
        carregarPartidasReaisNoRelatorio();

        comboTipoRelatorio.setItems(FXCollections.observableArrayList(
                "Geral",
                "Usuários",
                "Partidas",
                "Desempenho"
        ));

        carregarSelecoesReaisNoCombo();

        comboTipoRelatorio.setValue("Geral");

        atualizarGraficos();
    }

    private void carregarSelecoesReaisNoCombo() {
        comboSelecao.getItems().clear();

        List<Relatorios.PartidaRelatorio> partidas = relatoriosLogic.getPartidas();

        for (Relatorios.PartidaRelatorio partida : partidas) {
            adicionarSelecaoNoCombo(partida.getSelecaoA());
            adicionarSelecaoNoCombo(partida.getSelecaoB());
        }

        if (!comboSelecao.getItems().isEmpty()) {
            comboSelecao.setValue(comboSelecao.getItems().get(0));
        }
    }

    private void adicionarSelecaoNoCombo(String selecao) {
        if (selecao == null || selecao.trim().isEmpty()) {
            return;
        }

        for (String item : comboSelecao.getItems()) {
            if (item.equalsIgnoreCase(selecao)) {
                return;
            }
        }

        comboSelecao.getItems().add(selecao);
    }

    private void carregarPartidasReaisNoRelatorio() {
        try {
            List<Partida> partidasReais = PartidaRepositorio.carregarListaPartidas();
            relatoriosLogic.carregarPartidasReais(partidasReais);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar partidas reais no relatório: " + e.getMessage());
            relatoriosLogic.carregarPartidasReais(new java.util.ArrayList<>());
        }
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
        carregarPartidasReaisNoRelatorio();
        aplicarFiltrosNaLogica();

        String tipo = comboTipoRelatorio.getValue();

        if ("Usuários".equalsIgnoreCase(tipo)) {
            atualizarRelatorioUsuarios();
            return;
        }

        if ("Desempenho".equalsIgnoreCase(tipo)) {
            atualizarRelatorioDesempenho();
            return;
        }

        boolean gerou = relatoriosLogic.gerarRelatorio();

        if (!gerou) {
            System.out.println("Não foi possível gerar o relatório. Verifique os filtros.");
            return;
        }

        atualizarRelatorioPartidas();
    }

    private void atualizarRelatorioPartidas() {
        lblTituloCard1.setText("Partidas");
        lblTituloCard2.setText("Finalizadas");
        lblTituloCard3.setText("Gols");
        lblTituloCard4.setText("Média");
        lblTituloGrafico.setText("Partidas por status");

        atualizarCards();
        atualizarGraficos();
        atualizarDestaques();
    }

    private void atualizarRelatorioUsuarios() {
        lblTituloCard1.setText("Usuários");
        lblTituloCard2.setText("Ativos");
        lblTituloCard3.setText("Inativos");
        lblTituloCard4.setText("Admins");
        lblTituloGrafico.setText("Usuários por status");

        int totalUsuarios = Usuario.usuarios.size();
        int ativos = 0;
        int inativos = 0;
        int administradores = 0;
        int organizadores = 0;

        for (Usuario usuario : Usuario.usuarios) {
            if (usuario.getStatus() == Usuario.Status.ATIVO) {
                ativos++;
            }

            if (usuario.getStatus() == Usuario.Status.INATIVO) {
                inativos++;
            }

            if (usuario.getFuncao() == Usuario.Funcao.ADMINISTRADOR) {
                administradores++;
            }

            if (usuario.getFuncao() == Usuario.Funcao.ORGANIZADOR) {
                organizadores++;
            }
        }

        lblNumeroPartidas.setText(String.valueOf(totalUsuarios));
        lblPartidasFinalizadas.setText(String.valueOf(ativos));
        lblTotalGols.setText(String.valueOf(inativos));
        lblMediaGols.setText(String.valueOf(administradores));

        lblPartidaMaisGols.setText("Administradores cadastrados: " + administradores);
        lblMaiorGoleada.setText("Organizadores cadastrados: " + organizadores);
        lblSelecaoAnalisada.setText("Relatório analisado: usuários do sistema.");

        graficoStatus.getData().clear();
        graficoStatus.getData().add(new PieChart.Data("Ativos", ativos));
        graficoStatus.getData().add(new PieChart.Data("Inativos", inativos));
    }

    private void atualizarRelatorioDesempenho() {
        String selecao = comboSelecao.getValue();

        if (selecao == null || selecao.trim().isEmpty()) {
            selecao = "Brasil";
        }

        lblTituloCard1.setText("Jogos");
        lblTituloCard2.setText("Vitórias");
        lblTituloCard3.setText("Gols pró");
        lblTituloCard4.setText("Aproveitamento");
        lblTituloGrafico.setText("Resultado da seleção");

        List<Relatorios.PartidaRelatorio> partidas = relatoriosLogic.filtrarPartidasPorPeriodo();

        int jogos = 0;
        int vitorias = 0;
        int empates = 0;
        int derrotas = 0;
        int golsPro = 0;
        int golsContra = 0;

        for (Relatorios.PartidaRelatorio partida : partidas) {
            if (partida.getStatus() == null || !partida.getStatus().equalsIgnoreCase("finalizada")) {
                continue;
            }

            boolean selecaoA = partida.getSelecaoA() != null &&
                    partida.getSelecaoA().equalsIgnoreCase(selecao);

            boolean selecaoB = partida.getSelecaoB() != null &&
                    partida.getSelecaoB().equalsIgnoreCase(selecao);

            if (!selecaoA && !selecaoB) {
                continue;
            }

            jogos++;

            int golsDaSelecao;
            int golsDoAdversario;

            if (selecaoA) {
                golsDaSelecao = partida.getGolsSelecaoA();
                golsDoAdversario = partida.getGolsSelecaoB();
            } else {
                golsDaSelecao = partida.getGolsSelecaoB();
                golsDoAdversario = partida.getGolsSelecaoA();
            }

            golsPro += golsDaSelecao;
            golsContra += golsDoAdversario;

            if (golsDaSelecao > golsDoAdversario) {
                vitorias++;
            } else if (golsDaSelecao == golsDoAdversario) {
                empates++;
            } else {
                derrotas++;
            }
        }

        int saldo = golsPro - golsContra;
        int pontuacao = (vitorias * 3) + empates;

        double aproveitamento = 0;

        if (jogos > 0) {
            aproveitamento = ((double) pontuacao / (jogos * 3)) * 100;
        }

        lblNumeroPartidas.setText(String.valueOf(jogos));
        lblPartidasFinalizadas.setText(String.valueOf(vitorias));
        lblTotalGols.setText(String.valueOf(golsPro));
        lblMediaGols.setText(String.format("%.2f%%", aproveitamento));

        lblPartidaMaisGols.setText("Empates: " + empates + " | Derrotas: " + derrotas);
        lblMaiorGoleada.setText("Gols contra: " + golsContra + " | Saldo: " + saldo);
        lblSelecaoAnalisada.setText("Seleção analisada: " + selecao + " | Pontuação: " + pontuacao);

        graficoStatus.getData().clear();
        graficoStatus.getData().add(new PieChart.Data("Vitórias", vitorias));
        graficoStatus.getData().add(new PieChart.Data("Empates", empates));
        graficoStatus.getData().add(new PieChart.Data("Derrotas", derrotas));
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

        graficoStatus.getData().clear();

        relatoriosLogic.carregarPartidasTeste();
        atualizarGraficos();
        lblTituloCard1.setText("Partidas");
        lblTituloCard2.setText("Finalizadas");
        lblTituloCard3.setText("Gols");
        lblTituloCard4.setText("Média");
        lblTituloGrafico.setText("Partidas por status");
    }

    @FXML
    private void handleGerarPDF(ActionEvent event) {
        carregarPartidasReaisNoRelatorio();
        aplicarFiltrosNaLogica();

        boolean gerou = relatoriosLogic.gerarRelatorio();

        if (!gerou) {
            System.out.println("Não foi possível gerar o relatório para PDF.");
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
                atualizarCards();
                atualizarGraficos();
                atualizarDestaques();

                System.out.println("Operação concluída. Arquivo salvo em: " + arquivoDestino.getAbsolutePath());
            } else {
                System.out.println("Falha na operação de salvar o PDF.");
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