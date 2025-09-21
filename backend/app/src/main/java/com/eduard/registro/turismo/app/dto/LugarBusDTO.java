package com.eduard.registro.turismo.app.dto;

public class LugarBusDTO {
    
    private Integer numero;
    private boolean disponible;
    private String tiempoRestante;
    private String tiempoLiberacion;
    private boolean liberada;
    
    public LugarBusDTO(Integer numero, boolean disponible) {
        this.numero = numero;
        this.disponible = disponible;
    }
    
    public LugarBusDTO(Integer numero, boolean disponible, String tiempoRestante) {
        this.numero = numero;
        this.disponible = disponible;
        this.tiempoRestante = tiempoRestante;
    }
    
    public LugarBusDTO(Integer numero, boolean disponible, String tiempoRestante, String tiempoLiberacion, boolean liberada) {
        this.numero = numero;
        this.disponible = disponible;
        this.tiempoRestante = tiempoRestante;
        this.tiempoLiberacion = tiempoLiberacion;
        this.liberada = liberada;
    }
    
    // Getters y Setters
    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(String tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }
    
    public String getTiempoLiberacion() {
        return tiempoLiberacion;
    }

    public void setTiempoLiberacion(String tiempoLiberacion) {
        this.tiempoLiberacion = tiempoLiberacion;
    }

    public boolean isLiberada() {
        return liberada;
    }

    public void setLiberada(boolean liberada) {
        this.liberada = liberada;
    }
}