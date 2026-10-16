package com.pizzafz.domain.service;

import com.pizzafz.domain.exception.DatosInvalidosException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementa el caso de uso incluido (<<incluye>>) "validar datos del usuario".
 * Es un servicio de DOMINIO (no de aplicación ni de infraestructura) porque
 * las reglas de validación son lógica de negocio pura, sin dependencias
 * externas (no toca base de datos, no toca HTTP).
 */
public class ValidadorUsuarioService {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    /**
     * Lanza DatosInvalidosException si algo falla.
     * Esto es lo que, en el flujo del caso de uso, activa el extend "corregir datos".
     */
    public void validar(String nombre, String email, String password, String telefono) {
        validarCliente(nombre, email, password, telefono, null);
    }

    public void validarCliente(String nombre, String email, String password, String telefono, String direccionEntrega) {
        List<String> errores = new ArrayList<>();

        if (nombre == null || nombre.isBlank()) {
            errores.add("el nombre es obligatorio");
        }
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            errores.add("el email no tiene un formato válido");
        }
        if (password == null || password.length() < 8) {
            errores.add("la contraseña debe tener al menos 8 caracteres");
        }
        if (telefono != null && !telefono.isBlank() && !telefono.matches("^[0-9+ -]{7,15}$")) {
            errores.add("el teléfono no tiene un formato válido");
        }
        if (direccionEntrega != null && direccionEntrega.isBlank()) {
            errores.add("la dirección de entrega no puede estar vacía");
        }

        if (!errores.isEmpty()) {
            throw new DatosInvalidosException(errores);
        }
    }
}
