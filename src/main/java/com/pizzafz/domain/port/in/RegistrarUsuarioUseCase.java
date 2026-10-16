package com.pizzafz.domain.port.in;

import com.pizzafz.domain.model.Cliente;

/**
 * Puerto de entrada (driving port). Define QUÉ se puede pedir al dominio,
 * no CÓMO se hace. El adaptador web (controller) depende de esta interfaz,
 * nunca de la implementación concreta.
 *
 * Este es el patrón que debes repetir para cada caso de uso del diagrama:
 * IniciarSesionUseCase, RealizarPedidoUseCase, RealizarPagoUseCase, etc.
 */
public interface RegistrarUsuarioUseCase {

    Cliente registrar(RegistrarUsuarioCommand comando);

    /**
     * Command object: agrupa los datos de entrada del caso de uso.
     * Evita firmas de método con muchos parámetros sueltos.
     */
    record RegistrarUsuarioCommand(
            String nombre,
            String email,
            String passwordPlano,
            String telefono,
            String direccionEntrega
    ) {}
}
