package com.pizzafz.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase base del dominio. Cliente, Cocinero y Administrador
 * comparten estos atributos y comportamientos comunes
 * (registrarse, iniciar sesión, cerrar sesión aplican a los 3 actores).
 *
 * Es abstracta porque en el dominio nunca existe un "Usuario" genérico,
 * siempre es una de sus especializaciones.
 */
public abstract class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private String passwordHash;
    private String telefono;
    private final Rol rol;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    protected Usuario(Long id, String nombre, String email, String passwordHash,
                       String telefono, Rol rol, boolean activo, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = Objects.requireNonNull(nombre, "nombre no puede ser null");
        this.email = Objects.requireNonNull(email, "email no puede ser null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash no puede ser null");
        this.telefono = telefono;
        this.rol = Objects.requireNonNull(rol, "rol no puede ser null");
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    // --- comportamiento común a los 3 actores ---

    public boolean tieneRol(Rol rolEsperado) {
        return this.rol == rolEsperado;
    }

    public void desactivar() {
        this.activo = false;
    }

    // --- getters ---

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getTelefono() { return telefono; }
    public Rol getRol() { return rol; }
    public boolean isActivo() { return activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public void setId(Long id) { this.id = id; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
