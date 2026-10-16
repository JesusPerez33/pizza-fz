package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.NoHayPizzasDisponiblesException;
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

    @Test
    @DisplayName("Mockito: devuelve las pizzas que entrega el repositorio")
    void debeDevolverPizzasDelRepositorio() {
        Pizza hawaiana = new Pizza(1L, "Hawaiana", "Piña y jamón", new BigDecimal("30000"), 5, true);
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

        assertThrows(NoHayPizzasDisponiblesException.class, catalogoService::consultarPizzasDisponibles);
    }

    @Test
    @DisplayName("Mockito: lanza PizzaNoEncontradaException si el id no existe")
    void debeLanzarPizzaNoEncontrada() {
        when(pizzaRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(PizzaNoEncontradaException.class, () -> catalogoService.consultarDetallePizza(99L));
    }
}
