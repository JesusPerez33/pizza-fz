package com.pizzafz.domain.exception;

public class CantidadNoDisponibleException extends RuntimeException {
    public CantidadNoDisponibleException() {
        super("Cantidad solicitada ya no está disponible");
    }

    public CantidadNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
