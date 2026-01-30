package br.ufrn.manageit.infra;

import br.ufrn.manageit.infra.exception.BuscaInvalida;
import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public RestErrorMessage handleNotFound(RecursoNaoEncontradoException ex) {
        return new RestErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado. Verifique se digitou as " +
                        "informações corretamente. " + ex.getMessage()
        );
    }

    @ExceptionHandler(BuscaInvalida.class)
    public RestErrorMessage handleNotFound(BuscaInvalida ex) {
        return new RestErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                "A busca não pôde ser concluída. " + ex.getMessage()
        );
    }

}
