package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.CantidadNoDisponibleException;
import com.pizzafz.domain.exception.ProductoNoEncontradoEnCarritoException;
import com.pizzafz.domain.model.Carrito;
import com.pizzafz.domain.model.Pizza;
import com.pizzafz.domain.service.ValidadorCarritoService;
import com.pizzafz.infrastructure.adapter.out.persistence.CarritoRepositoryAdapter;
import com.pizzafz.infrastructure.adapter.out.persistence.PizzaRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionarCarritoServiceTest {

    private GestionarCarritoService carritoService;
    private PizzaRepositoryAdapter pizzaRepository;
    private CarritoRepositoryAdapter carritoRepository;

    private Long clienteId = 100L;
    private Pizza pizzaPepperoni;

    @BeforeEach
    void setUp() {
        pizzaRepository = new PizzaRepositoryAdapter();
        carritoRepository = new CarritoRepositoryAdapter();
        ValidadorCarritoService validador = new ValidadorCarritoService();

        carritoService = new GestionarCarritoService(carritoRepository, pizzaRepository, validador);

        List<Pizza> disponibles = pizzaRepository.buscarDisponibles();
        pizzaPepperoni = disponibles.get(0); // Pepperoni (precio 35000, disponibilidad 10)
    }

    @Test
    @DisplayName("Debe agregar un producto al carrito y recalcular el total correctamente")
    void debeAgregarProductoYRecalcularTotal() {
        Carrito carrito = carritoService.agregarProducto(clienteId, pizzaPepperoni.getId(), 2);

        assertEquals(1, carrito.getItems().size());
        assertEquals(2, carrito.getItems().get(0).getCantidad());
        // 35000 * 2 = 70000
        assertEquals(new BigDecimal("70000.00"), carrito.getTotal());
    }

    @Test
    @DisplayName("Debe lanzar CantidadNoDisponibleException cuando la cantidad solicitada supera el inventario")
    void debeLanzarExcepcionCantidadSuperiorAlInventario() {
        int cantidadMayor = pizzaPepperoni.getDisponibilidad() + 5;

        CantidadNoDisponibleException exception = assertThrows(
                CantidadNoDisponibleException.class,
                () -> carritoService.agregarProducto(clienteId, pizzaPepperoni.getId(), cantidadMayor)
        );

        assertEquals("Cantidad solicitada ya no está disponible", exception.getMessage());
    }

    @Test
    @DisplayName("Debe modificar la cantidad de un producto existente en el carrito y recalcular el total")
    void debeModificarCantidadYRecalcularTotal() {
        carritoService.agregarProducto(clienteId, pizzaPepperoni.getId(), 2);

        Carrito carritoActualizado = carritoService.modificarCantidad(clienteId, pizzaPepperoni.getId(), 4);

        assertEquals(4, carritoActualizado.getItems().get(0).getCantidad());
        // 35000 * 4 = 140000
        assertEquals(new BigDecimal("140000.00"), carritoActualizado.getTotal());
    }

    @Test
    @DisplayName("Debe eliminar un producto individual del carrito")
    void debeEliminarProductoIndividual() {
        carritoService.agregarProducto(clienteId, pizzaPepperoni.getId(), 2);

        Carrito carritoPostEliminar = carritoService.eliminarProducto(clienteId, pizzaPepperoni.getId());

        assertTrue(carritoPostEliminar.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, carritoPostEliminar.getTotal());
    }

    @Test
    @DisplayName("Debe vaciar el carrito completamente al eliminar todos los productos")
    void debeVaciarCarritoCompletamente() {
        List<Pizza> disponibles = pizzaRepository.buscarDisponibles();
        Pizza pizza2 = disponibles.get(1);

        carritoService.agregarProducto(clienteId, pizzaPepperoni.getId(), 2);
        carritoService.agregarProducto(clienteId, pizza2.getId(), 1);

        Carrito carritoVacio = carritoService.vaciarCarrito(clienteId);

        assertTrue(carritoVacio.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, carritoVacio.getTotal());
    }
}
