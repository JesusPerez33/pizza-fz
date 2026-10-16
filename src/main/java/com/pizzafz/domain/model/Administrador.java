package com.pizzafz.domain.model;

import java.time.LocalDateTime;

/**
 * Actor "Administrador": gestionar usuarios, productos, inventario,
 * pedidos, reportes, promociones, cerrar sesión.
 */
public class Administrador extends Usuario {

    private String nivelAcceso; // ej: "total", "solo-reportes", etc.

    public Administrador(Long id, String nombre, String email, String passwordHash,
                          String telefono, String nivelAcceso,
                          boolean activo, LocalDateTime fechaRegistro) {
        super(id, nombre, email, passwordHash, telefono, Rol.ADMINISTRADOR, activo, fechaRegistro);
        this.nivelAcceso = nivelAcceso;
    }

    public String getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(String nivelAcceso) { this.nivelAcceso = nivelAcceso; }
}
