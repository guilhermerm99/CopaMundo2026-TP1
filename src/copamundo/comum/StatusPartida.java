package copamundo.comum;

public enum StatusPartida {
    AGENDADA("Agendada"),
    EM_ANDAMENTO("Em andamento"),
    FINALIZADA("Finalizada");

    private final String textosStatusPartida;

    StatusPartida(String textosFase) {

        this.textosStatusPartida = textosFase;
    }

    @Override
    public String toString() {

        return textosStatusPartida;
    }
}
