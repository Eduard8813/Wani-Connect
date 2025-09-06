package com.eduard.registro.turismo.app.dto;

// Lombok genera automáticamente getters, setters, toString, equals y hashCode
import lombok.Data;

/**
 * DTO que representa la respuesta enviada al cliente tras una autenticación exitosa.
 * Contiene el token JWT y el tipo de autenticación (Bearer).
 */
@Data // Lombok genera automáticamente los métodos necesarios para esta clase
public class AuthResponse {

    // Token JWT generado tras la autenticación
    private String token;

    // Tipo de autenticación, por defecto "Bearer"
    private String type = "Bearer";

    /**
     * Constructor que inicializa el token.
     * El tipo se establece automáticamente como "Bearer".
     */
    public AuthResponse(String token) {
        this.token = token;
    }
}
