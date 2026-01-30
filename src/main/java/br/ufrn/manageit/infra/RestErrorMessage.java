package br.ufrn.manageit.infra;

import java.time.LocalDateTime;

public class RestErrorMessage {
    private int status;
    private String mensagem;
    private LocalDateTime timestamp;

    public RestErrorMessage(int status, String mensagem, LocalDateTime timestamp) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
    }

    public RestErrorMessage(int status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {return status;}
    public String getMensagem() { return mensagem; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
