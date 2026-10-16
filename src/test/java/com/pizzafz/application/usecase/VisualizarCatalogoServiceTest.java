package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.NoHayPizzasDisponiblesException;
import com.pizzafz.domain.exception.PizzaAgotadaException;
import com.pizzafz.domain.exception.PizzaNoEncontradaException;
import com.pizzafz.domain.model.Pizza;
import com.pizzafz.infrastructure.adapter.out.persistence.PizzaRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VisualizarCatalogoServiceTest {

    private VisualizarCatalogoService catalogoService;
    private PizzaRepositoryAdapter pizzaRepository;

    @BeforeEach
    void setUp() {
        pizzaRepository = new PizzaRepositoryAdapter();
        catalogoService = new VisualizarCatalogoService(pizzaRepository);
    }

    @Test
    @DisplayName("Debe listar las pizzas disponibles activas")
    void debeListarPizzasDisponibles() {
        List<Pizza> disponibles = catalogoService.consultarPizzasDisponibles();

        assertFalse(disponibles.isEmpty());
        assertTrue(disponibles.stream().allMatch(Pizza::estaDisponible));
    }

    @Test
    @DisplayName("Debe lanzar NoHayPizzasDisponiblesException si no existen pizzas disponibles en el catálogo")
    void debeLanzarExcepcionCatalogoVacio() {
        PizzaRepositoryAdapter repoVacio = new PizzaRepositoryAdapter();
        // Desactivar o agotar todas las pizzas
        repoVacio.buscarTodas().forEach(p -> p.setDisponibilidad(0));
        VisualizarCatalogoService serviceVacio = new VisualizarCatalogoService(repoVacio);

        NoHayPizzasDisponiblesException exception = assertThrows(
                NoHayPizzasDisponiblesException.class,
                serviceVacio::consultarPizzasDisponibles
        );

        assertEquals("No hay pizzas disponibles", exception.getMessage());
    }

    @Test
    @DisplayName("Debe consultar los detalles de una pizza correctamente")
    void debeConsultarDetallesDePizza() {
        List<Pizza> disponibles = catalogoService.consultarPizzasDisponibles();
        Pizza primera = disponibles.get(0);

        Pizza detalle = catalogoService.consultarDetallePizza(primera.getId());

        assertNotNull(detalle);
        assertEquals(primera.getNombre(), detalle.getNombre());
        assertEquals(primera.getDescripcion(), detalle.getDescripcion());
    }

    @Test
    @DisplayName("Debe lanzar PizzaAgotadaException si la pizza consultada está agotada o inactiva")
    void debeLanzarExcepcionPizzaAgotada() {
        Pizza pizzaAgotada = pizzaRepository.buscarTodas().stream()
                .filter(p -> p.getDisponibilidad() == 0)
                .findFirst()
                .orElseGet(() -> pizzaRepository.guardar(new Pizza(null, "Agotada", "Desc", new BigDecimal("100"), 0, true)));

        PizzaAgotadaException exception = assertThrows(
                PizzaAgotadaException.class,
                () -> catalogoService.consultarDetallePizza(pizzaAgotada.getId())
        );

        assertEquals("Pizza agotada", exception.getMessage());
    }
}
