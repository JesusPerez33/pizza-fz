package com.pizzafz.infrastructure.adapter.in.web;

import com.pizzafz.domain.exception.CredencialesInvalidasException;
import com.pizzafz.domain.exception.UsuarioInactivoException;
import com.pizzafz.domain.exception.UsuarioNoEncontradoException;
import com.pizzafz.domain.model.Usuario;
import com.pizzafz.domain.port.in.IniciarSesionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IniciarSesionUseCase iniciarSesionUseCase;

    public AuthController(IniciarSesionUseCase iniciarSesionUseCase) {
        this.iniciarSesionUseCase = iniciarSesionUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginRequest request) {
        Usuario usuario = iniciarSesionUseCase.iniciarSesion(
                new IniciarSesionUseCase.IniciarSesionCommand(request.email(), request.password())
        );
        return ResponseEntity.ok(Map.of(
                "mensaje", "Inicio de sesión exitoso",
                "usuario", Map.of(
                        "id", usuario.getId(),
                        "nombre", usuario.getNombre(),
                        "email", usuario.getEmail(),
                        "rol", usuario.getRol(),
                        "activo", usuario.isActivo()
                )
        ));
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<?> manejarUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(UsuarioInactivoException.class)
    public ResponseEntity<?> manejarUsuarioInactivo(UsuarioInactivoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<?> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", ex.getMessage()));
    }

    public record LoginRequest(
            String email,
            String password
    ) {}
}
