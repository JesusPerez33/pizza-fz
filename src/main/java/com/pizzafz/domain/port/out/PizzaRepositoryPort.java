package com.pizzafz.domain.port.out;

import com.pizzafz.domain.model.Pizza;

import java.util.List;
import java.util.Optional;

public interface PizzaRepositoryPort {

    Optional<Pizza> buscarPorId(Long id);

    List<Pizza> buscarDisponibles();

    List<Pizza> buscarTodas();

    Pizza guardar(Pizza pizza);
}
