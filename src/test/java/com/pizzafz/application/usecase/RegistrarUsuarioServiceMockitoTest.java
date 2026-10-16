package com.pizzafz.application.usecase;

import com.pizzafz.domain.exception.CuentaExistenteException;
import com.pizzafz.domain.exception.DatosInvalidosException;
import com.pizzafz.domain.model.Cliente;
import com.pizzafz.domain.model.Usuario;
import com.pizzafz.domain.port.in.RegistrarUsuarioUseCase.RegistrarUsuarioCommand;
import com.pizzafz.domain.port.out.PasswordHasherPort;
import com.pizzafz.domain.port.out.UsuarioRepositoryPort;
import com.pizzafz.domain.service.ValidadorUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioServiceMockitoTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private PasswordHasherPort passwordHasher;

    @Mock
    private ValidadorUsuarioService validador;

    @InjectMocks
    private RegistrarUsuarioService registrarUsuarioService;

    private final RegistrarUsuarioCommand comando = new RegistrarUsuarioCommand(
            "Carlos Perez", "carlos@gmail.com", "password123", "3001234567", "Calle 10 # 20-30");

    @Test
    @DisplayName("Mockito: registra el cliente guardando la contraseña hasheada por el puerto")
    void debeRegistrarClienteConPasswordHasheado() {
        when(usuarioRepository.buscarPorEmail("carlos@gmail.com")).thenReturn(Optional.empty());
        when(passwordHasher.hashear("password123")).thenReturn("HASH");
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente cliente = registrarUsuarioService.registrar(comando);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).guardar(captor.capture());
        assertEquals("HASH", captor.getValue().getPasswordHash());
        assertEquals("carlos@gmail.com", cliente.getEmail());
        verify(validador).validarCliente("Carlos Perez", "carlos@gmail.com", "password123",
                "3001234567", "Calle 10 # 20-30");
    }

    @Test
    @DisplayName("Mockito: lanza CuentaExistenteException y no guarda si el email ya existe")
    void debeLanzarCuentaExistente() {
        Cliente existente = new Cliente(1L, "Otro", "carlos@gmail.com", "X", "3000000000",
                "Calle 1", true, LocalDateTime.now());
        when(usuarioRepository.buscarPorEmail("carlos@gmail.com")).thenReturn(Optional.of(existente));

        assertThrows(CuentaExistenteException.class, () -> registrarUsuarioService.registrar(comando));

        verify(usuarioRepository, never()).guardar(any());
        verifyNoInteractions(passwordHasher);
    }

    @Test
    @DisplayName("Mockito: si el validador falla no se consulta el repositorio ni se hashea")
    void debePropagarDatosInvalidos() {
        doThrow(new DatosInvalidosException(List.of("el nombre es obligatorio")))
                .when(validador).validarCliente(any(), anyString(), anyString(), anyString(), anyString());

        assertThrows(DatosInvalidosException.class, () -> registrarUsuarioService.registrar(comando));

        verifyNoInteractions(usuarioRepository, passwordHasher);
    }
}
