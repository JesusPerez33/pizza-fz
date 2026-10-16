package com.pizzafz.domain.model;

import java.time.LocalDateTime;

/**
 * Actor "Nuevo cliente" del diagrama: registrarse, iniciar sesión,
 * visualizar catálogo, carrito, pedidos, pagos, historial, cerrar sesión.
 */
public class Cliente extends Usuario {

    private String direccionEntrega;

    public Cliente(Long id, String nombre, String email, String passwordHash,
                    String telefono, String direccionEntrega,
                    boolean activo, LocalDateTime fechaRegistro) {
        super(id, nombre, email, passwordHash, telefono, Rol.CLIENTE, activo, fechaRegistro);
        this.direccionEntrega = direccionEntrega;
    }

    /** Fábrica usada por el caso de uso "registrarse" (ver RegistrarUsuarioService). */
    public static Cliente nuevo(String nombre, String email, String passwordHash,
                                 String telefono, String direccionEntrega) {
        return new Cliente(null, nombre, email, passwordHash, telefono,
                direccionEntrega, true, LocalDateTime.now());
    }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
}
