package com.eduard.registro.turismo.app.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ReservaRequest {
    @NotNull(message = "El ID del terminal es obligatorio")
    private Long terminalId;
    
    @NotNull(message = "El ID del bus es obligatorio")
    private Long busId;
    
    @NotNull(message = "El número de lugar es obligatorio")
    private Integer numeroLugar;
}