package com.eduard.registro.turismo.app.dto;

import lombok.Data;

@Data
public class ReservaDTO {
    private Long id;
    private String codigoUnico;
    private Long terminalId;
    private String terminalNombre;
    private Long userId;
    private String userName;
    private String lugarReservado;
    private String fechaReserva;
    private boolean confirmada;
}