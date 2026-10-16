package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.NoHayPizzasDisponiblesException;
import com.pizzafz.domain.exception.PizzaAgotadaException;
import com.pizzafz.domain.exception.PizzaNoEncontradaException;
import com.pizzafz.domain.model.Pizza;
import com.pizzafz.domain.port.in.VisualizarCatalogoUseCase;
import com.pizzafz.domain.port.out.PizzaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisualizarCatalogoService implements VisualizarCatalogoUseCase {

    private final PizzaRepositoryPort pizzaRepository;

    public VisualizarCatalogoService(PizzaRepositoryPort pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public List<Pizza> consultarPizzasDisponibles() {
        List<Pizza> disponibles = pizzaRepository.buscarDisponibles();
        if (disponibles.isEmpty()) {
            throw new NoHayPizzasDisponiblesException();
        }
        return disponibles;
    }

    @Override
    public Pizza consultarDetallePizza(Long id) {
        Pizza pizza = pizzaRepository.buscarPorId(id)
                .orElseThrow(PizzaNoEncontradaException::new);

        if (!pizza.estaDisponible()) {
            throw new PizzaAgotadaException();
        }

        return pizza;
    }
}
