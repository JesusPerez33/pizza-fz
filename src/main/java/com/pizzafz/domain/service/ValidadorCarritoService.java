package com.pizzafz.domain.service;

import com.pizzafz.domain.exception.CantidadInvalidaException;
import com.pizzafz.domain.exception.CantidadNoDisponibleException;
import com.pizzafz.domain.exception.DatosInvalidosException;
import com.pizzafz.domain.exception.PizzaAgotadaException;
import com.pizzafz.domain.model.Pizza;

import java.util.List;

public class ValidadorCarritoService {

    public void validarClienteId(Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new DatosInvalidosException(List.of("el ID del cliente no es válido"));
        }
    }

    public void validarAgregarProducto(Pizza pizza, int cantidad) {
        if (pizza == null) {
            throw new DatosInvalidosException(List.of("la pizza no es válida"));
        }
        if (!pizza.isActivo() || pizza.getDisponibilidad() <= 0) {
            throw new PizzaAgotadaException("Pizza agotada");
        }
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor a cero");
        }
        if (!pizza.tieneDisponibilidadSuficiente(cantidad)) {
            throw new CantidadNoDisponibleException("Cantidad solicitada ya no está disponible");
        }
    }
}
