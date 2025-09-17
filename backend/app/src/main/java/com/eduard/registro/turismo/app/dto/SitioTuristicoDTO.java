package com.eduard.registro.turismo.app.dto;
import com.eduard.registro.turismo.app.model.ImagenSitio;
import com.eduard.registro.turismo.app.model.SitioTuristicos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class SitioTuristicoDTO {
    
    @Valid
    private SitioTuristicos sitioTuristico;
    
    @NotEmpty(message = "Debe proporcionar al menos una imagen")
    private List<@Valid ImagenSitio> imagenes;

    // Getters y Setters
    public SitioTuristicos getSitioTuristico() {
        return sitioTuristico;
    }

    public void setSitioTuristico(SitioTuristicos sitioTuristico) {
        this.sitioTuristico = sitioTuristico;
    }

    public List<ImagenSitio> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenSitio> imagenes) {
        this.imagenes = imagenes;
    }
}