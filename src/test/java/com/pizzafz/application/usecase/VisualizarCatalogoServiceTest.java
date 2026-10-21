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

    // ---------- consultarPizzasDisponibles ----------

    @Test
    @DisplayName("Debe listar solo pizzas disponibles")
    void debeListarPizzasDisponibles() {
        List<Pizza> disponibles = catalogoService.consultarPizzasDisponibles();

        assertFalse(disponibles.isEmpty());
        assertTrue(disponibles.stream().allMatch(Pizza::estaDisponible));
    }

    @Test
    @DisplayName("El listado no incluye una pizza agotada")
    void listadoNoDebeIncluirPizzaAgotada() {
        Pizza agotada = pizzaRepository.guardar(
                new Pizza(null, "Agotada", "Desc", new BigDecimal("100"), 0, true));

        List<Pizza> disponibles = catalogoService.consultarPizzasDisponibles();

        assertTrue(disponibles.stream().noneMatch(p -> p.getId().equals(agotada.getId())));
    }

    @Test
    @DisplayName("Debe lanzar NoHayPizzasDisponiblesException si todas las pizzas están agotadas")
    void debeLanzarExcepcionCatalogoVacio() {
        pizzaRepository.buscarTodas().forEach(p -> p.setDisponibilidad(0));

        NoHayPizzasDisponiblesException exception = assertThrows(
                NoHayPizzasDisponiblesException.class,
                () -> catalogoService.consultarPizzasDisponibles());

        assertEquals("No hay pizzas disponibles", exception.getMessage());
    }

    // ---------- consultarDetallePizza ----------

    @Test
    @DisplayName("Debe consultar el detalle de una pizza disponible")
    void debeConsultarDetallesDePizza() {
        Pizza guardada = pizzaRepository.guardar(
                new Pizza(null, "Hawaiana", "Con piña", new BigDecimal("28000"), 5, true));

        Pizza detalle = catalogoService.consultarDetallePizza(guardada.getId());

        assertEquals(guardada.getId(), detalle.getId());
        assertEquals("Hawaiana", detalle.getNombre());
        assertEquals("Con piña", detalle.getDescripcion());
        assertEquals(0, new BigDecimal("28000").compareTo(detalle.getPrecio()));
    }

    @Test
    @DisplayName("Debe lanzar PizzaNoEncontradaException si el id no existe")
    void debeLanzarExcepcionPizzaNoEncontrada() {
        assertThrows(PizzaNoEncontradaException.class,
                () -> catalogoService.consultarDetallePizza(9999L));
    }

    @Test
    @DisplayName("Debe lanzar PizzaAgotadaException si la pizza no tiene disponibilidad")
    void debeLanzarExcepcionPizzaAgotada() {
        Pizza agotada = pizzaRepository.guardar(
                new Pizza(null, "Agotada", "Desc", new BigDecimal("100"), 0, true));

        PizzaAgotadaException exception = assertThrows(
                PizzaAgotadaException.class,
                () -> catalogoService.consultarDetallePizza(agotada.getId()));

        assertEquals("Pizza agotada", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar PizzaAgotadaException si la pizza está inactiva")
    void debeLanzarExcepcionPizzaInactiva() {
        Pizza inactiva = pizzaRepository.guardar(
                new Pizza(null, "Inactiva", "Desc", new BigDecimal("100"), 5, false));

        assertThrows(PizzaAgotadaException.class,
                () -> catalogoService.consultarDetallePizza(inactiva.getId()));
    }
}