package br.ufrn.manageit.infra.exception;

public class BuscaInvalidaException extends RuntimeException {
    public BuscaInvalidaException(String message) {
        super(message);
    }
}
