package br.ufrn.manageit.infra;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class RestErrorMessage {
    private HttpStatus status;
    private String mensagem;
    private LocalDateTime timestamp;

    public RestErrorMessage(HttpStatus status, String mensagem, LocalDateTime timestamp) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
    }

    public RestErrorMessage(HttpStatus status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    public HttpStatus getStatus() {return status;}
    public String getMensagem() { return mensagem; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
