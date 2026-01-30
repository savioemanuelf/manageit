package br.ufrn.manageit.infra.exception;

public class BuscaInvalida extends RuntimeException {
    public BuscaInvalida(String message) {
        super(message);
    }
}
