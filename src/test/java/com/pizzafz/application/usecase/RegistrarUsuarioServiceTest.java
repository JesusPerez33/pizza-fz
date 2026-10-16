package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.CuentaExistenteException;
import com.pizzafz.domain.exception.DatosInvalidosException;
import com.pizzafz.domain.model.Cliente;
import com.pizzafz.domain.model.Rol;
import com.pizzafz.domain.port.in.RegistrarUsuarioUseCase.RegistrarUsuarioCommand;
import com.pizzafz.domain.service.ValidadorUsuarioService;
import com.pizzafz.infrastructure.adapter.out.persistence.BCryptPasswordHasherAdapter;
import com.pizzafz.infrastructure.adapter.out.persistence.UsuarioRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrarUsuarioServiceTest {

    private RegistrarUsuarioService registrarUsuarioService;
    private UsuarioRepositoryAdapter usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryAdapter();
        BCryptPasswordHasherAdapter passwordHasher = new BCryptPasswordHasherAdapter();
        ValidadorUsuarioService validador = new ValidadorUsuarioService();

        registrarUsuarioService = new RegistrarUsuarioService(usuarioRepository, passwordHasher, validador);
    }

    @Test
    @DisplayName("Debe registrar un cliente correctamente con datos válidos")
    void debeRegistrarClienteCorrectamente() {
        RegistrarUsuarioCommand comando = new RegistrarUsuarioCommand(
                "Carlos Perez",
                "carlos@gmail.com",
                "password123",
                "3001234567",
                "Calle 10 # 20-30"
        );

        Cliente cliente = registrarUsuarioService.registrar(comando);

        assertNotNull(cliente.getId());
        assertEquals("Carlos Perez", cliente.getNombre());
        assertEquals("carlos@gmail.com", cliente.getEmail());
        assertEquals("Calle 10 # 20-30", cliente.getDireccionEntrega());
        assertEquals(Rol.CLIENTE, cliente.getRol());
        assertTrue(cliente.isActivo());
        assertNotNull(cliente.getFechaRegistro());
        assertNotEquals("password123", cliente.getPasswordHash());
    }

    @Test
    @DisplayName("Debe lanzar CuentaExistenteException si el email ya está registrado")
    void debeLanzarExcepcionEmailDuplicado() {
        RegistrarUsuarioCommand comando1 = new RegistrarUsuarioCommand(
                "Carlos Perez",
                "duplicado@gmail.com",
                "password123",
                "3001234567",
                "Calle 10 # 20-30"
        );
        registrarUsuarioService.registrar(comando1);

        RegistrarUsuarioCommand comando2 = new RegistrarUsuarioCommand(
                "Carlos Segundo",
                "duplicado@gmail.com",
                "password12345",
                "3007654321",
                "Calle 20 # 10-05"
        );

        CuentaExistenteException exception = assertThrows(
                CuentaExistenteException.class,
                () -> registrarUsuarioService.registrar(comando2)
        );

        assertTrue(exception.getMessage().contains("duplicado@gmail.com"));
    }

    @Test
    @DisplayName("Debe lanzar DatosInvalidosException si los datos ingresados son inválidos")
    void debeLanzarExcepcionDatosInvalidos() {
        RegistrarUsuarioCommand comandoInvalido = new RegistrarUsuarioCommand(
                "", // nombre vacío
                "email-invalido", // email sin @
                "123", // password corto
                "abc", // teléfono no numérico
                ""
        );

        DatosInvalidosException exception = assertThrows(
                DatosInvalidosException.class,
                () -> registrarUsuarioService.registrar(comandoInvalido)
        );

        assertFalse(exception.getErrores().isEmpty());
    }
}
