package br.com.escola.armarios.exception;

/**
 * Lançada quando a reserva procurada não existe. Vira HTTP 404 Not Found.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
