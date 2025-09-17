package com.eduard.registro.turismo.app.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Embeddable
class UbicacionGeografica {
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    
    @DecimalMin(value = "-90.0", message = "Latitud inválida")
    @DecimalMax(value = "90.0", message = "Latitud inválida")
    private Double latitud;
    
    @DecimalMin(value = "-180.0", message = "Longitud inválida")
    @DecimalMax(value = "180.0", message = "Longitud inválida")
    private Double longitud;
    
    // Getters y Setters

    public String getDireccion() {
        return direccion;
    }
    public Double getLatitud() {
        return latitud;
    }
    public Double getLongitud() {
        return longitud;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}