package br.com.fiap.pedidos.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String productName) {
        super("Produto '" + productName + "' está fora de estoque.");
    }
}

