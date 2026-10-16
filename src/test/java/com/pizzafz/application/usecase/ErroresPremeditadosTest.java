package com.pizzafz.application.usecase;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ERRORES PREMEDITADOS (para la actividad de CI).
 * Estas pruebas FALLAN a propósito para que se vean en rojo en GitHub Actions.
 * Para dejar el pipeline en verde: borra esta clase o corrige los valores esperados
 * (1 en vez de 2, y times(1) en vez de times(2)).
 */
@ExtendWith(MockitoExtension.class)
class ErroresPremeditadosTest {

    @Mock
    private PizzaRepositoryPort pizzaRepository;

    @InjectMocks
    private VisualizarCatalogoService catalogoService;

    @Test
    @DisplayName("ERROR PREMEDITADO: se espera 2 pizzas pero el mock devuelve 1")
    void errorPremeditadoCantidadIncorrecta() {
        Pizza pizza = new Pizza(1L, "Pepperoni", "Clásica", new BigDecimal("35000"), 10, true);
        when(pizzaRepository.buscarDisponibles()).thenReturn(List.of(pizza));

        List<Pizza> resultado = catalogoService.consultarPizzasDisponibles();

        assertEquals(2, resultado.size(), "Error premeditado: el catálogo solo tiene 1 pizza");
    }

    @Test
    @DisplayName("ERROR PREMEDITADO: Mockito verifica 2 llamadas al repositorio pero solo hubo 1")
    void errorPremeditadoVerifyMockito() {
        Pizza pizza = new Pizza(1L, "Pepperoni", "Clásica", new BigDecimal("35000"), 10, true);
        when(pizzaRepository.buscarDisponibles()).thenReturn(List.of(pizza));

        catalogoService.consultarPizzasDisponibles();

        verify(pizzaRepository, times(2)).buscarDisponibles();
    }
}
