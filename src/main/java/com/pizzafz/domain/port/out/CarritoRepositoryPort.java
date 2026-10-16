package com.pizzafz.domain.port.out;

import com.pizzafz.domain.model.Carrito;

import java.util.Optional;

public interface CarritoRepositoryPort {

    Optional<Carrito> buscarPorClienteId(Long clienteId);

    Carrito guardar(Carrito carrito);

    void eliminarPorClienteId(Long clienteId);
}
