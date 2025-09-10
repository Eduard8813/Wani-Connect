package com.eduard.registro.turismo.app.config;

// Importa la anotación @Configuration para indicar que esta clase contiene configuración de Spring
import org.springframework.context.annotation.Configuration;

// Importa interfaces para configurar CORS en aplicaciones web
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración que habilita CORS (Cross-Origin Resource Sharing) en la aplicación.
 * Esto permite que el frontend (por ejemplo, una app React en localhost:3000) pueda comunicarse
 * con el backend Spring Boot, incluso si están en dominios distintos.
 *
 * Esta clase es clave cuando trabajas con arquitecturas separadas (frontend-backend) y necesitas
 * evitar errores de política de origen cruzado en el navegador.
 */
@Configuration // Indica que esta clase se registra como configuración en el contexto de Spring
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Método que configura las reglas de CORS para todas las rutas de la aplicación.
     * Se ejecuta automáticamente al iniciar el contexto web.
     */
    @Override
        public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*") // ¡Corregido! Usa "*" en lugar de "**"
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false) // Compatible con "*"
            .maxAge(3600);
}
}
