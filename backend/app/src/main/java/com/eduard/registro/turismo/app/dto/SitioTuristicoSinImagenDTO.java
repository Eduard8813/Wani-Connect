package com.eduard.registro.turismo.app.dto;


import lombok.Data;

@Data
public class SitioTuristicoSinImagenDTO {
    private String nombre;
    private String tipo;
    private String ubicacion;
    private String horarioAtencion;
    private Float costoEntrada;
    private String codigoUnico;

    private String historiaResumida;
    private String eventosHistoricos;
    private String personajesAsociados;

    private String audioguiaUrl;
    private String serviciosDisponibles;
    private String actividadesRecomendadas;
    private String nivelAccesibilidad;
    private String reglasLugar;
    private String enlaceReserva;
}
