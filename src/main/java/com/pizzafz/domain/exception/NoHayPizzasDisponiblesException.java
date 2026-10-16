package com.pizzafz.domain.exception;

public class NoHayPizzasDisponiblesException extends RuntimeException {
    public NoHayPizzasDisponiblesException() {
        super("No hay pizzas disponibles");
    }

    public NoHayPizzasDisponiblesException(String mensaje) {
        super(mensaje);
    }
}
