package com.pizzafz.domain.port.in;

import com.pizzafz.domain.model.Carrito;

public interface GestionarCarritoUseCase {

    Carrito obtenerCarrito(Long clienteId);

    Carrito agregarProducto(Long clienteId, Long pizzaId, int cantidad);

    Carrito modificarCantidad(Long clienteId, Long pizzaId, int nuevaCantidad);

    Carrito eliminarProducto(Long clienteId, Long pizzaId);

    Carrito vaciarCarrito(Long clienteId);
}
