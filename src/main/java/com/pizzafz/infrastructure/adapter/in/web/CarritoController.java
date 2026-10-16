package com.pizzafz.infrastructure.adapter.in.web;

import com.pizzafz.domain.exception.CantidadInvalidaException;
import com.pizzafz.domain.exception.CantidadNoDisponibleException;
import com.pizzafz.domain.exception.PizzaAgotadaException;
import com.pizzafz.domain.exception.PizzaNoEncontradaException;
import com.pizzafz.domain.exception.ProductoNoEncontradoEnCarritoException;
import com.pizzafz.domain.model.Carrito;
import com.pizzafz.domain.port.in.GestionarCarritoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final GestionarCarritoUseCase gestionarCarritoUseCase;

    public CarritoController(GestionarCarritoUseCase gestionarCarritoUseCase) {
        this.gestionarCarritoUseCase = gestionarCarritoUseCase;
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Long clienteId) {
        Carrito carrito = gestionarCarritoUseCase.obtenerCarrito(clienteId);
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/{clienteId}/items")
    public ResponseEntity<Carrito> agregarProducto(
            @PathVariable Long clienteId,
            @RequestBody AgregarItemRequest request) {
        Carrito carrito = gestionarCarritoUseCase.agregarProducto(clienteId, request.pizzaId(), request.cantidad());
        return ResponseEntity.status(HttpStatus.CREATED).body(carrito);
    }

    @PutMapping("/{clienteId}/items/{pizzaId}")
    public ResponseEntity<Carrito> modificarCantidad(
            @PathVariable Long clienteId,
            @PathVariable Long pizzaId,
            @RequestBody ModificarCantidadRequest request) {
        Carrito carrito = gestionarCarritoUseCase.modificarCantidad(clienteId, pizzaId, request.nuevaCantidad());
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/{clienteId}/items/{pizzaId}")
    public ResponseEntity<Carrito> eliminarProducto(
            @PathVariable Long clienteId,
            @PathVariable Long pizzaId) {
        Carrito carrito = gestionarCarritoUseCase.eliminarProducto(clienteId, pizzaId);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Carrito> vaciarCarrito(@PathVariable Long clienteId) {
        Carrito carrito = gestionarCarritoUseCase.vaciarCarrito(clienteId);
        return ResponseEntity.ok(carrito);
    }

    @ExceptionHandler({CantidadInvalidaException.class, CantidadNoDisponibleException.class, PizzaAgotadaException.class})
    public ResponseEntity<?> manejarErrorValidacionCarrito(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler({PizzaNoEncontradaException.class, ProductoNoEncontradoEnCarritoException.class})
    public ResponseEntity<?> manejarRecursoNoEncontrado(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", ex.getMessage()));
    }

    public record AgregarItemRequest(Long pizzaId, int cantidad) {}
    public record ModificarCantidadRequest(int nuevaCantidad) {}
}
