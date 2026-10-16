package com.pizzafz.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemCarrito {

    private Pizza pizza;
    private int cantidad;
    private BigDecimal precioUnitario;

    public ItemCarrito(Pizza pizza, int cantidad) {
        this.pizza = Objects.requireNonNull(pizza, "pizza no puede ser null");
        this.cantidad = cantidad;
        this.precioUnitario = pizza.getPrecio();
    }

    public BigDecimal calcularSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public Pizza getPizza() { return pizza; }
    public void setPizza(Pizza pizza) { this.pizza = pizza; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}
