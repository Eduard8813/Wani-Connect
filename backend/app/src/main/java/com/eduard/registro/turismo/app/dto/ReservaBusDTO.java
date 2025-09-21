package com.eduard.registro.turismo.app.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class ReservaBusDTO {
    
    @NotNull(message = "El ID de la terminal es obligatorio")
    private Long terminalId;
    
    @NotNull(message = "El ID del bus es obligatorio")
    private Long busId;
    
    // Para compatibilidad con frontend existente
    private Integer numeroLugar;
    
    // Para múltiples lugares
    private List<Integer> numerosLugar;
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String emailUsuario;
    
    // Getters y Setters
    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Integer getNumeroLugar() {
        return numeroLugar;
    }

    public void setNumeroLugar(Integer numeroLugar) {
        this.numeroLugar = numeroLugar;
    }

    public List<Integer> getNumerosLugar() {
        // Si solo hay un lugar, convertir a lista
        if (numerosLugar == null && numeroLugar != null) {
            return List.of(numeroLugar);
        }
        return numerosLugar;
    }

    public void setNumerosLugar(List<Integer> numerosLugar) {
        this.numerosLugar = numerosLugar;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            this.nombreUsuario = nombreUsuario.substring(0, 1).toUpperCase() + nombreUsuario.substring(1).toLowerCase();
        } else {
            this.nombreUsuario = nombreUsuario;
        }
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}