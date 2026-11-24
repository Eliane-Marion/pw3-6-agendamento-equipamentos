package br.com.escola.armarios.exception;

/**
 * Lançada quando os dados enviados estão incompletos ou inválidos
 * (ex.: data final antes da data inicial). Vira HTTP 400 Bad Request.
 */
public class DadosInvalidosException extends RuntimeException {

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}
