package com.pizzafz.domain.model;

import com.pizzafz.domain.exception.CantidadInvalidaException;
import com.pizzafz.domain.exception.CantidadNoDisponibleException;
import com.pizzafz.domain.exception.PizzaAgotadaException;
import com.pizzafz.domain.exception.ProductoNoEncontradoEnCarritoException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Carrito {

    private Long id;
    private Long clienteId;
    private final List<ItemCarrito> items;
    private BigDecimal total;

    public Carrito(Long id, Long clienteId) {
        this.id = id;
        this.clienteId = Objects.requireNonNull(clienteId, "clienteId no puede ser null");
        this.items = new ArrayList<>();
        this.total = BigDecimal.ZERO;
    }

    public void agregarProducto(Pizza pizza, int cantidad) {
        if (pizza == null) {
            throw new IllegalArgumentException("La pizza no puede ser nula");
        }
        if (!pizza.isActivo() || pizza.getDisponibilidad() <= 0) {
            throw new PizzaAgotadaException("Pizza agotada");
        }
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor a cero");
        }

        Optional<ItemCarrito> itemExistente = buscarItemPorPizzaId(pizza.getId());
        int cantidadTotalDeseada = cantidad + itemExistente.map(ItemCarrito::getCantidad).orElse(0);

        if (!pizza.tieneDisponibilidadSuficiente(cantidadTotalDeseada)) {
            throw new CantidadNoDisponibleException("Cantidad solicitada ya no está disponible");
        }

        if (itemExistente.isPresent()) {
            itemExistente.get().setCantidad(cantidadTotalDeseada);
        } else {
            items.add(new ItemCarrito(pizza, cantidad));
        }

        recalcularTotal();
    }

    public void modificarCantidad(Long pizzaId, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor a cero");
        }

        ItemCarrito item = buscarItemPorPizzaId(pizzaId)
                .orElseThrow(() -> new ProductoNoEncontradoEnCarritoException("El producto no se encuentra en el carrito"));

        if (!item.getPizza().tieneDisponibilidadSuficiente(nuevaCantidad)) {
            throw new CantidadNoDisponibleException("Cantidad solicitada ya no está disponible");
        }

        item.setCantidad(nuevaCantidad);
        recalcularTotal();
    }

    public void eliminarProducto(Long pizzaId) {
        ItemCarrito item = buscarItemPorPizzaId(pizzaId)
                .orElseThrow(() -> new ProductoNoEncontradoEnCarritoException("El producto no se encuentra en el carrito"));
        items.remove(item);
        recalcularTotal();
    }

    public void vaciar() {
        items.clear();
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.total = items.stream()
                .map(ItemCarrito::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Optional<ItemCarrito> buscarItemPorPizzaId(Long pizzaId) {
        if (pizzaId == null) return Optional.empty();
        return items.stream()
                .filter(item -> pizzaId.equals(item.getPizza().getId()))
                .findFirst();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public List<ItemCarrito> getItems() { return Collections.unmodifiableList(items); }

    public BigDecimal getTotal() {
        recalcularTotal();
        return total;
    }
}
