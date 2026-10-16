package com.pizzafz.domain.exception;

public class UsuarioInactivoException extends RuntimeException {
    public UsuarioInactivoException() {
        super("Usuario inactivo");
    }

    public UsuarioInactivoException(String mensaje) {
        super(mensaje);
    }
}
