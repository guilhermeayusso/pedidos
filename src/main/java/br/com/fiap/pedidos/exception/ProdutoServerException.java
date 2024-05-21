package br.com.fiap.pedidos.exception;

import org.springframework.http.HttpStatus;

public class ProdutoServerException extends ProdutoException {
    public ProdutoServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
