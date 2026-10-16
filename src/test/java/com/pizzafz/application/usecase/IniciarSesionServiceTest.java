package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.CredencialesInvalidasException;
import com.pizzafz.domain.exception.UsuarioInactivoException;
import com.pizzafz.domain.exception.UsuarioNoEncontradoException;
import com.pizzafz.domain.model.Cliente;
import com.pizzafz.domain.model.Rol;
import com.pizzafz.domain.model.Usuario;
import com.pizzafz.domain.port.in.IniciarSesionUseCase.IniciarSesionCommand;
import com.pizzafz.domain.service.ValidadorUsuarioService;
import com.pizzafz.infrastructure.adapter.out.persistence.BCryptPasswordHasherAdapter;
import com.pizzafz.infrastructure.adapter.out.persistence.UsuarioRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IniciarSesionServiceTest {

    private IniciarSesionService iniciarSesionService;
    private UsuarioRepositoryAdapter usuarioRepository;
    private BCryptPasswordHasherAdapter passwordHasher;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryAdapter();
        passwordHasher = new BCryptPasswordHasherAdapter();
        iniciarSesionService = new IniciarSesionService(usuarioRepository, passwordHasher);
    }

    @Test
    @DisplayName("Debe iniciar sesión exitosamente con credenciales correctas")
    void debeIniciarSesionCorrectamente() {
        String passHash = passwordHasher.hashear("secreto123");
        Cliente cliente = new Cliente(null, "Ana Maria", "ana@gmail.com", passHash, "3101234567", "Carrera 5 # 12-34", true, java.time.LocalDateTime.now());
        usuarioRepository.guardar(cliente);

        IniciarSesionCommand comando = new IniciarSesionCommand("ana@gmail.com", "secreto123");

        Usuario usuarioAutenticado = iniciarSesionService.iniciarSesion(comando);

        assertNotNull(usuarioAutenticado);
        assertEquals("ana@gmail.com", usuarioAutenticado.getEmail());
        assertEquals(Rol.CLIENTE, usuarioAutenticado.getRol());
    }

    @Test
    @DisplayName("Debe lanzar UsuarioNoEncontradoException si el usuario no existe")
    void debeLanzarExcepcionUsuarioNoExistente() {
        IniciarSesionCommand comando = new IniciarSesionCommand("noexiste@gmail.com", "secreto123");

        UsuarioNoEncontradoException exception = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> iniciarSesionService.iniciarSesion(comando)
        );

        assertEquals("Usuario no registrado", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar CredencialesInvalidasException si la contraseña es incorrecta")
    void debeLanzarExcepcionPasswordIncorrecto() {
        String passHash = passwordHasher.hashear("secreto123");
        Cliente cliente = new Cliente(null, "Ana Maria", "ana@gmail.com", passHash, "3101234567", "Carrera 5 # 12-34", true, java.time.LocalDateTime.now());
        usuarioRepository.guardar(cliente);

        IniciarSesionCommand comando = new IniciarSesionCommand("ana@gmail.com", "passwordErrado");

        CredencialesInvalidasException exception = assertThrows(
                CredencialesInvalidasException.class,
                () -> iniciarSesionService.iniciarSesion(comando)
        );

        assertEquals("Credenciales incorrectas", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar UsuarioInactivoException si el usuario está inactivo")
    void debeLanzarExcepcionUsuarioInactivo() {
        String passHash = passwordHasher.hashear("secreto123");
        Cliente cliente = new Cliente(null, "Ana Inactiva", "inactiva@gmail.com", passHash, "3101234567", "Carrera 5 # 12-34", false, java.time.LocalDateTime.now());
        usuarioRepository.guardar(cliente);

        IniciarSesionCommand comando = new IniciarSesionCommand("inactiva@gmail.com", "secreto123");

        UsuarioInactivoException exception = assertThrows(
                UsuarioInactivoException.class,
                () -> iniciarSesionService.iniciarSesion(comando)
        );

        assertEquals("Usuario inactivo", exception.getMessage());
    }
}
