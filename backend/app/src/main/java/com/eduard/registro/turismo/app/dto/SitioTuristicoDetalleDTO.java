package com.eduard.registro.turismo.app.dto;

import java.util.List;

public class SitioTuristicoDetalleDTO {
    private Long id;
    private String nombre;
    private String tipoLugar;
    private String direccion;
    private String horarioAtencion;
    private Double costoEntrada;
    private String historiaResumida;
    private String eventosHistoricos;
    private String personajesAsociados;
    private String audioguias;
    private String serviciosDisponibles;
    private String actividadesRecomendadas;
    private String nivelAccesibilidad;
    private String reglasLugar;
    private String enlaceReserva;
    private String codigoUnico;
    private List<ImagenDTO> imagenes;
    private Double latitud;
    private Double longitud;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public Double getCostoEntrada() {
        return costoEntrada;
    }

    public void setCostoEntrada(Double costoEntrada) {
        this.costoEntrada = costoEntrada;
    }

    public String getHistoriaResumida() {
        return historiaResumida;
    }

    public void setHistoriaResumida(String historiaResumida) {
        this.historiaResumida = historiaResumida;
    }

    public String getEventosHistoricos() {
        return eventosHistoricos;
    }

    public void setEventosHistoricos(String eventosHistoricos) {
        this.eventosHistoricos = eventosHistoricos;
    }

    public String getPersonajesAsociados() {
        return personajesAsociados;
    }

    public void setPersonajesAsociados(String personajesAsociados) {
        this.personajesAsociados = personajesAsociados;
    }

    public String getAudioguias() {
        return audioguias;
    }

    public void setAudioguias(String audioguias) {
        this.audioguias = audioguias;
    }

    public String getServiciosDisponibles() {
        return serviciosDisponibles;
    }

    public void setServiciosDisponibles(String serviciosDisponibles) {
        this.serviciosDisponibles = serviciosDisponibles;
    }

    public String getActividadesRecomendadas() {
        return actividadesRecomendadas;
    }

    public void setActividadesRecomendadas(String actividadesRecomendadas) {
        this.actividadesRecomendadas = actividadesRecomendadas;
    }

    public String getNivelAccesibilidad() {
        return nivelAccesibilidad;
    }

    public void setNivelAccesibilidad(String nivelAccesibilidad) {
        this.nivelAccesibilidad = nivelAccesibilidad;
    }

    public String getReglasLugar() {
        return reglasLugar;
    }

    public void setReglasLugar(String reglasLugar) {
        this.reglasLugar = reglasLugar;
    }

    public String getEnlaceReserva() {
        return enlaceReserva;
    }

    public void setEnlaceReserva(String enlaceReserva) {
        this.enlaceReserva = enlaceReserva;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public List<ImagenDTO> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenDTO> imagenes) {
        this.imagenes = imagenes;
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