package copamundo.estadios.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public final class DesignacaoArbitro implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String jogo;
    private LocalDate data;

    private Arbitro arbitro;
    private Estadio estadio;

    // Construtor vazio
    public DesignacaoArbitro() {
    }

    // Construtor completo
    public DesignacaoArbitro(int id, String jogo, LocalDate data,
                             Arbitro arbitro, Estadio estadio) {

        this.id = id;
        this.jogo = jogo;
        this.data = data;
        this.arbitro = arbitro;
        this.estadio = estadio;
    }

    // GETTERS

    public int getId() {
        return id;
    }

    public String getJogo() {
        return jogo;
    }

    public LocalDate getData() {
        return data;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    // SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setJogo(String jogo) {
        this.jogo = jogo;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }
}
