package com.pizzafz.domain.port.in;

import com.pizzafz.domain.model.Pizza;

import java.util.List;

public interface VisualizarCatalogoUseCase {

    List<Pizza> consultarPizzasDisponibles();

    Pizza consultarDetallePizza(Long id);
}
