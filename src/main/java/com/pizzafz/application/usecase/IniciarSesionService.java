package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.CredencialesInvalidasException;
import com.pizzafz.domain.exception.UsuarioInactivoException;
import com.pizzafz.domain.exception.UsuarioNoEncontradoException;
import com.pizzafz.domain.model.Usuario;
import com.pizzafz.domain.port.in.IniciarSesionUseCase;
import com.pizzafz.domain.port.out.PasswordHasherPort;
import com.pizzafz.domain.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class IniciarSesionService implements IniciarSesionUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordHasherPort passwordHasher;

    public IniciarSesionService(UsuarioRepositoryPort usuarioRepository,
                                PasswordHasherPort passwordHasher) {
        this.usuarioRepository = usuarioRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public Usuario iniciarSesion(IniciarSesionCommand comando) {
        if (comando == null || comando.email() == null || comando.email().isBlank()) {
            throw new UsuarioNoEncontradoException("Usuario no registrado");
        }

        Usuario usuario = usuarioRepository.buscarPorEmail(comando.email())
                .orElseThrow(UsuarioNoEncontradoException::new);

        if (!usuario.isActivo()) {
            throw new UsuarioInactivoException();
        }

        boolean passwordValido = passwordHasher.verificar(comando.passwordPlano(), usuario.getPasswordHash());
        if (!passwordValido) {
            throw new CredencialesInvalidasException();
        }

        return usuario;
    }
}
