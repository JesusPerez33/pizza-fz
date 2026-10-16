package com.pizzafz.domain.port.in;

import com.pizzafz.domain.model.Usuario;

public interface IniciarSesionUseCase {

    Usuario iniciarSesion(IniciarSesionCommand comando);

    record IniciarSesionCommand(
            String email,
            String passwordPlano
    ) {}
}
