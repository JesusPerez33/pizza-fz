package com.pizzafz.domain.port.out;

import com.pizzafz.domain.model.Usuario;

import java.util.Optional;

/**
 * Puerto de salida (driven port). El dominio declara qué necesita de la
 * persistencia, sin saber si detrás hay JPA, MongoDB, un mapa en memoria, etc.
 * La implementación real vive en infrastructure/adapter/out/persistence.
 */
public interface UsuarioRepositoryPort {

    Optional<Usuario> buscarPorEmail(String email);

    Usuario guardar(Usuario usuario);
}
