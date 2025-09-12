package com.eduard.registro.turismo.app.config;

// Importa la anotación para mapear propiedades desde application.properties o application.yml
import org.springframework.boot.context.properties.ConfigurationProperties;

// Marca esta clase como parte de la configuración del contexto de Spring
import org.springframework.context.annotation.Configuration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
// Anotación de Lombok que genera automáticamente getters, setters, toString, equals y hashCode
import lombok.Data;

/**
 * Clase de configuración que encapsula las propiedades relacionadas con JWT (JSON Web Token).
 * Permite acceder de forma tipada y segura a valores definidos en el archivo de configuración,
 * como el secreto de firma y el tiempo de expiración del token.
 *
 * Esta clase es clave para mantener la seguridad desacoplada y configurable,
 * facilitando la validación y generación de tokens en tu arquitectura.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    @NotBlank(message = "La clave secreta no puede estar vacía")
    @Min(value = 32, message = "La clave secreta debe tener al menos 32 caracteres")
    private String secret;
    
    @Min(value = 3600000, message = "El tiempo de expiración debe ser al menos 1 hora")
    private long expiration = 86400000; // 24 horas por defecto
}