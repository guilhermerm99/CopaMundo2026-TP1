package copamundo.comum;

public class Partida {
    private String dataPartida;
    private String horarioPartida;
    private Selecao selecao1;
    private Selecao selecao2;
    private Estadio estadioPartida;
    private Fase fase;
    private StatusPartida status;
    private Resultado resultado;
    private Arbitros arbitro;

    public Partida(String dataPartida, String horarioPartida, Estadio estadioPartida, Selecao selecao1, Selecao selecao2, Fase fase, StatusPartida status) {
        this.dataPartida = dataPartida;
        this.horarioPartida = horarioPartida;
        this.estadioPartida = estadioPartida;
        this.selecao1 = selecao1;
        this.selecao2 = selecao2;
        this.fase = fase;
        this.status = status;
    }

    public String getDataPartida() {
        return dataPartida;
    }

    public void setDataPartida(String dataPartida) {
        this.dataPartida = dataPartida;
    }

    public Estadio getEstadioPartida() {
        return estadioPartida;
    }

    public void setEstadioPartida(Estadio estadioPartida) {
        this.estadioPartida = estadioPartida;
    }

    public Fase getFasePartida() {
        return fase;
    }

    public void setFasePartida(Fase fase) {
        this.fase = fase;
    }

    public String getHorarioPartida() {
        return horarioPartida;
    }

    public void setHorarioPartida(String horarioPartida) {
        this.horarioPartida = horarioPartida;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public Selecao getSelecao1() {
        return selecao1;
    }

    public void setSelecao1(Selecao selecao1) {
        this.selecao1 = selecao1;
    }

    public Selecao getSelecao2() {
        return selecao2;
    }

    public void setSelecao2(Selecao selecao2) {
        this.selecao2 = selecao2;
    }

    public StatusPartida getStatusPartida() {
        return status;
    }

    public void setStatusPartida(StatusPartida status) {
        this.status = status;
    }

    public Arbitros getArbitro() { return arbitro; }

    public void setArbitro(Arbitros arbitro) { this.arbitro = arbitro; }
}
