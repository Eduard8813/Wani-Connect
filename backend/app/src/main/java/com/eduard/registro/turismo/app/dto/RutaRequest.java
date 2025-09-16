package com.eduard.registro.turismo.app.dto;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RutaRequest {
    private String origen; // Ejemplo: "12.13,-86.25"
    private List<String> destinos; // Ejemplo: ["12.15,-86.24", "12.16,-86.21"]
    // Puedes agregar campos extra según tus necesidades, como preferencias, tipo de ruta, etc.
}