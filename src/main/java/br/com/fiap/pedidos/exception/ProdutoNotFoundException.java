package br.com.fiap.pedidos.exception;

import org.springframework.http.HttpStatus;

public class ProdutoNotFoundException extends ProdutoException {
    public ProdutoNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
