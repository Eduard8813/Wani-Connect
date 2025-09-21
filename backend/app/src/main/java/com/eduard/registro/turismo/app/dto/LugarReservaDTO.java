package com.eduard.registro.turismo.app.dto;

import lombok.Data;

@Data
public class LugarReservaDTO {
    private Long id;
    private Integer numero;
    private boolean disponible;
    private String tiempoRestante;
    private Long busId;
    private String usuarioReserva;
}