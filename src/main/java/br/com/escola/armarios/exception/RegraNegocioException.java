package br.com.escola.armarios.exception;

/**
 * Lançada quando uma regra de negócio é violada (ex.: conflito de reserva).
 * O GlobalExceptionHandler transforma esta exceção em HTTP 409 Conflict.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
