package com.eduard.registro.turismo.app.dto;

import java.time.LocalTime;

import com.eduard.registro.turismo.app.model.BusDisponible;

public class BusDisponibleDTO {
    private Long id;
    private String numeroBus;
    private String destino;
    private LocalTime horaSalida;
    private Integer totalLugares;
    private Integer lugaresDisponibles;
    // Nota: No incluimos la referencia a TerminalBus para evitar la circularidad
    
    // Constructor, getters y setters
    public BusDisponibleDTO() {}
    
    // Constructor que recibe una entidad BusDisponible
    public BusDisponibleDTO(BusDisponible bus) {
        this.id = bus.getId();
        this.numeroBus = bus.getNumeroBus();
        this.destino = bus.getDestino();
        this.horaSalida = bus.getHoraSalida();
        this.totalLugares = bus.getTotalLugares();
        this.lugaresDisponibles = bus.getLugaresDisponibles();
    }
    
    // Getters y setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroBus() {
        return numeroBus;
    }

    public void setNumeroBus(String numeroBus) {
        this.numeroBus = numeroBus;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Integer getTotalLugares() {
        return totalLugares;
    }

    public void setTotalLugares(Integer totalLugares) {
        this.totalLugares = totalLugares;
    }

    public Integer getLugaresDisponibles() {
        return lugaresDisponibles;
    }

    public void setLugaresDisponibles(Integer lugaresDisponibles) {
        this.lugaresDisponibles = lugaresDisponibles;
    }
}