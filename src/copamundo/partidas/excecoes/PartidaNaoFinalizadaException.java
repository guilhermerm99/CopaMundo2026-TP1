package copamundo.partidas.excecoes;

public class PartidaNaoFinalizadaException extends RuntimeException {
    public PartidaNaoFinalizadaException(String message) {
        super(message);
    }
}
