package copamundo.estadios.excecoes;

public class RegraNegocioException extends Exception {
    private static final long serialVersionUID = 1L;

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
