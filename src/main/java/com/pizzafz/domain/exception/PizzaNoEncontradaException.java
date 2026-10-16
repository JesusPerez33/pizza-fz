package com.pizzafz.domain.exception;

public class PizzaNoEncontradaException extends RuntimeException {
    public PizzaNoEncontradaException() {
        super("Pizza no encontrada");
    }

    public PizzaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
