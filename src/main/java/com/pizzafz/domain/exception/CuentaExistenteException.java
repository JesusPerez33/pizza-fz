package com.pizzafz.domain.exception;

/**
 * Se lanza cuando se cumple la condición del punto de extensión <<extend>>
 * "informar de cuenta existente": el email ya está registrado.
 */
public class CuentaExistenteException extends RuntimeException {

    public CuentaExistenteException(String email) {
        super("Ya existe una cuenta registrada con el correo: " + email);
    }
}
