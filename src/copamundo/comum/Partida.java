package copamundo.comum;
import copamundo.estadios.modelo.Arbitro;
import copamundo.estadios.modelo.Estadio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Partida {
    private final String id;
    private String dataPartida;
    private String horarioPartida;
    private Selecao selecao1;
    private Selecao selecao2;
    private Estadio estadioPartida;
    private Fase fase;
    private StatusPartida status;
    private Resultado resultado;
    private Arbitro arbitro;

    public Partida(String dataPartida, String horarioPartida, Estadio estadioPartida, Selecao selecao1, Selecao selecao2, Fase fase, StatusPartida status) {
        this.id = UUID.randomUUID().toString();
        this.dataPartida = dataPartida;
        this.horarioPartida = horarioPartida;
        this.estadioPartida = estadioPartida;
        this.selecao1 = selecao1;
        this.selecao2 = selecao2;
        this.fase = fase;
        this.status = status;
    }

    public Partida(Selecao selecao1, Selecao selecao2, Estadio estadioPartida, LocalDateTime dataHora) {
        this(
                dataHora.toLocalDate().toString(),
                dataHora.toLocalTime().toString(),
                estadioPartida,
                selecao1,
                selecao2,
                Fase.GRUPOS,
                StatusPartida.AGENDADA
        );
    }


    public String getId() {
        return id;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public void setStatus(StatusPartida status) {
        this.status = status;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
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

    public Estadio getEstadio() {
        return estadioPartida;
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

    public Selecao getMandante() {
        return selecao1;
    }

    public Selecao getVisitante() {
        return selecao2;
    }

    public StatusPartida getStatusPartida() {
        return status;
    }

    public void setStatusPartida(StatusPartida status) {
        this.status = status;
    }

    public Arbitro getArbitro() { return arbitro; }

    public void setArbitro(Arbitro arbitro) { this.arbitro = arbitro; }

    public Arbitro getArbitroPrincipal() {
        return arbitro;
    }

    public void setArbitroPrincipal(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    public String getDescricao() {
        return selecao1.getPais() + " x " + selecao2.getPais();
    }

    public String getDataHora() {
        return dataPartida + " " + horarioPartida;
    }

    public boolean aconteceNoMesmoEstadioEHorario(Partida outra) {
        return Objects.equals(estadioPartida, outra.estadioPartida)
                && Objects.equals(dataPartida, outra.dataPartida)
                && Objects.equals(horarioPartida, outra.horarioPartida);
    }

    @Override
    public String toString() {
        return getDescricao() + " - " + getDataHora();
    }
}
