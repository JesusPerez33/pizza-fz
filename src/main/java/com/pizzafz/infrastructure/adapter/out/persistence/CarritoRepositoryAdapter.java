package com.pizzafz.infrastructure.adapter.out.persistence;

import com.pizzafz.domain.model.Carrito;
import com.pizzafz.domain.port.out.CarritoRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CarritoRepositoryAdapter implements CarritoRepositoryPort {

    private final Map<Long, Carrito> porClienteId = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @Override
    public Optional<Carrito> buscarPorClienteId(Long clienteId) {
        if (clienteId == null) return Optional.empty();
        return Optional.ofNullable(porClienteId.get(clienteId));
    }

    @Override
    public Carrito guardar(Carrito carrito) {
        if (carrito.getId() == null) {
            carrito.setId(secuencia.getAndIncrement());
        }
        porClienteId.put(carrito.getClienteId(), carrito);
        return carrito;
    }

    @Override
    public void eliminarPorClienteId(Long clienteId) {
        if (clienteId != null) {
            porClienteId.remove(clienteId);
        }
    }
}
