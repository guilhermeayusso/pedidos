package br.com.fiap.pedidos.exception;

import org.springframework.http.HttpStatus;

public class ClienteNotFoundException extends ClienteException {
    public ClienteNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
