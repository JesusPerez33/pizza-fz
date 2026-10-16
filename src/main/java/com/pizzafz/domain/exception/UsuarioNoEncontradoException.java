package com.pizzafz.domain.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException() {
        super("Usuario no registrado");
    }

    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
