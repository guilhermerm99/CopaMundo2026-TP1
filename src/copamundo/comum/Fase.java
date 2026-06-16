package copamundo.comum;

public enum Fase {
    GRUPOS("Fase de Grupos"),
    OITAVAS("Oitavas de Final"),
    QUARTAS("Quartas de Final"),
    SEMIFINAL("Semifinal"),
    FINAL("Final");

    private final String textosFase;

    Fase(String textosFase) {

        this.textosFase = textosFase;
    }

    @Override
    public String toString() {

        return textosFase;
    }
}
