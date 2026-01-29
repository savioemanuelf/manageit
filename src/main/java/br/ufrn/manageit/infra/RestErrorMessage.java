package br.ufrn.manageit.infra;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class RestErrorMessage {
    private HttpStatus status;
    private String message;
    private LocalDateTime timestamp;

    public RestErrorMessage(HttpStatus status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public RestErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public HttpStatus getStatus() {return status;}
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
