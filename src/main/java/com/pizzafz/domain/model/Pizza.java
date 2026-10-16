package com.pizzafz.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Pizza {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int disponibilidad;
    private boolean activo;

    public Pizza(Long id, String nombre, String descripcion, BigDecimal precio, int disponibilidad, boolean activo) {
        this.id = id;
        this.nombre = Objects.requireNonNull(nombre, "nombre no puede ser null");
        this.descripcion = Objects.requireNonNull(descripcion, "descripcion no puede ser null");
        this.precio = Objects.requireNonNull(precio, "precio no puede ser null");
        this.disponibilidad = disponibilidad;
        this.activo = activo;
    }

    public boolean estaDisponible() {
        return activo && disponibilidad > 0;
    }

    public boolean tieneDisponibilidadSuficiente(int cantidad) {
        return activo && disponibilidad >= cantidad;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(int disponibilidad) { this.disponibilidad = disponibilidad; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
