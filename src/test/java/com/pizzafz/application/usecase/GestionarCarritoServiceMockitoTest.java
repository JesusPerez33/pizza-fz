package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.CantidadNoDisponibleException;
import com.pizzafz.domain.exception.PizzaNoEncontradaException;
import com.pizzafz.domain.model.Carrito;
import com.pizzafz.domain.model.Pizza;
import com.pizzafz.domain.port.out.CarritoRepositoryPort;
import com.pizzafz.domain.port.out.PizzaRepositoryPort;
import com.pizzafz.domain.service.ValidadorCarritoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionarCarritoServiceMockitoTest {

    @Mock
    private CarritoRepositoryPort carritoRepository;

    @Mock
    private PizzaRepositoryPort pizzaRepository;

    @Mock
    private ValidadorCarritoService validadorCarrito;

    @InjectMocks
    private GestionarCarritoService carritoService;

    private static final Long CLIENTE_ID = 100L;
    private final Pizza pepperoni = new Pizza(1L, "Pepperoni", "Clásica", new BigDecimal("35000"), 10, true);

    @Test
    @DisplayName("Mockito: crea un carrito nuevo si el cliente no tiene uno")
    void debeCrearCarritoSiNoExiste() {
        when(carritoRepository.buscarPorClienteId(CLIENTE_ID)).thenReturn(Optional.empty());
        when(carritoRepository.guardar(any(Carrito.class))).thenAnswer(inv -> inv.getArgument(0));

        Carrito carrito = carritoService.obtenerCarrito(CLIENTE_ID);

        assertEquals(CLIENTE_ID, carrito.getClienteId());
        assertTrue(carrito.getItems().isEmpty());
        verify(carritoRepository).guardar(any(Carrito.class));
    }

    @Test
    @DisplayName("Mockito: agrega producto usando la pizza del repositorio y guarda el carrito")
    void debeAgregarProducto() {
        when(carritoRepository.buscarPorClienteId(CLIENTE_ID)).thenReturn(Optional.of(new Carrito(1L, CLIENTE_ID)));
        when(pizzaRepository.buscarPorId(1L)).thenReturn(Optional.of(pepperoni));
        when(carritoRepository.guardar(any(Carrito.class))).thenAnswer(inv -> inv.getArgument(0));

        Carrito carrito = carritoService.agregarProducto(CLIENTE_ID, 1L, 2);

        assertEquals(0, new BigDecimal("70000").compareTo(carrito.getTotal()));
        verify(validadorCarrito).validarAgregarProducto(pepperoni, 2);
        verify(carritoRepository, times(1)).guardar(carrito);
    }

    @Test
    @DisplayName("Mockito: lanza PizzaNoEncontradaException y no guarda si la pizza no existe")
    void debeLanzarPizzaNoEncontrada() {
        when(carritoRepository.buscarPorClienteId(CLIENTE_ID)).thenReturn(Optional.of(new Carrito(1L, CLIENTE_ID)));
        when(pizzaRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(PizzaNoEncontradaException.class, () -> carritoService.agregarProducto(CLIENTE_ID, 99L, 1));

        verify(carritoRepository, never()).guardar(any());
    }

    @Test
    @DisplayName("Mockito: si el validador rechaza la cantidad no se guarda el carrito")
    void debePropagarCantidadNoDisponible() {
        when(carritoRepository.buscarPorClienteId(CLIENTE_ID)).thenReturn(Optional.of(new Carrito(1L, CLIENTE_ID)));
        when(pizzaRepository.buscarPorId(1L)).thenReturn(Optional.of(pepperoni));
        doThrow(new CantidadNoDisponibleException("Cantidad solicitada ya no está disponible"))
                .when(validadorCarrito).validarAgregarProducto(pepperoni, 50);

        assertThrows(CantidadNoDisponibleException.class, () -> carritoService.agregarProducto(CLIENTE_ID, 1L, 50));

        verify(carritoRepository, never()).guardar(any());
    }

    @Test
    @DisplayName("Mockito: vaciar carrito deja el total en cero y lo guarda")
    void debeVaciarCarrito() {
        Carrito carrito = new Carrito(1L, CLIENTE_ID);
        carrito.agregarProducto(pepperoni, 3);
        when(carritoRepository.buscarPorClienteId(CLIENTE_ID)).thenReturn(Optional.of(carrito));
        when(carritoRepository.guardar(carrito)).thenReturn(carrito);

        Carrito resultado = carritoService.vaciarCarrito(CLIENTE_ID);

        assertTrue(resultado.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, resultado.getTotal());
    }
}
