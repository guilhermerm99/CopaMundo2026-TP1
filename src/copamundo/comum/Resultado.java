package copamundo.comum;

public class Resultado {
    private Partida partida;
    private int golsSelecao1;
    private int golsSelecao2;
    private int faltasSelecao1;
    private int faltasSelecao2;
    private int vermelhosSelecao1;
    private int vermelhosSelecao2;
    private int amarelosSelecao1;
    private int amarelosSelecao2;
    private float posseSelecao1;
    private float posseSelecao2;
    private int finalizacoesSelecao1;
    private int finalizacoesSelecao2;
    private int escanteiosSelecao1;
    private int escanteiosSelecao2;
    private int impedimentosSelecao1;
    private int impedimentosSelecao2;

    public Resultado(Partida partida, int golsSelecao1, int golsSelecao2, int faltasSelecao1, int faltasSelecao2, int vermelhosSelecao1, int vermelhosSelecao2, int amarelosSelecao1, int amarelosSelecao2, float posseSelecao1, float posseSelecao2, int finalizacoesSelecao1, int finalizacoesSelecao2, int escanteiosSelecao1, int escanteiosSelecao2, int impedimentosSelecao1, int impedimentosSelecao2) {
        this.partida = partida;
        this.golsSelecao1 = golsSelecao1;
        this.golsSelecao2 = golsSelecao2;
        this.faltasSelecao1 = faltasSelecao1;
        this.faltasSelecao2 = faltasSelecao2;
        this.vermelhosSelecao1 = vermelhosSelecao1;
        this.vermelhosSelecao2 = vermelhosSelecao2;
        this.amarelosSelecao1 = amarelosSelecao1;
        this.amarelosSelecao2 = amarelosSelecao2;
        this.posseSelecao1 = posseSelecao1;
        this.posseSelecao2 = posseSelecao2;
        this.finalizacoesSelecao1 = finalizacoesSelecao1;
        this.finalizacoesSelecao2 = finalizacoesSelecao2;
        this.escanteiosSelecao1 = escanteiosSelecao1;
        this.escanteiosSelecao2 = escanteiosSelecao2;
        this.impedimentosSelecao1 = impedimentosSelecao1;
        this.impedimentosSelecao2 = impedimentosSelecao2;
    }

    public int getAmarelosSelecao1() {
        return amarelosSelecao1;
    }

    public void setAmarelosSelecao1(int amarelosSelecao1) {
        this.amarelosSelecao1 = amarelosSelecao1;
    }

    public int getAmarelosSelecao2() {
        return amarelosSelecao2;
    }

    public void setAmarelosSelecao2(int amarelosSelecao2) {
        this.amarelosSelecao2 = amarelosSelecao2;
    }

    public int getEscanteiosSelecao1() {
        return escanteiosSelecao1;
    }

    public void setEscanteiosSelecao1(int escanteiosSelecao1) {
        this.escanteiosSelecao1 = escanteiosSelecao1;
    }

    public int getEscanteiosSelecao2() {
        return escanteiosSelecao2;
    }

    public void setEscanteiosSelecao2(int escanteiosSelecao2) {
        this.escanteiosSelecao2 = escanteiosSelecao2;
    }

    public int getFaltasSelecao1() {
        return faltasSelecao1;
    }

    public void setFaltasSelecao1(int faltasSelecao1) {
        this.faltasSelecao1 = faltasSelecao1;
    }

    public int getFaltasSelecao2() {
        return faltasSelecao2;
    }

    public void setFaltasSelecao2(int faltasSelecao2) {
        this.faltasSelecao2 = faltasSelecao2;
    }

    public int getFinalizacoesSelecao1() {
        return finalizacoesSelecao1;
    }

    public void setFinalizacoesSelecao1(int finalizacoesSelecao1) {
        this.finalizacoesSelecao1 = finalizacoesSelecao1;
    }

    public int getFinalizacoesSelecao2() {
        return finalizacoesSelecao2;
    }

    public void setFinalizacoesSelecao2(int finalizacoesSelecao2) {
        this.finalizacoesSelecao2 = finalizacoesSelecao2;
    }

    public int getGolsSelecao1() {
        return golsSelecao1;
    }

    public void setGolsSelecao1(int golsSelecao1) {
        this.golsSelecao1 = golsSelecao1;
    }

    public int getGolsSelecao2() {
        return golsSelecao2;
    }

    public void setGolsSelecao2(int golsSelecao2) {
        this.golsSelecao2 = golsSelecao2;
    }

    public int getImpedimentosSelecao1() {
        return impedimentosSelecao1;
    }

    public void setImpedimentosSelecao1(int impedimentosSelecao1) {
        this.impedimentosSelecao1 = impedimentosSelecao1;
    }

    public int getImpedimentosSelecao2() {
        return impedimentosSelecao2;
    }

    public void setImpedimentosSelecao2(int impedimentosSelecao2) {
        this.impedimentosSelecao2 = impedimentosSelecao2;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public float getPosseSelecao1() {
        return posseSelecao1;
    }

    public void setPosseSelecao1(float posseSelecao1) {
        this.posseSelecao1 = posseSelecao1;
    }

    public float getPosseSelecao2() {
        return posseSelecao2;
    }

    public void setPosseSelecao2(float posseSelecao2) {
        this.posseSelecao2 = posseSelecao2;
    }

    public int getVermelhosSelecao1() {
        return vermelhosSelecao1;
    }

    public void setVermelhosSelecao1(int vermelhosSelecao1) {
        this.vermelhosSelecao1 = vermelhosSelecao1;
    }

    public int getVermelhosSelecao2() {
        return vermelhosSelecao2;
    }

    public void setVermelhosSelecao2(int vermelhosSelecao2) {
        this.vermelhosSelecao2 = vermelhosSelecao2;
    }
}
