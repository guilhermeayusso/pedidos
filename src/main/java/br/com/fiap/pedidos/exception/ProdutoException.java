package br.com.fiap.pedidos.exception;

import org.springframework.http.HttpStatus;

public class ProdutoException extends RuntimeException {
    private final HttpStatus status;

    public ProdutoException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
