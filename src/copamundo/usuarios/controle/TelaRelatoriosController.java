package copamundo.usuarios.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TelaRelatoriosController {
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
            relatorio.append(gerarRelatorioUsuarios());
            relatorio.append("\n\n");
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

        criarPastaRelatorios();

        caminhoPdf = PASTA_RELATORIOS + File.separator + "relatorio_" + id + ".txt";

        boolean salvou = salvarTextoEmArquivo(caminhoPdf, conteudoUltimoRelatorio);

        if (salvou) {
            System.out.println("relatório exportado para: " + caminhoPdf);
            return caminhoPdf;
        }

        System.out.println("erro ao exportar relatório.");
        return null;
    }

    public void registrarGeracaoRelatorio() {
        String registro = "relatório " + tipoRelatorio + " gerado em " + dataGeracao;

        if (responsavel != null) {
            registro += " por " + responsavel;
        }

        historicoRelatorios.add(registro);
        salvarLinhaNoArquivo(ARQUIVO_HISTORICO, registro);
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
        publicoTotal = 0;

        for (PartidaRelatorio partida : partidasFiltradas) {
            publicoTotal += partida.getPublico();
        }

        if (numeroPartidas > 0) {
            mediaPublico = (double) publicoTotal / numeroPartidas;
        } else {
            mediaPublico = 0;
        }

        String relatorio = "relatório de partidas\n";
        relatorio += "número de partidas: " + numeroPartidas + "\n";
        relatorio += "público total: " + publicoTotal + "\n";
        relatorio += "média de público: " + mediaPublico + "\n";
        relatorio += "partidas por status: " + contarPartidasPorStatus() + "\n";
        relatorio += "partidas por fase: " + contarPartidasPorFase();

        System.out.println(relatorio);
        return relatorio;
    }

    public Map<String, Integer> contarPartidasPorStatus() {
        Map<String, Integer> quantidadePorStatus = new HashMap<>();

        for (PartidaRelatorio partida : partidas) {
            String status = partida.getStatus();

            if (status == null) {
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
        Map<String, Integer> quantidadePorFase = new HashMap<>();

        for (PartidaRelatorio partida : partidas) {
            String fase = partida.getFase();

            if (fase == null) {
                fase = "sem fase";
            }

            if (!quantidadePorFase.containsKey(fase)) {
                quantidadePorFase.put(fase, 0);
            }

            quantidadePorFase.put(fase, quantidadePorFase.get(fase) + 1);
        }

        return quantidadePorFase;
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

        int vitorias = calcularVitorias();
        int empates = calcularEmpates();
        int derrotas = calcularDerrotas();
        int golsPro = calcularGolsPro();
        int golsContra = calcularGolsContra();
        int saldoGols = calcularSaldoGols();
        int pontuacao = calcularPontuacao();

        String relatorio = "relatório de desempenho da seleção: " + desempenhoSelecoes + "\n";
        relatorio += "vitórias: " + vitorias + "\n";
        relatorio += "empates: " + empates + "\n";
        relatorio += "derrotas: " + derrotas + "\n";
        relatorio += "gols pró: " + golsPro + "\n";
        relatorio += "gols contra: " + golsContra + "\n";
        relatorio += "saldo de gols: " + saldoGols + "\n";
        relatorio += "pontuação: " + pontuacao;

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
}