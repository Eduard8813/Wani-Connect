package com.eduard.registro.turismo.app.dto;

import com.eduard.registro.turismo.app.model.AlquilerVehiculo;
import com.eduard.registro.turismo.app.model.ImagenAlquilerVehiculo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class AlquilerVehiculoDTO {
    
    @Valid
    private AlquilerVehiculo alquilerVehiculo;
    
    @NotEmpty(message = "Debe proporcionar al menos una imagen")
    private List<@Valid ImagenAlquilerVehiculo> imagenes;

    // Getters y Setters
    public AlquilerVehiculo getAlquilerVehiculo() {
        return alquilerVehiculo;
    }

    public void setAlquilerVehiculo(AlquilerVehiculo alquilerVehiculo) {
        this.alquilerVehiculo = alquilerVehiculo;
    }

    public List<ImagenAlquilerVehiculo> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenAlquilerVehiculo> imagenes) {
        this.imagenes = imagenes;
    }
}