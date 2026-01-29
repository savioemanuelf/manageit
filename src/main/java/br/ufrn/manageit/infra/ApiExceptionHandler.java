package br.ufrn.manageit.infra;

import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public RestErrorMessage handleNotFound(RecursoNaoEncontradoException ex) {
        return new RestErrorMessage(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado. Verifique se digitou as " +
                        "informações corretamente. " + ex.getMessage()
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestErrorMessage handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return new RestErrorMessage(
                HttpStatus.METHOD_NOT_ALLOWED,
                "O método HTTP utilizado não é suportado para esse endpoint. " + ex.getMessage()
        );
    }
}
