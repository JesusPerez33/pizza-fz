package com.pizzafz.domain.exception;

public class PizzaAgotadaException extends RuntimeException {
    public PizzaAgotadaException() {
        super("Pizza agotada");
    }

    public PizzaAgotadaException(String mensaje) {
        super(mensaje);
    }
}
