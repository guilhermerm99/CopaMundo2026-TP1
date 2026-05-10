package copamundo.usuarios.controle;
import java.util.Date;

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
}