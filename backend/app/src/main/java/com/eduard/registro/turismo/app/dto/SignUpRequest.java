package com.eduard.registro.turismo.app.dto;

// Anotaciones de validación para asegurar integridad de datos
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Lombok genera automáticamente getters, setters, toString, equals y hashCode
import lombok.Data;

/**
 * DTO que representa la solicitud de registro de un nuevo usuario.
 * Contiene campos básicos de cuenta y perfil, con validaciones para garantizar calidad de datos.
 */
@Data // Lombok genera automáticamente los métodos necesarios para esta clase
public class SignUpRequest {

    @NotBlank // Valida que el campo no esté vacío ni contenga solo espacios
    @Size(min = 4, max = 20) // Restringe el tamaño del nombre de usuario
    private String username;

    @NotBlank // Valida que la contraseña no esté vacía
    @Size(min = 6) // Requiere al menos 6 caracteres para mayor seguridad
    private String password;

    @NotBlank // Valida que el email no esté vacío
    @Email // Verifica que el formato del email sea válido
    private String email;

    @NotBlank // Valida que el nombre no esté vacío
    private String firstName;

    // Campos opcionales del perfil del usuario
    private String lastName;
    private String phone;
    private String address;
}
