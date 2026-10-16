package com.pizzafz.infrastructure.adapter.in.web;

import com.pizzafz.domain.exception.NoHayPizzasDisponiblesException;
import com.pizzafz.domain.exception.PizzaAgotadaException;
import com.pizzafz.domain.exception.PizzaNoEncontradaException;
import com.pizzafz.domain.model.Pizza;
import com.pizzafz.domain.port.in.VisualizarCatalogoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final VisualizarCatalogoUseCase visualizarCatalogoUseCase;

    public CatalogoController(VisualizarCatalogoUseCase visualizarCatalogoUseCase) {
        this.visualizarCatalogoUseCase = visualizarCatalogoUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Pizza>> obtenerPizzasDisponibles() {
        List<Pizza> pizzas = visualizarCatalogoUseCase.consultarPizzasDisponibles();
        return ResponseEntity.ok(pizzas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pizza> obtenerDetallePizza(@PathVariable Long id) {
        Pizza pizza = visualizarCatalogoUseCase.consultarDetallePizza(id);
        return ResponseEntity.ok(pizza);
    }

    @ExceptionHandler(NoHayPizzasDisponiblesException.class)
    public ResponseEntity<?> manejarNoHayPizzas(NoHayPizzasDisponiblesException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(PizzaNoEncontradaException.class)
    public ResponseEntity<?> manejarPizzaNoEncontrada(PizzaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(PizzaAgotadaException.class)
    public ResponseEntity<?> manejarPizzaAgotada(PizzaAgotadaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", ex.getMessage()));
    }
}
