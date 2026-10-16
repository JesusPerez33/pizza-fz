package com.pizzafz.infrastructure.adapter.in.web;

import com.pizzafz.domain.exception.CuentaExistenteException;
import com.pizzafz.domain.exception.DatosInvalidosException;
import com.pizzafz.domain.model.Cliente;
import com.pizzafz.domain.port.in.RegistrarUsuarioUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Adaptador de entrada (driving adapter). Traduce HTTP <-> dominio.
 * Depende SOLO del puerto de entrada (RegistrarUsuarioUseCase),
 * nunca de RegistrarUsuarioService directamente.
 */
@RestController
@RequestMapping("/api/clientes")
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    public UsuarioController(RegistrarUsuarioUseCase registrarUsuarioUseCase) {
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarse(@RequestBody RegistroRequest request) {
        Cliente cliente = registrarUsuarioUseCase.registrar(
                new RegistrarUsuarioUseCase.RegistrarUsuarioCommand(
                        request.nombre(),
                        request.email(),
                        request.password(),
                        request.telefono(),
                        request.direccionEntrega()
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    // --- traducción de excepciones de dominio a respuestas HTTP ---
    // esto es lo que en el frontend dispara "corregir datos" o "informar de cuenta existente"

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<?> manejarDatosInvalidos(DatosInvalidosException ex) {
        return ResponseEntity.badRequest().body(Map.of("errores", ex.getErrores()));
    }

    @ExceptionHandler(CuentaExistenteException.class)
    public ResponseEntity<?> manejarCuentaExistente(CuentaExistenteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", ex.getMessage()));
    }

    public record RegistroRequest(
            String nombre,
            String email,
            String password,
            String telefono,
            String direccionEntrega
    ) {}
}
