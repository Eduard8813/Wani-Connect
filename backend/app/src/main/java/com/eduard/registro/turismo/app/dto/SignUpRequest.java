package com.eduard.registro.turismo.app.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;
    
    @NotBlank
    @Size(min = 6)
    private String password;
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String firstName;
    
    private String lastName;
    private String phone;
    private String address;
    
    // Nuevos campos con validaciones
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe estar en el pasado")
    private LocalDate birthDate;
    
    @Pattern(regexp = "^[MF]$", message = "El género debe ser 'M' o 'F'")
    private String gender;
    
    @Size(max = 100, message = "La ubicación no puede exceder los 100 caracteres")
    private String location;
    
    @Size(max = 100, message = "El país de origen no puede exceder los 100 caracteres")
    private String countryOfOrigin;
    
    @Size(max = 50, message = "El idioma no puede exceder los 50 caracteres")
    private String language;
    
    @Size(max = 200, message = "Los intereses turísticos no pueden exceder los 200 caracteres")
    private String touristInterest;
    
    @Size(max = 100, message = "El perfil social no puede exceder los 100 caracteres")
    private String social;
    
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String description;
}