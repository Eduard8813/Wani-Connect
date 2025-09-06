package com.eduard.registro.turismo.app.config;

// Importa la anotación para mapear propiedades desde application.properties o application.yml
import org.springframework.boot.context.properties.ConfigurationProperties;

// Marca esta clase como parte de la configuración del contexto de Spring
import org.springframework.context.annotation.Configuration;

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
@Data // Lombok genera automáticamente los métodos necesarios para esta clase
@Configuration // Registra esta clase como un bean de configuración en Spring
@ConfigurationProperties(prefix = "jwt") // Mapea propiedades que comienzan con "jwt" en application.properties
public class JwtConfig {

    // Clave secreta usada para firmar y verificar tokens JWT
    private String secret;

    // Tiempo de expiración del token en milisegundos
    private long expiration;
}
