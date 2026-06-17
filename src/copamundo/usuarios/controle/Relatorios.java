package copamundo.usuarios.controle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import copamundo.comum.Partida;
import copamundo.comum.Resultado;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public class Relatorios {
    private int id;
    private String tipoRelatorio;
    private Date dataGeracao;
    private String responsavel;

    private int numeroPartidas;
    private int publicoTotal;
    private double mediaPublico;
    private String desempenhoSelecoes;

    private String caminhoPdf;
    private boolean gerado;

    private Date dataInicio;
    private Date dataFim;

    private String conteudoUltimoRelatorio = "";

    private static final String PASTA_RELATORIOS = "relatorios";
    private static final String ARQUIVO_HISTORICO = "relatorios/historico_relatorios.txt";

    private List<PartidaRelatorio> partidas = new ArrayList<>();
    private List<String> historicoRelatorios = new ArrayList<>();
    private Map<Usuario, Date> ultimosLogins = new HashMap<>();

    public static class PartidaRelatorio {
        private String selecaoA;
        private String selecaoB;
        private int golsSelecaoA;
        private int golsSelecaoB;
        private String status;
        private String fase;
        private String estadio;
        private Date dataPartida;
        private int publico;

        public PartidaRelatorio(String selecaoA, String selecaoB, int golsSelecaoA, int golsSelecaoB,
                                String status, String fase, String estadio, Date dataPartida, int publico) {
            this.selecaoA = selecaoA;
            this.selecaoB = selecaoB;
            this.golsSelecaoA = golsSelecaoA;
            this.golsSelecaoB = golsSelecaoB;
            this.status = status;
            this.fase = fase;
            this.estadio = estadio;
            this.dataPartida = dataPartida;
            this.publico = publico;
        }

        public String getSelecaoA() {
            return selecaoA;
        }
        public String getSelecaoB() {
            return selecaoB;
        }
        public int getGolsSelecaoA() {
            return golsSelecaoA;
        }
        public int getGolsSelecaoB() {
            return golsSelecaoB;
        }
        public String getStatus() {
            return status;
        }
        public String getFase() {
            return fase;
        }
        public String getEstadio() {
            return estadio;
        }
        public Date getDataPartida() {
            return dataPartida;
        }
        public int getPublico() {
            return publico;
        }
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getTipoRelatorio() {
        return tipoRelatorio;
    }
    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }


    public Date getDataGeracao() {
        return dataGeracao;
    }
    public void setDataGeracao(Date dataGeracao) {
        this.dataGeracao = dataGeracao;
    }


    public String getResponsavel() {
        return responsavel;
    }
    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }


    public int getNumeroPartidas() {
        return numeroPartidas;
    }
    public void setNumeroPartidas(int numeroPartidas) {
        this.numeroPartidas = numeroPartidas;
    }


    public int getPublicoTotal() {
        return publicoTotal;
    }
    public void setPublicoTotal(int publicoTotal) {
        this.publicoTotal = publicoTotal;
    }


    public double getMediaPublico() {
        return mediaPublico;
    }
    public void setMediaPublico(double mediaPublico) {
        this.mediaPublico = mediaPublico;
    }


    public String getDesempenhoSelecoes() {
        return desempenhoSelecoes;
    }
    public void setDesempenhoSelecoes(String desempenhoSelecoes) {
        this.desempenhoSelecoes = desempenhoSelecoes;
    }


    public String getCaminhoPdf() {
        return caminhoPdf;
    }
    public void setCaminhoPdf(String caminhoPdf) {
        this.caminhoPdf = caminhoPdf;
    }


    public boolean isGerado() {
        return gerado;
    }
    public void setGerado(boolean gerado) {
        this.gerado = gerado;
    }

    public Date getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public List<PartidaRelatorio> getPartidas() {
        return partidas;
    }
    public List<String> getHistoricoRelatorios() {
        return historicoRelatorios;
    }

    // gerais
    public void abrirTelaRelatorios() {
        System.out.println("tela de relatórios aberta.");
    }

    public void selecionarTipoRelatorio() {
        if (tipoRelatorio == null || tipoRelatorio.trim().isEmpty()) {
            tipoRelatorio = "geral";
        }
    }

    public void selecionarTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public boolean validarFiltrosRelatorio() {
        if (tipoRelatorio == null || tipoRelatorio.trim().isEmpty()) {
            return false;
        }

        if (!validarPeriodoRelatorio()) {
            return false;
        }

        return true;
    }

    public boolean validarPeriodoRelatorio() {
        if (dataInicio != null && dataFim != null) {
            if (dataInicio.after(dataFim)) {
                return false;
            }
        }

        return true;
    }

    public String getConteudoUltimoRelatorio() {
        return conteudoUltimoRelatorio;
    }

    public boolean gerarArquivoPDF(String caminhoDestino) {
        if (!isGerado() || conteudoUltimoRelatorio == null || conteudoUltimoRelatorio.isEmpty()) {
            System.out.println("Erro: Não há conteúdo gerado para exportar.");
            return false;
        }

        try {
            PdfWriter writer = new PdfWriter(caminhoDestino);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            document.setMargins(35, 35, 35, 35);

            PdfFont fonteTitulo = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fonteSubtitulo = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fonteTextoNormal = PdfFontFactory.createFont(StandardFonts.COURIER);
            PdfFont fonteNegrito = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            DeviceRgb azulEscuro = new DeviceRgb(5, 24, 48);
            DeviceRgb azulMedio = new DeviceRgb(12, 46, 86);
            DeviceRgb dourado = new DeviceRgb(198, 151, 73);
            DeviceRgb cinzaClaro = new DeviceRgb(245, 247, 250);
            DeviceRgb cinzaTexto = new DeviceRgb(55, 55, 55);

            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dataAtual = formatoData.format(new Date());

            Div cabecalho = new Div()
                    .setBackgroundColor(azulEscuro)
                    .setPaddingTop(22)
                    .setPaddingBottom(22)
                    .setPaddingLeft(25)
                    .setPaddingRight(25)
                    .setMarginBottom(18);

            Paragraph selo = new Paragraph("COPA DO MUNDO 2026")
                    .setFont(fonteNegrito)
                    .setFontSize(10)
                    .setFontColor(dourado)
                    .setCharacterSpacing(1.5f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(8);

            Paragraph titulo = new Paragraph("RELATÓRIO OFICIAL DO SISTEMA")
                    .setFont(fonteTitulo)
                    .setFontSize(22)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(4);

            Paragraph subtituloCabecalho = new Paragraph("Gestão de usuários, partidas e desempenho operacional")
                    .setFont(fonteSubtitulo)
                    .setFontSize(11)
                    .setFontColor(new DeviceRgb(220, 225, 232))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(0);

            cabecalho.add(selo);
            cabecalho.add(titulo);
            cabecalho.add(subtituloCabecalho);
            document.add(cabecalho);

            Div faixaInfo = new Div()
                    .setBorder(new SolidBorder(dourado, 1.2f))
                    .setBackgroundColor(cinzaClaro)
                    .setPaddingTop(12)
                    .setPaddingBottom(12)
                    .setPaddingLeft(16)
                    .setPaddingRight(16)
                    .setMarginBottom(18);

            Paragraph infoGeracao = new Paragraph()
                    .setFont(fonteSubtitulo)
                    .setFontSize(10.5f)
                    .setFontColor(cinzaTexto)
                    .setMarginBottom(4);

            infoGeracao.add("Gerado em: ");
            infoGeracao.add(dataAtual);
            infoGeracao.add("   |   Tipo: ");
            infoGeracao.add(tipoRelatorio == null ? "Geral" : tipoRelatorio.toUpperCase());

            Paragraph infoResponsavel = new Paragraph()
                    .setFont(fonteSubtitulo)
                    .setFontSize(10.5f)
                    .setFontColor(cinzaTexto)
                    .setMarginBottom(0);

            infoResponsavel.add("Responsável: ");
            infoResponsavel.add(responsavel == null || responsavel.isEmpty() ? "Sistema" : responsavel);

            faixaInfo.add(infoGeracao);
            faixaInfo.add(infoResponsavel);
            document.add(faixaInfo);

            Paragraph tituloSecao = new Paragraph("CONSOLIDADO DO RELATÓRIO")
                    .setFont(fonteNegrito)
                    .setFontSize(13)
                    .setFontColor(azulMedio)
                    .setCharacterSpacing(0.8f)
                    .setMarginBottom(5);

            document.add(tituloSecao);

            LineSeparator linhaDourada = new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine(1f))
                    .setStrokeColor(dourado)
                    .setMarginBottom(14);

            document.add(linhaDourada);

            Div caixaConteudo = new Div()
                    .setBackgroundColor(new DeviceRgb(250, 250, 250))
                    .setBorder(new SolidBorder(new DeviceRgb(220, 220, 220), 0.8f))
                    .setPaddingTop(16)
                    .setPaddingBottom(16)
                    .setPaddingLeft(18)
                    .setPaddingRight(18)
                    .setMarginBottom(18);

            String[] linhas = conteudoUltimoRelatorio.split("\\n");

            for (String linha : linhas) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String linhaTratada = linha.trim();

                if (linhaTratada.toLowerCase().contains("relatório")) {
                    Paragraph secao = new Paragraph(linhaTratada.toUpperCase())
                            .setFont(fonteNegrito)
                            .setFontSize(11.5f)
                            .setFontColor(azulMedio)
                            .setMarginTop(10)
                            .setMarginBottom(6);

                    caixaConteudo.add(secao);
                } else if (linhaTratada.contains(":")) {
                    Paragraph item = new Paragraph("▪ " + linhaTratada)
                            .setFont(fonteTextoNormal)
                            .setFontSize(10.2f)
                            .setFontColor(cinzaTexto)
                            .setMarginBottom(4);

                    caixaConteudo.add(item);
                } else {
                    Paragraph texto = new Paragraph(linhaTratada)
                            .setFont(fonteTextoNormal)
                            .setFontSize(10.2f)
                            .setFontColor(cinzaTexto)
                            .setMarginBottom(4);

                    caixaConteudo.add(texto);
                }
            }

            document.add(caixaConteudo);

            Div rodape = new Div()
                    .setBorderTop(new SolidBorder(azulEscuro, 1f))
                    .setPaddingTop(8);

            Paragraph textoRodape = new Paragraph("Sistema Copa do Mundo 2026  •  Relatório gerado automaticamente")
                    .setFont(fonteSubtitulo)
                    .setFontSize(9)
                    .setFontColor(new DeviceRgb(100, 100, 100))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(0);

            rodape.add(textoRodape);
            document.add(rodape);

            document.close();
            return true;

        } catch (Exception e) {
            System.out.println("Erro na estruturação do PDF: " + e.getMessage());
            return false;
        }
    }

    public boolean gerarRelatorio() {
        selecionarTipoRelatorio();

        if (!validarFiltrosRelatorio()) {
            System.out.println("filtros inválidos para gerar relatório.");
            return false;
        }

        dataGeracao = new Date();

        String tipo = tipoRelatorio.trim().toLowerCase();
        StringBuilder relatorio = new StringBuilder();

        if (tipo.contains("usuario") || tipo.contains("usuário")) {
            relatorio.append(gerarRelatorioUsuarios());
        } else if (tipo.contains("partida")) {
            relatorio.append(gerarRelatorioPartidas());
        } else if (tipo.contains("desempenho") || tipo.contains("selecao") || tipo.contains("seleção")) {
            relatorio.append(gerarRelatorioDesempenhoSelecoes());
        } else {
            relatorio.append(gerarRelatorioPartidas());
            relatorio.append("\n\n");
            relatorio.append(gerarRelatorioDesempenhoSelecoes());
        }

        conteudoUltimoRelatorio = relatorio.toString();

        gerado = true;
        registrarGeracaoRelatorio();

        System.out.println("relatório gerado com sucesso.");
        return true;
    }

    public void carregarPartidasReais(List<Partida> partidasReais) {
        partidas.clear();

        if (partidasReais == null || partidasReais.isEmpty()) {
            return;
        }

        for (Partida partida : partidasReais) {
            PartidaRelatorio partidaConvertida = converterPartidaReal(partida);

            if (partidaConvertida != null) {
                adicionarPartida(partidaConvertida);
            }
        }
    }

    public void adicionarPartidaReal(Partida partidaReal) {
        PartidaRelatorio partidaConvertida = converterPartidaReal(partidaReal);

        if (partidaConvertida != null) {
            adicionarPartida(partidaConvertida);
        }
    }

    private PartidaRelatorio converterPartidaReal(Partida partida) {
        if (partida == null) {
            return null;
        }

        String selecaoA = "Seleção A";
        String selecaoB = "Seleção B";
        String fase = "sem fase";
        String status = "sem status";
        String estadio = "sem estádio";
        int golsA = 0;
        int golsB = 0;
        int publico = 0;

        if (partida.getSelecao1() != null && partida.getSelecao1().getPais() != null) {
            selecaoA = partida.getSelecao1().getPais();
        }

        if (partida.getSelecao2() != null && partida.getSelecao2().getPais() != null) {
            selecaoB = partida.getSelecao2().getPais();
        }

        if (partida.getFase() != null) {
            fase = partida.getFase().toString().replace("_", " ").toLowerCase();
        }

        if (partida.getStatus() != null) {
            status = partida.getStatus().toString().replace("_", " ").toLowerCase();
        }

        if (partida.getEstadioPartida() != null) {
            estadio = partida.getEstadioPartida().toString();
        }

        Resultado resultado = partida.getResultado();

        if (resultado != null && resultado.getGolsSelecao1() != -1) {
            golsA = resultado.getGolsSelecao1();
            golsB = resultado.getGolsSelecao2();
            status = "finalizada";
        }

        Date data = converterDataPartida(partida.getDataPartida());

        return new PartidaRelatorio(
                selecaoA,
                selecaoB,
                golsA,
                golsB,
                status,
                fase,
                estadio,
                data,
                publico
        );
    }

    private Date converterDataPartida(String dataTexto) {
        if (dataTexto == null || dataTexto.trim().isEmpty()) {
            return new Date();
        }

        String[] formatos = {"yyyy-MM-dd", "dd/MM/yyyy"};

        for (String formato : formatos) {
            try {
                return new SimpleDateFormat(formato).parse(dataTexto);
            } catch (Exception e) {
                // tenta o próximo formato
            }
        }

        return new Date();
    }

    private String descricaoPartidaDetalhada(PartidaRelatorio partida) {
        if (partida == null) {
            return "partida não encontrada.";
        }

        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

        String data = "sem data";
        if (partida.getDataPartida() != null) {
            data = formatoData.format(partida.getDataPartida());
        }

        String placar;

        if (partida.getStatus() != null && partida.getStatus().equalsIgnoreCase("finalizada")) {
            placar = partida.getGolsSelecaoA() + " x " + partida.getGolsSelecaoB();
        } else {
            placar = "não finalizada";
        }

        return "- " + partida.getSelecaoA() + " x " + partida.getSelecaoB()
                + " | placar: " + placar
                + " | fase: " + partida.getFase()
                + " | status: " + partida.getStatus()
                + " | estádio: " + partida.getEstadio()
                + " | data: " + data;
    }

    public void exibirRelatorioNaTela() {
        if (!gerado) {
            System.out.println("nenhum relatório foi gerado ainda.");
            return;
        }

        System.out.println("id: " + id);
        System.out.println("tipo: " + tipoRelatorio);
        System.out.println("data de geração: " + dataGeracao);
        System.out.println("responsável: " + responsavel);
        System.out.println("número de partidas: " + numeroPartidas);
        System.out.println("público total: " + publicoTotal);
        System.out.println("média de público: " + mediaPublico);
        System.out.println("desempenho das seleções: " + desempenhoSelecoes);
        System.out.println("caminho pdf: " + caminhoPdf);
    }

    public void limparFiltrosRelatorio() {
        tipoRelatorio = null;
        dataInicio = null;
        dataFim = null;
        desempenhoSelecoes = null;
        caminhoPdf = null;
        gerado = false;
    }

    public String exportarRelatorio() {
        if (!gerado) {
            boolean conseguiuGerar = gerarRelatorio();

            if (!conseguiuGerar) {
                System.out.println("não foi possível exportar o relatório.");
                return null;
            }
        }

        /*criarPastaRelatorios();*/

        caminhoPdf = PASTA_RELATORIOS + File.separator + "relatorio_" + id + ".txt";

        /*boolean salvou = salvarTextoEmArquivo(caminhoPdf, conteudoUltimoRelatorio);*/

        /*if (salvou) {
            System.out.println("relatório exportado para: " + caminhoPdf);
            return caminhoPdf;
        }*/

        System.out.println("erro ao exportar relatório.");
        return null;
    }

    public void registrarGeracaoRelatorio() {
        String registro = "relatório " + tipoRelatorio + " gerado em " + dataGeracao;

        if (responsavel != null) {
            registro += " por " + responsavel;
        }

        historicoRelatorios.add(registro);
        /*salvarLinhaNoArquivo(ARQUIVO_HISTORICO, registro);*/
    }

    // relatórios de usuários
    public String gerarRelatorioUsuarios() {
        Map<Usuario.Funcao, Integer> porPerfil = contarUsuariosPorPerfil();
        Map<Usuario.Status, Integer> porStatus = contarUsuariosPorStatus();
        List<Usuario> bloqueados = listarUsuariosBloqueados();
        List<Usuario> semLoginRecente = listarUsuariosSemLoginRecente();

        String relatorio = "relatório de usuários\n";
        relatorio += "total de usuários: " + Usuario.usuarios.size() + "\n";
        relatorio += "usuários por perfil: " + porPerfil + "\n";
        relatorio += "usuários por status: " + porStatus + "\n";
        relatorio += "usuários bloqueados/inativos: " + bloqueados.size() + "\n";
        relatorio += "usuários sem login recente: " + semLoginRecente.size();

        System.out.println(relatorio);
        return relatorio;
    }

    public Map<Usuario.Funcao, Integer> contarUsuariosPorPerfil() {
        Map<Usuario.Funcao, Integer> quantidadePorPerfil = new HashMap<>();

        for (Usuario.Funcao funcao : Usuario.Funcao.values()) {
            quantidadePorPerfil.put(funcao, 0);
        }

        for (Usuario usuario : Usuario.usuarios) {
            Usuario.Funcao funcao = usuario.getFuncao();

            if (funcao != null) {
                quantidadePorPerfil.put(funcao, quantidadePorPerfil.get(funcao) + 1);
            }
        }

        return quantidadePorPerfil;
    }

    public Map<Usuario.Status, Integer> contarUsuariosPorStatus() {
        Map<Usuario.Status, Integer> quantidadePorStatus = new HashMap<>();

        for (Usuario.Status status : Usuario.Status.values()) {
            quantidadePorStatus.put(status, 0);
        }

        for (Usuario usuario : Usuario.usuarios) {
            Usuario.Status status = usuario.getStatus();

            if (status != null) {
                quantidadePorStatus.put(status, quantidadePorStatus.get(status) + 1);
            }
        }

        return quantidadePorStatus;
    }

    public Map<String, Integer> contarPartidasPorStatus() {
        return contarPartidasPorStatus(partidas);
    }

    private Map<String, Integer> contarPartidasPorStatus(List<PartidaRelatorio> listaPartidas) {
        Map<String, Integer> quantidadePorStatus = new HashMap<>();

        for (PartidaRelatorio partida : listaPartidas) {
            String status = partida.getStatus();

            if (status == null || status.trim().isEmpty()) {
                status = "sem status";
            }

            if (!quantidadePorStatus.containsKey(status)) {
                quantidadePorStatus.put(status, 0);
            }

            quantidadePorStatus.put(status, quantidadePorStatus.get(status) + 1);
        }

        return quantidadePorStatus;
    }

    public Map<String, Integer> contarPartidasPorFase() {
        return contarPartidasPorFase(partidas);
    }

    private Map<String, Integer> contarPartidasPorFase(List<PartidaRelatorio> listaPartidas) {
        Map<String, Integer> quantidadePorFase = new HashMap<>();

        for (PartidaRelatorio partida : listaPartidas) {
            String fase = partida.getFase();

            if (fase == null || fase.trim().isEmpty()) {
                fase = "sem fase";
            }

            if (!quantidadePorFase.containsKey(fase)) {
                quantidadePorFase.put(fase, 0);
            }

            quantidadePorFase.put(fase, quantidadePorFase.get(fase) + 1);
        }

        return quantidadePorFase;
    }

    public List<Usuario> listarUsuariosBloqueados() {
        List<Usuario> usuariosBloqueados = new ArrayList<>();

        for (Usuario usuario : Usuario.usuarios) {
            if (usuario.getStatus() == Usuario.Status.INATIVO) {
                usuariosBloqueados.add(usuario);
            }
        }

        return usuariosBloqueados;
    }

    public List<Usuario> listarUsuariosSemLoginRecente() {
        return listarUsuariosSemLoginRecente(30);
    }

    public List<Usuario> listarUsuariosSemLoginRecente(int dias) {
        List<Usuario> usuariosSemLoginRecente = new ArrayList<>();

        long hoje = new Date().getTime();
        long limite = dias * 24L * 60L * 60L * 1000L;

        for (Usuario usuario : Usuario.usuarios) {
            Date ultimoLogin = ultimosLogins.get(usuario);

            if (ultimoLogin == null) {
                usuariosSemLoginRecente.add(usuario);
            } else {
                long diferenca = hoje - ultimoLogin.getTime();

                if (diferenca > limite) {
                    usuariosSemLoginRecente.add(usuario);
                }
            }
        }

        return usuariosSemLoginRecente;
    }

    public void registrarLoginUsuario(Usuario usuario) {
        if (usuario != null) {
            ultimosLogins.put(usuario, new Date());
        }
    }

    // relatórios de partidas
    public void adicionarPartida(PartidaRelatorio partida) {
        if (partida != null) {
            partidas.add(partida);
        }
    }

    public String gerarRelatorioPartidas() {
        List<PartidaRelatorio> partidasFiltradas = filtrarPartidasPorPeriodo();

        numeroPartidas = partidasFiltradas.size();

        int partidasFinalizadas = contarPartidasFinalizadas(partidasFiltradas);
        int totalGols = calcularTotalGols(partidasFiltradas);
        double mediaGols = calcularMediaGolsPorPartidaFinalizada(partidasFiltradas);

        PartidaRelatorio partidaMaisGols = encontrarPartidaComMaisGols(partidasFiltradas);
        PartidaRelatorio maiorGoleada = encontrarMaiorGoleada(partidasFiltradas);

        String relatorio = "relatório de partidas\n";
        relatorio += "número de partidas: " + numeroPartidas + "\n";
        relatorio += "partidas finalizadas: " + partidasFinalizadas + "\n";
        relatorio += "partidas não finalizadas: " + (numeroPartidas - partidasFinalizadas) + "\n";
        relatorio += "total de gols: " + totalGols + "\n";
        relatorio += "média de gols por partida finalizada: " + String.format("%.2f", mediaGols) + "\n";
        relatorio += "partidas por status: " + contarPartidasPorStatus(partidasFiltradas) + "\n";
        relatorio += "partidas por fase: " + contarPartidasPorFase(partidasFiltradas) + "\n";

        if (partidaMaisGols != null) {
            relatorio += "partida com mais gols: " + descricaoPartida(partidaMaisGols) + "\n";
        }

        if (maiorGoleada != null) {
            relatorio += "maior goleada: " + descricaoPartida(maiorGoleada) + "\n";
        }

        relatorio += "\npartidas detalhadas:\n";

        if (partidasFiltradas.isEmpty()) {
            relatorio += "nenhuma partida encontrada para os filtros informados.";
        } else {
            for (PartidaRelatorio partida : partidasFiltradas) {
                relatorio += descricaoPartidaDetalhada(partida) + "\n";
            }
        }

        System.out.println(relatorio);
        return relatorio;
    }

    public List<PartidaRelatorio> filtrarPartidasPorSelecao() {
        return filtrarPartidasPorSelecao(desempenhoSelecoes);
    }

    public List<PartidaRelatorio> filtrarPartidasPorSelecao(String selecao) {
        List<PartidaRelatorio> partidasFiltradas = new ArrayList<>();

        if (selecao == null) {
            return partidasFiltradas;
        }

        for (PartidaRelatorio partida : partidas) {
            if (partida.getSelecaoA() != null && partida.getSelecaoA().equalsIgnoreCase(selecao)) {
                partidasFiltradas.add(partida);
            } else if (partida.getSelecaoB() != null && partida.getSelecaoB().equalsIgnoreCase(selecao)) {
                partidasFiltradas.add(partida);
            }
        }

        return partidasFiltradas;
    }

    public List<PartidaRelatorio> filtrarPartidasPorEstadio() {
        return filtrarPartidasPorEstadio(null);
    }

    public List<PartidaRelatorio> filtrarPartidasPorEstadio(String estadio) {
        List<PartidaRelatorio> partidasFiltradas = new ArrayList<>();

        if (estadio == null) {
            return new ArrayList<>(partidas);
        }

        for (PartidaRelatorio partida : partidas) {
            if (partida.getEstadio() != null && partida.getEstadio().equalsIgnoreCase(estadio)) {
                partidasFiltradas.add(partida);
            }
        }

        return partidasFiltradas;
    }

    public List<PartidaRelatorio> filtrarPartidasPorPeriodo() {
        List<PartidaRelatorio> partidasFiltradas = new ArrayList<>();

        for (PartidaRelatorio partida : partidas) {
            Date dataPartida = partida.getDataPartida();

            if (dataInicio != null && dataPartida != null && dataPartida.before(dataInicio)) {
                continue;
            }

            if (dataFim != null && dataPartida != null && dataPartida.after(dataFim)) {
                continue;
            }

            partidasFiltradas.add(partida);
        }

        return partidasFiltradas;
    }

    // relatório de desempenho das seleções
    public String gerarRelatorioDesempenhoSelecoes() {
        if (desempenhoSelecoes == null || desempenhoSelecoes.trim().isEmpty()) {
            String mensagem = "nenhuma seleção foi informada para gerar desempenho.";
            System.out.println(mensagem);
            return mensagem;
        }

        String relatorio = gerarDesempenhoDeUmaSelecao(desempenhoSelecoes);

        System.out.println(relatorio);
        return relatorio;
    }

    public int calcularVitorias() {
        return calcularVitorias(desempenhoSelecoes);
    }

    public int calcularVitorias(String selecao) {
        int vitorias = 0;

        for (PartidaRelatorio partida : considerarApenasPartidasFinalizadas()) {
            if (selecaoVenceu(partida, selecao)) {
                vitorias++;
            }
        }

        return vitorias;
    }

    public int calcularEmpates() {
        return calcularEmpates(desempenhoSelecoes);
    }

    public int calcularEmpates(String selecao) {
        int empates = 0;

        for (PartidaRelatorio partida : considerarApenasPartidasFinalizadas()) {
            if (participouDaPartida(partida, selecao) &&
                    partida.getGolsSelecaoA() == partida.getGolsSelecaoB()) {
                empates++;
            }
        }

        return empates;
    }

    public int calcularDerrotas() {
        return calcularDerrotas(desempenhoSelecoes);
    }

    public int calcularDerrotas(String selecao) {
        int derrotas = 0;

        for (PartidaRelatorio partida : considerarApenasPartidasFinalizadas()) {
            if (selecaoPerdeu(partida, selecao)) {
                derrotas++;
            }
        }

        return derrotas;
    }

    public int calcularGolsPro() {
        return calcularGolsPro(desempenhoSelecoes);
    }

    public int calcularGolsPro(String selecao) {
        int golsPro = 0;

        for (PartidaRelatorio partida : considerarApenasPartidasFinalizadas()) {
            if (partida.getSelecaoA() != null && partida.getSelecaoA().equalsIgnoreCase(selecao)) {
                golsPro += partida.getGolsSelecaoA();
            } else if (partida.getSelecaoB() != null && partida.getSelecaoB().equalsIgnoreCase(selecao)) {
                golsPro += partida.getGolsSelecaoB();
            }
        }

        return golsPro;
    }

    public int calcularGolsContra() {
        return calcularGolsContra(desempenhoSelecoes);
    }

    public int calcularGolsContra(String selecao) {
        int golsContra = 0;

        for (PartidaRelatorio partida : considerarApenasPartidasFinalizadas()) {
            if (partida.getSelecaoA() != null && partida.getSelecaoA().equalsIgnoreCase(selecao)) {
                golsContra += partida.getGolsSelecaoB();
            } else if (partida.getSelecaoB() != null && partida.getSelecaoB().equalsIgnoreCase(selecao)) {
                golsContra += partida.getGolsSelecaoA();
            }
        }

        return golsContra;
    }

    public int calcularSaldoGols() {
        return calcularSaldoGols(desempenhoSelecoes);
    }

    public int calcularSaldoGols(String selecao) {
        return calcularGolsPro(selecao) - calcularGolsContra(selecao);
    }

    public int calcularPontuacao() {
        return calcularPontuacao(desempenhoSelecoes);
    }

    public int calcularPontuacao(String selecao) {
        int vitorias = calcularVitorias(selecao);
        int empates = calcularEmpates(selecao);

        return (vitorias * 3) + empates;
    }

    public List<PartidaRelatorio> considerarApenasPartidasFinalizadas() {
        List<PartidaRelatorio> partidasFinalizadas = new ArrayList<>();

        for (PartidaRelatorio partida : partidas) {
            if (partida.getStatus() != null && partida.getStatus().equalsIgnoreCase("finalizada")) {
                partidasFinalizadas.add(partida);
            }
        }

        return partidasFinalizadas;
    }

    private boolean participouDaPartida(PartidaRelatorio partida, String selecao) {
        if (partida == null || selecao == null) {
            return false;
        }
        if (partida.getSelecaoA() != null && partida.getSelecaoA().equalsIgnoreCase(selecao)) {
            return true;
        }
        if (partida.getSelecaoB() != null && partida.getSelecaoB().equalsIgnoreCase(selecao)) {
            return true;
        }

        return false;
    }

    private boolean selecaoVenceu(PartidaRelatorio partida, String selecao) {
        if (!participouDaPartida(partida, selecao)) {
            return false;
        }
        if (partida.getSelecaoA().equalsIgnoreCase(selecao)) {
            return partida.getGolsSelecaoA() > partida.getGolsSelecaoB();
        }
        if (partida.getSelecaoB().equalsIgnoreCase(selecao)) {
            return partida.getGolsSelecaoB() > partida.getGolsSelecaoA();
        }

        return false;
    }

    private boolean selecaoPerdeu(PartidaRelatorio partida, String selecao) {
        if (!participouDaPartida(partida, selecao)) {
            return false;
        }
        if (partida.getSelecaoA().equalsIgnoreCase(selecao)) {
            return partida.getGolsSelecaoA() < partida.getGolsSelecaoB();
        }
        if (partida.getSelecaoB().equalsIgnoreCase(selecao)) {
            return partida.getGolsSelecaoB() < partida.getGolsSelecaoA();
        }

        return false;
    }

    private String gerarDesempenhoDeUmaSelecao(String selecao) {
        int jogos = calcularJogos(selecao);
        int vitorias = calcularVitorias(selecao);
        int empates = calcularEmpates(selecao);
        int derrotas = calcularDerrotas(selecao);
        int golsPro = calcularGolsPro(selecao);
        int golsContra = calcularGolsContra(selecao);
        int saldoGols = calcularSaldoGols(selecao);
        int pontuacao = calcularPontuacao(selecao);
        double aproveitamento = calcularAproveitamento(selecao);
        double mediaGolsPro = calcularMediaGolsPro(selecao);
        double mediaGolsContra = calcularMediaGolsContra(selecao);

        String relatorio = "desempenho da seleção: " + selecao + "\n";
        relatorio += "jogos: " + jogos + "\n";
        relatorio += "vitórias: " + vitorias + "\n";
        relatorio += "empates: " + empates + "\n";
        relatorio += "derrotas: " + derrotas + "\n";
        relatorio += "gols pró: " + golsPro + "\n";
        relatorio += "gols contra: " + golsContra + "\n";
        relatorio += "saldo de gols: " + saldoGols + "\n";
        relatorio += "pontuação: " + pontuacao + "\n";
        relatorio += "aproveitamento: " + String.format("%.2f", aproveitamento) + "%\n";
        relatorio += "média de gols pró por jogo: " + String.format("%.2f", mediaGolsPro) + "\n";
        relatorio += "média de gols contra por jogo: " + String.format("%.2f", mediaGolsContra);

        return relatorio;
    }

    private int calcularJogos(String selecao) {
        int jogos = 0;

        for (PartidaRelatorio partida : considerarApenasPartidasFinalizadas()) {
            if (participouDaPartida(partida, selecao)) {
                jogos++;
            }
        }

        return jogos;
    }

    private double calcularAproveitamento(String selecao) {
        int jogos = calcularJogos(selecao);

        if (jogos == 0) {
            return 0;
        }

        int pontuacao = calcularPontuacao(selecao);
        return (pontuacao * 100.0) / (jogos * 3.0);
    }

    private double calcularMediaGolsPro(String selecao) {
        int jogos = calcularJogos(selecao);

        if (jogos == 0) {
            return 0;
        }

        return (double) calcularGolsPro(selecao) / jogos;
    }

    private double calcularMediaGolsContra(String selecao) {
        int jogos = calcularJogos(selecao);

        if (jogos == 0) {
            return 0;
        }

        return (double) calcularGolsContra(selecao) / jogos;
    }

    private int contarPartidasFinalizadas(List<PartidaRelatorio> listaPartidas) {
        int total = 0;

        for (PartidaRelatorio partida : listaPartidas) {
            if (partida.getStatus() != null && partida.getStatus().equalsIgnoreCase("finalizada")) {
                total++;
            }
        }

        return total;
    }

    private int calcularTotalGols(List<PartidaRelatorio> listaPartidas) {
        int totalGols = 0;

        for (PartidaRelatorio partida : listaPartidas) {
            if (partida.getStatus() != null && partida.getStatus().equalsIgnoreCase("finalizada")) {
                totalGols += partida.getGolsSelecaoA() + partida.getGolsSelecaoB();
            }
        }

        return totalGols;
    }

    private double calcularMediaGolsPorPartidaFinalizada(List<PartidaRelatorio> listaPartidas) {
        int finalizadas = contarPartidasFinalizadas(listaPartidas);

        if (finalizadas == 0) {
            return 0;
        }

        return (double) calcularTotalGols(listaPartidas) / finalizadas;
    }

    private PartidaRelatorio encontrarPartidaComMaisGols(List<PartidaRelatorio> listaPartidas) {
        PartidaRelatorio partidaMaisGols = null;
        int maiorQuantidadeGols = -1;

        for (PartidaRelatorio partida : listaPartidas) {
            if (partida.getStatus() != null && partida.getStatus().equalsIgnoreCase("finalizada")) {
                int totalGols = partida.getGolsSelecaoA() + partida.getGolsSelecaoB();

                if (totalGols > maiorQuantidadeGols) {
                    maiorQuantidadeGols = totalGols;
                    partidaMaisGols = partida;
                }
            }
        }

        return partidaMaisGols;
    }

    private PartidaRelatorio encontrarMaiorGoleada(List<PartidaRelatorio> listaPartidas) {
        PartidaRelatorio maiorGoleada = null;
        int maiorDiferenca = -1;

        for (PartidaRelatorio partida : listaPartidas) {
            if (partida.getStatus() != null && partida.getStatus().equalsIgnoreCase("finalizada")) {
                int diferenca = Math.abs(partida.getGolsSelecaoA() - partida.getGolsSelecaoB());

                if (diferenca > maiorDiferenca) {
                    maiorDiferenca = diferenca;
                    maiorGoleada = partida;
                }
            }
        }

        return maiorGoleada;
    }

    private String descricaoPartida(PartidaRelatorio partida) {
        if (partida == null) {
            return "não encontrada";
        }

        return partida.getSelecaoA() + " " +
                partida.getGolsSelecaoA() + " x " +
                partida.getGolsSelecaoB() + " " +
                partida.getSelecaoB();
    }

    private List<String> listarSelecoesDasPartidas() {
        List<String> selecoes = new ArrayList<>();

        for (PartidaRelatorio partida : partidas) {
            adicionarSelecaoSeNaoExistir(selecoes, partida.getSelecaoA());
            adicionarSelecaoSeNaoExistir(selecoes, partida.getSelecaoB());
        }

        return selecoes;
    }

    private void adicionarSelecaoSeNaoExistir(List<String> selecoes, String selecao) {
        if (selecao == null || selecao.trim().isEmpty()) {
            return;
        }

        for (String nome : selecoes) {
            if (nome.equalsIgnoreCase(selecao)) {
                return;
            }
        }

        selecoes.add(selecao);
    }

    public void carregarPartidasTeste() {
        if (!partidas.isEmpty()) {
            return;
        }

        adicionarPartida(new PartidaRelatorio(
                "Brasil",
                "Argentina",
                2,
                1,
                "finalizada",
                "grupos",
                "Estádio Nacional",
                new Date(),
                0
        ));

        adicionarPartida(new PartidaRelatorio(
                "França",
                "Alemanha",
                3,
                3,
                "finalizada",
                "grupos",
                "Arena Central",
                new Date(),
                0
        ));

        adicionarPartida(new PartidaRelatorio(
                "Brasil",
                "França",
                1,
                0,
                "finalizada",
                "quartas de final",
                "Estádio Nacional",
                new Date(),
                0
        ));

        adicionarPartida(new PartidaRelatorio(
                "Argentina",
                "Alemanha",
                0,
                2,
                "finalizada",
                "quartas de final",
                "Arena Central",
                new Date(),
                0
        ));

        adicionarPartida(new PartidaRelatorio(
                "Brasil",
                "Alemanha",
                4,
                1,
                "finalizada",
                "semifinal",
                "Estádio Internacional",
                new Date(),
                0
        ));

        adicionarPartida(new PartidaRelatorio(
                "França",
                "Argentina",
                0,
                0,
                "agendada",
                "disputa de terceiro lugar",
                "Arena Central",
                new Date(),
                0
        ));
    }
}