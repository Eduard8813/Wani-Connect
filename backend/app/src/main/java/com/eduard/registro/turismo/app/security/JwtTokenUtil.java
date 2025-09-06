package com.eduard.registro.turismo.app.security;

// Importa la configuración personalizada de JWT
import com.eduard.registro.turismo.app.config.JwtConfig;

// Librería JJWT para construir y analizar tokens
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

// Lombok para generar constructor con campos final
import lombok.RequiredArgsConstructor;

// Spring Security para acceder a los datos del usuario
import org.springframework.security.core.userdetails.UserDetails;

// Marca esta clase como componente para que Spring la detecte
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Componente que gestiona la generación, validación y análisis de tokens JWT.
 * Utiliza la configuración definida en JwtConfig y la librería JJWT.
 */
@Component // Registra esta clase como bean en el contexto de Spring
@RequiredArgsConstructor // Lombok genera constructor con los atributos final
public class JwtTokenUtil {

    // Configuración de JWT (clave secreta y expiración)
    private final JwtConfig jwtConfig;

    /**
     * Método privado que genera la clave de firma para el token.
     * Si la clave configurada es muy corta, genera una clave segura automáticamente.
     */
    private SecretKey getSigningKey() {
        if (jwtConfig.getSecret() == null || jwtConfig.getSecret().length() < 32) {
            return Keys.secretKeyFor(SignatureAlgorithm.HS256); // Clave segura generada automáticamente
        }

        // Convierte la cadena configurada en bytes y genera la clave HMAC
        byte[] keyBytes = jwtConfig.getSecret().getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un token JWT para el usuario autenticado.
     * Incluye el nombre de usuario, fecha de emisión y expiración.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Establece el sujeto del token (username)
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration())) // Fecha de expiración
                .signWith(getSigningKey()) // Firma el token con la clave segura
                .compact(); // Construye el token
    }

    /**
     * Extrae el nombre de usuario desde un token JWT.
     * Utiliza la clave de firma para validar el token antes de extraer datos.
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Establece la clave para validar la firma
                .build()
                .parseClaimsJws(token) // Analiza el token
                .getBody()
                .getSubject(); // Devuelve el sujeto (username)
    }

    /**
     * Valida si el token es auténtico y pertenece al usuario proporcionado.
     * También verifica que no haya expirado.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Verifica si el token ha expirado comparando su fecha de expiración con la actual.
     */
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date()); // Devuelve true si la fecha de expiración ya pasó
    }
}
