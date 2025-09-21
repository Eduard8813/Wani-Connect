package com.eduard.registro.turismo.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class ReservaDTO {
    private Long sitioTuristicoId;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String emailUsuario;

    // Getters y Setters
    public Long getSitioTuristicoId() { 
        return sitioTuristicoId; 
    }
    
    public void setSitioTuristicoId(Long sitioTuristicoId) { 
        this.sitioTuristicoId = sitioTuristicoId; 
    }
    
    public String getNombreUsuario() { 
        return nombreUsuario; 
    }
    
    public void setNombreUsuario(String nombreUsuario) { 
        this.nombreUsuario = nombreUsuario; 
    }
    
    public String getEmailUsuario() { 
        return emailUsuario; 
    }
    
    public void setEmailUsuario(String emailUsuario) { 
        this.emailUsuario = emailUsuario; 
    }
}