package com.pizzafz.domain.model;

import java.time.LocalDateTime;

/**
 * Actor "Cocinero": preparar pedido, actualizar estado del pedido.
 */
public class Cocinero extends Usuario {

    private String turno; // ej: "mañana", "tarde", "noche"

    public Cocinero(Long id, String nombre, String email, String passwordHash,
                     String telefono, String turno,
                     boolean activo, LocalDateTime fechaRegistro) {
        super(id, nombre, email, passwordHash, telefono, Rol.COCINERO, activo, fechaRegistro);
        this.turno = turno;
    }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
}
