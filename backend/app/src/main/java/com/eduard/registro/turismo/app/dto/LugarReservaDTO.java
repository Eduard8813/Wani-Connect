package com.eduard.registro.turismo.app.dto;

import lombok.Data;

@Data
public class LugarReservaDTO {
    private Long id;
    private String nombre;
    private boolean disponible;
    private Long terminalId;
}
