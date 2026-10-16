package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.PizzaNoEncontradaException;
import com.pizzafz.domain.model.Carrito;
import com.pizzafz.domain.model.Pizza;
import com.pizzafz.domain.port.in.GestionarCarritoUseCase;
import com.pizzafz.domain.port.out.CarritoRepositoryPort;
import com.pizzafz.domain.port.out.PizzaRepositoryPort;
import com.pizzafz.domain.service.ValidadorCarritoService;
import org.springframework.stereotype.Service;

@Service
public class GestionarCarritoService implements GestionarCarritoUseCase {

    private final CarritoRepositoryPort carritoRepository;
    private final PizzaRepositoryPort pizzaRepository;
    private final ValidadorCarritoService validadorCarrito;

    public GestionarCarritoService(CarritoRepositoryPort carritoRepository,
                                  PizzaRepositoryPort pizzaRepository,
                                  ValidadorCarritoService validadorCarrito) {
        this.carritoRepository = carritoRepository;
        this.pizzaRepository = pizzaRepository;
        this.validadorCarrito = validadorCarrito;
    }

    @Override
    public Carrito obtenerCarrito(Long clienteId) {
        validadorCarrito.validarClienteId(clienteId);
        return carritoRepository.buscarPorClienteId(clienteId)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito(null, clienteId);
                    return carritoRepository.guardar(nuevo);
                });
    }

    @Override
    public Carrito agregarProducto(Long clienteId, Long pizzaId, int cantidad) {
        Carrito carrito = obtenerCarrito(clienteId);
        Pizza pizza = pizzaRepository.buscarPorId(pizzaId)
                .orElseThrow(PizzaNoEncontradaException::new);

        validadorCarrito.validarAgregarProducto(pizza, cantidad);
        carrito.agregarProducto(pizza, cantidad);
        return carritoRepository.guardar(carrito);
    }

    @Override
    public Carrito modificarCantidad(Long clienteId, Long pizzaId, int nuevaCantidad) {
        Carrito carrito = obtenerCarrito(clienteId);
        carrito.modificarCantidad(pizzaId, nuevaCantidad);
        return carritoRepository.guardar(carrito);
    }

    @Override
    public Carrito eliminarProducto(Long clienteId, Long pizzaId) {
        Carrito carrito = obtenerCarrito(clienteId);
        carrito.eliminarProducto(pizzaId);
        return carritoRepository.guardar(carrito);
    }

    @Override
    public Carrito vaciarCarrito(Long clienteId) {
        Carrito carrito = obtenerCarrito(clienteId);
        carrito.vaciar();
        return carritoRepository.guardar(carrito);
    }
}
