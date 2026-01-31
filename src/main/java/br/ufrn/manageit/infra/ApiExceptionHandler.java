package br.ufrn.manageit.infra;

import br.ufrn.manageit.infra.exception.BuscaInvalidaException;
import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import br.ufrn.manageit.infra.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RegraNegocioException.class)
    public RestErrorMessage handleBusinessViolation(RegraNegocioException ex) {
        return new RestErrorMessage(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public RestErrorMessage handleNotFound(RecursoNaoEncontradoException ex) {
        return new RestErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado. Verifique se digitou as " +
                        "informações corretamente. " + ex.getMessage()
        );
    }

    @ExceptionHandler(BuscaInvalidaException.class)
    public RestErrorMessage handleBadRequest(BuscaInvalidaException ex) {
        return new RestErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                "A busca não pôde ser concluída. " + ex.getMessage()
        );
    }

}
