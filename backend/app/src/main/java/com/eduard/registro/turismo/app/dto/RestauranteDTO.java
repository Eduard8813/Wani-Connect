package com.eduard.registro.turismo.app.dto;

import com.eduard.registro.turismo.app.model.ImagenRestaurante;
import com.eduard.registro.turismo.app.model.Restaurante;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class RestauranteDTO {
    
    @Valid
    private Restaurante restaurante;
    
    @NotEmpty(message = "Debe proporcionar al menos una imagen")
    private List<@Valid ImagenRestaurante> imagenes;

    // Getters y Setters
    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public List<ImagenRestaurante> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenRestaurante> imagenes) {
        this.imagenes = imagenes;
    }
}