package com.eduard.registro.turismo.app.dto;

import lombok.Data;

@Data
public class UbicacionTerminalDTO {
    private String nombre;
    private String localidad;
    private Double latitud;
    private Double longitud;
    private String urlImagen;
}