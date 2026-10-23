package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.NoHayPizzasDisponiblesException;
import com.pizzafz.domain.exception.PizzaAgotadaException;
import com.pizzafz.domain.exception.PizzaNoEncontradaException;
import com.pizzafz.domain.model.Pizza;
import com.pizzafz.domain.port.out.PizzaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisualizarCatalogoServiceMockitoTest {

    @Mock
    private PizzaRepositoryPort pizzaRepository;

    @InjectMocks
    private VisualizarCatalogoService catalogoService;

    // ---------- consultarPizzasDisponibles ----------

    @Test
    @DisplayName("Mockito: devuelve las pizzas que entrega el repositorio")
    void debeDevolverPizzasDelRepositorio() {
        Pizza hawaiana = new Pizza(1L, "Hawaiana", "Piña y jamón", new BigDecimal("387000"), 5, true);
        when(pizzaRepository.buscarDisponibles()).thenReturn(List.of(hawaiana));

        List<Pizza> resultado = catalogoService.consultarPizzasDisponibles();

        assertEquals(1, resultado.size());
        assertEquals("Hawaiana", resultado.get(0).getNombre());
        verify(pizzaRepository, times(1)).buscarDisponibles();
    }

    @Test
    @DisplayName("Mockito: lanza NoHayPizzasDisponiblesException si el repositorio devuelve lista vacía")
    void debeLanzarExcepcionSiNoHayPizzas() {
        when(pizzaRepository.buscarDisponibles()).thenReturn(List.of());

        NoHayPizzasDisponiblesException exception = assertThrows(
                NoHayPizzasDisponiblesException.class,
                catalogoService::consultarPizzasDisponibles);

        assertEquals("No hay pizzas disponibles", exception.getMessage());
    }

    // ---------- consultarDetallePizza ----------

    @Test
    @DisplayName("Mockito: devuelve el detalle de una pizza disponible")
    void debeConsultarDetalle() {
        Pizza hawaiana = new Pizza(1L, "Hawaiana", "Piña y jamón", new BigDecimal("3000"), 5, true);
        when(pizzaRepository.buscarPorId(1L)).thenReturn(Optional.of(hawaiana));

        Pizza detalle = catalogoService.consultarDetallePizza(1L);

        assertSame(hawaiana, detalle);
        verify(pizzaRepository).buscarPorId(1L);
    }

    @Test
    @DisplayName("Mockito: lanza PizzaNoEncontradaException si el id no existe")
    void debeLanzarPizzaNoEncontrada() {
        when(pizzaRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(PizzaNoEncontradaException.class, () -> catalogoService.consultarDetallePizza(99L));

        verify(pizzaRepository).buscarPorId(99L);
    }

    @Test
    @DisplayName("Mockito: lanza PizzaAgotadaException si la pizza no tiene disponibilidad")
    void debeLanzarPizzaAgotada() {
        Pizza agotada = new Pizza(3L, "Agotada", "Desc", new BigDecimal("100"), 0, true);
        when(pizzaRepository.buscarPorId(3L)).thenReturn(Optional.of(agotada));

        PizzaAgotadaException exception = assertThrows(
                PizzaAgotadaException.class,
                () -> catalogoService.consultarDetallePizza(3L));

        assertEquals("Pizza agotada", exception.getMessage());
    }

    @Test
    @DisplayName("Mockito: lanza PizzaAgotadaException si la pizza está inactiva")
    void debeLanzarPizzaInactiva() {
        Pizza inactiva = new Pizza(4L, "Inactiva", "Desc", new BigDecimal("100"), 5, false);
        when(pizzaRepository.buscarPorId(4L)).thenReturn(Optional.of(inactiva));

        assertThrows(PizzaAgotadaException.class, () -> catalogoService.consultarDetallePizza(4L));
    }
}