package com.pizzafz.domain.exception;

import java.util.List;

/**
 * Se lanza cuando el paso <<incluye>> "validar datos del usuario" falla.
 * El adaptador de entrada (controller) debe traducir esto en la respuesta
 * que dispara el flujo "corregir datos" en el cliente (frontend).
 */
public class DatosInvalidosException extends RuntimeException {

    private final List<String> errores;

    public DatosInvalidosException(List<String> errores) {
        super("Datos incompletos o inválidos: " + String.join(", ", errores));
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
