package com.eduard.registro.turismo.app.dto;

// Anotación de validación que asegura que el campo no esté vacío ni en blanco
import jakarta.validation.constraints.NotBlank;

// Lombok genera automáticamente getters, setters, toString, equals y hashCode
import lombok.Data;

/**
 * DTO que representa la solicitud de autenticación.
 * Contiene los campos necesarios para iniciar sesión: username y password.
 * Ambos campos están validados para evitar entradas vacías.
 */
@Data // Lombok genera automáticamente los métodos necesarios para esta clase
public class AuthRequest {

    @NotBlank // Valida que el nombre de usuario no esté vacío ni contenga solo espacios
    private String username;
    
    @NotBlank // Valida que la contraseña no esté vacía ni contenga solo espacios
    private String password; 
}
