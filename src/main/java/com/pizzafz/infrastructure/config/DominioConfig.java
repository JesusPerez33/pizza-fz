package com.pizzafz.infrastructure.config;

import com.pizzafz.domain.service.ValidadorCarritoService;
import com.pizzafz.domain.service.ValidadorPizzaService;
import com.pizzafz.domain.service.ValidadorUsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Los servicios de DOMINIO no deberían tener anotaciones de Spring
 * (@Service, @Component) porque el dominio debe ser independiente del
 * framework. Aquí se los registra como beans desde infraestructura,
 * manteniendo el dominio "limpio".
 */
@Configuration
public class DominioConfig {

    @Bean
    public ValidadorUsuarioService validadorUsuarioService() {
        return new ValidadorUsuarioService();
    }

    @Bean
    public ValidadorPizzaService validadorPizzaService() {
        return new ValidadorPizzaService();
    }

    @Bean
    public ValidadorCarritoService validadorCarritoService() {
        return new ValidadorCarritoService();
    }
}
