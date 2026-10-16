package com.pizzafz.domain.port.out;

/**
 * Puerto de salida: el dominio necesita "hashear" una contraseña, pero no
 * le importa si por debajo se usa BCrypt, Argon2, etc. Eso se decide en
 * el adaptador de infraestructura.
 */
public interface PasswordHasherPort {
    String hashear(String passwordPlano);
    boolean verificar(String passwordPlano, String passwordHash);
}
