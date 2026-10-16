package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.CuentaExistenteException;
import com.pizzafz.domain.model.Cliente;
import com.pizzafz.domain.port.in.RegistrarUsuarioUseCase;
import com.pizzafz.domain.port.out.PasswordHasherPort;
import com.pizzafz.domain.port.out.UsuarioRepositoryPort;
import com.pizzafz.domain.service.ValidadorUsuarioService;
import org.springframework.stereotype.Service;

/**
 * Implementación del caso de uso "registrarse".
 * Orquesta exactamente el flujo del diagrama:
 *
 *   1. <<incluye>> validar datos del usuario   -> ValidadorUsuarioService (SIEMPRE se ejecuta)
 *   2. <<extend>>  informar de cuenta existente -> se dispara SOLO si el email ya existe
 *   3. <<extend>>  corregir datos               -> se dispara SOLO si la validación falla
 *      (se traduce en la excepción propagada hacia el controller)
 *
 * Vive en la capa de aplicación: coordina el dominio y los puertos,
 * pero no contiene reglas de negocio (esas están en el dominio).
 */
@Service
public class RegistrarUsuarioService implements RegistrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordHasherPort passwordHasher;
    private final ValidadorUsuarioService validador;

    public RegistrarUsuarioService(UsuarioRepositoryPort usuarioRepository,
                                    PasswordHasherPort passwordHasher,
                                    ValidadorUsuarioService validador) {
        this.usuarioRepository = usuarioRepository;
        this.passwordHasher = passwordHasher;
        this.validador = validador;
    }

    @Override
    public Cliente registrar(RegistrarUsuarioCommand comando) {

        // 1) <<incluye>> validar datos del usuario
        //    si falla, lanza DatosInvalidosException -> dispara "corregir datos"
        validador.validarCliente(comando.nombre(), comando.email(),
                comando.passwordPlano(), comando.telefono(), comando.direccionEntrega());

        // 2) <<extend>> informar de cuenta existente
        usuarioRepository.buscarPorEmail(comando.email())
                .ifPresent(u -> { throw new CuentaExistenteException(comando.email()); });

        // 3) flujo principal: crear y persistir el cliente
        String passwordHash = passwordHasher.hashear(comando.passwordPlano());

        Cliente cliente = Cliente.nuevo(
                comando.nombre(),
                comando.email(),
                passwordHash,
                comando.telefono(),
                comando.direccionEntrega()
        );

        return (Cliente) usuarioRepository.guardar(cliente);
    }
}
