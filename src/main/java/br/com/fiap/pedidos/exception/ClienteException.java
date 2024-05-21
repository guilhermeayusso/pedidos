package br.com.fiap.pedidos.exception;

import org.springframework.http.HttpStatus;

public class ClienteException extends RuntimeException {
    private final HttpStatus status;

    public ClienteException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
