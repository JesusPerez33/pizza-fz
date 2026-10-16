package com.pizzafz.domain.service;

import com.pizzafz.domain.exception.DatosInvalidosException;
import com.pizzafz.domain.model.Pizza;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ValidadorPizzaService {

    public void validar(Pizza pizza) {
        if (pizza == null) {
            throw new DatosInvalidosException(List.of("la pizza no puede ser nula"));
        }
        validar(pizza.getNombre(), pizza.getDescripcion(), pizza.getPrecio(), pizza.getDisponibilidad());
    }

    public void validar(String nombre, String descripcion, BigDecimal precio, int disponibilidad) {
        List<String> errores = new ArrayList<>();

        if (nombre == null || nombre.isBlank()) {
            errores.add("el nombre de la pizza es obligatorio");
        }
        if (descripcion == null || descripcion.isBlank()) {
            errores.add("la descripción de la pizza es obligatoria");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("el precio debe ser mayor que cero");
        }
        if (disponibilidad < 0) {
            errores.add("la disponibilidad no puede ser negativa");
        }

        if (!errores.isEmpty()) {
            throw new DatosInvalidosException(errores);
        }
    }
}
