package br.com.fiap.pedidos.exception;

import org.springframework.http.HttpStatus;

public class ClienteServerException extends ClienteException {
    public ClienteServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}