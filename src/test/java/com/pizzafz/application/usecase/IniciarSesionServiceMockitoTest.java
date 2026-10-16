package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.CredencialesInvalidasException;
import com.pizzafz.domain.exception.UsuarioInactivoException;
import com.pizzafz.domain.exception.UsuarioNoEncontradoException;
import com.pizzafz.domain.model.Cliente;
import com.pizzafz.domain.model.Usuario;
import com.pizzafz.domain.port.in.IniciarSesionUseCase.IniciarSesionCommand;
import com.pizzafz.domain.port.out.PasswordHasherPort;
import com.pizzafz.domain.port.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias con Mockito: los puertos de salida (repositorio y hasher)
 * se reemplazan por mocks, así se prueba SOLO la lógica del caso de uso.
 */
@ExtendWith(MockitoExtension.class)
class IniciarSesionServiceMockitoTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private PasswordHasherPort passwordHasher;

    @InjectMocks
    private IniciarSesionService iniciarSesionService;

    private Cliente cliente(boolean activo) {
        return new Cliente(1L, "Ana Maria", "ana@gmail.com", "HASH", "3101234567",
                "Carrera 5 # 12-34", activo, LocalDateTime.now());
    }

    @Test
    @DisplayName("Mockito: inicia sesión cuando el repositorio encuentra al usuario y el hasher valida la clave")
    void debeIniciarSesionConMocks() {
        when(usuarioRepository.buscarPorEmail("ana@gmail.com")).thenReturn(Optional.of(cliente(true)));
        when(passwordHasher.verificar("secreto123", "HASH")).thenReturn(true);

        Usuario usuario = iniciarSesionService.iniciarSesion(new IniciarSesionCommand("ana@gmail.com", "secreto123"));

        assertEquals("ana@gmail.com", usuario.getEmail());
        verify(usuarioRepository).buscarPorEmail("ana@gmail.com");
        verify(passwordHasher).verificar("secreto123", "HASH");
    }

    @Test
    @DisplayName("Mockito: lanza UsuarioNoEncontradoException si el repositorio no devuelve nada")
    void debeLanzarUsuarioNoEncontrado() {
        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class,
                () -> iniciarSesionService.iniciarSesion(new IniciarSesionCommand("x@gmail.com", "123")));

        verifyNoInteractions(passwordHasher);
    }

    @Test
    @DisplayName("Mockito: lanza UsuarioInactivoException y nunca verifica la contraseña")
    void debeLanzarUsuarioInactivo() {
        when(usuarioRepository.buscarPorEmail("ana@gmail.com")).thenReturn(Optional.of(cliente(false)));

        assertThrows(UsuarioInactivoException.class,
                () -> iniciarSesionService.iniciarSesion(new IniciarSesionCommand("ana@gmail.com", "secreto123")));

        verify(passwordHasher, never()).verificar(anyString(), anyString());
    }

    @Test
    @DisplayName("Mockito: lanza CredencialesInvalidasException si el hasher rechaza la clave")
    void debeLanzarCredencialesInvalidas() {
        when(usuarioRepository.buscarPorEmail("ana@gmail.com")).thenReturn(Optional.of(cliente(true)));
        when(passwordHasher.verificar("mala", "HASH")).thenReturn(false);

        assertThrows(CredencialesInvalidasException.class,
                () -> iniciarSesionService.iniciarSesion(new IniciarSesionCommand("ana@gmail.com", "mala")));
    }
}
