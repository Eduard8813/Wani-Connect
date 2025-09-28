package com.eduard.registro.turismo.app.dto;

import com.eduard.registro.turismo.app.model.Hospedaje;
import com.eduard.registro.turismo.app.model.ImagenHospedaje;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class HospedajeDTO {
    
    @Valid
    private Hospedaje hospedaje;
    
    @NotEmpty(message = "Debe proporcionar al menos una imagen")
    private List<@Valid ImagenHospedaje> imagenes;

    // Getters y Setters
    public Hospedaje getHospedaje() {
        return hospedaje;
    }

    public void setHospedaje(Hospedaje hospedaje) { 
        this.hospedaje = hospedaje;
    }

    public List<ImagenHospedaje> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenHospedaje> imagenes) {
        this.imagenes = imagenes;
    }
}