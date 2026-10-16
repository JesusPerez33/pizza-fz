package com.pizzafz.domain.exception;

public class ProductoNoEncontradoEnCarritoException extends RuntimeException {
    public ProductoNoEncontradoEnCarritoException() {
        super("El producto no se encuentra en el carrito");
    }

    public ProductoNoEncontradoEnCarritoException(String mensaje) {
        super(mensaje);
    }
}
