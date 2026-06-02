package copamundo.partidas.excecoes;

public class PartidaNaoEncontradaException extends RuntimeException {

    public PartidaNaoEncontradaException(String message) {
        super(message);
    }
}
