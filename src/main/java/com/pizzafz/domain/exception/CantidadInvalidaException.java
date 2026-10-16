package com.pizzafz.domain.exception;

public class CantidadInvalidaException extends RuntimeException {
    public CantidadInvalidaException() {
        super("La cantidad debe ser mayor a cero");
    }

    public CantidadInvalidaException(String mensaje) {
        super(mensaje);
    }
}
