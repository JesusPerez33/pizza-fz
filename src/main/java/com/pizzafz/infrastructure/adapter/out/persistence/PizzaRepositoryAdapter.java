package com.pizzafz.infrastructure.adapter.out.persistence;

import com.pizzafz.domain.model.Pizza;
import com.pizzafz.domain.port.out.PizzaRepositoryPort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PizzaRepositoryAdapter implements PizzaRepositoryPort {

    private final Map<Long, Pizza> pizzas = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    public PizzaRepositoryAdapter() {
        // Inicializar catálogo base de demostración
        guardar(new Pizza(null, "Pizza Pepperoni", "Salsa de tomate, queso mozzarella y pepperoni artesanal", new BigDecimal("35000.00"), 10, true));
        guardar(new Pizza(null, "Pizza Margarita", "Salsa de tomate, queso mozzarella y albahaca fresca", new BigDecimal("30000.00"), 15, true));
        guardar(new Pizza(null, "Pizza Hawaiana", "Salsa de tomate, queso, jamón y piña seleccionada", new BigDecimal("32000.00"), 8, true));
        guardar(new Pizza(null, "Pizza Especial Agotada", "Pizza de temporada agotada", new BigDecimal("40000.00"), 0, true));
    }

    @Override
    public Optional<Pizza> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(pizzas.get(id));
    }

    @Override
    public List<Pizza> buscarDisponibles() {
        return pizzas.values().stream()
                .filter(Pizza::estaDisponible)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pizza> buscarTodas() {
        return new ArrayList<>(pizzas.values());
    }

    @Override
    public Pizza guardar(Pizza pizza) {
        if (pizza.getId() == null) {
            pizza.setId(secuencia.getAndIncrement());
        }
        pizzas.put(pizza.getId(), pizza);
        return pizza;
    }
}
