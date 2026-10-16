package com.pizzafz.infrastructure.adapter.out.persistence;

import com.pizzafz.domain.port.out.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasherAdapter implements PasswordHasherPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hashear(String passwordPlano) {
        return encoder.encode(passwordPlano);
    }

    @Override
    public boolean verificar(String passwordPlano, String passwordHash) {
        if (passwordPlano == null || passwordHash == null) {
            return false;
        }
        return encoder.matches(passwordPlano, passwordHash);
    }
}
