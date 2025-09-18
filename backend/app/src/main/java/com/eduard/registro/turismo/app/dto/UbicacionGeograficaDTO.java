package com.eduard.registro.turismo.app.dto;

import com.eduard.registro.turismo.app.model.UbicacionGeografica;

public class UbicacionGeograficaDTO {
    private String direccion;
    private Double latitud;
    private Double longitud;
    
    public UbicacionGeograficaDTO() {}
    
    public UbicacionGeograficaDTO(UbicacionGeografica ubicacion) {
        this.direccion = ubicacion.getDireccion();
        this.latitud = ubicacion.getLatitud();
        this.longitud = ubicacion.getLongitud();
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public Double getLatitud() {
        return latitud;
    }
    
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
    
    public Double getLongitud() {
        return longitud;
    }
    
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}