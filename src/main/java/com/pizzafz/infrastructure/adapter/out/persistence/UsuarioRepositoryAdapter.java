package com.pizzafz.infrastructure.adapter.out.persistence;

import com.pizzafz.domain.model.Usuario;
import com.pizzafz.domain.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Adaptador de salida (driven adapter). Implementación provisional en
 * memoria para poder probar el flujo end-to-end sin montar aún la base
 * de datos. Cuando conectes JPA, esta clase se reemplaza por una que
 * use un UsuarioJpaRepository (Spring Data) + un mapper Entity<->Dominio,
 * sin tocar ni el dominio ni la capa de aplicación.
 */
@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final Map<String, Usuario> porEmail = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(porEmail.get(email));
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(secuencia.getAndIncrement());
        }
        porEmail.put(usuario.getEmail(), usuario);
        return usuario;
    }
}
