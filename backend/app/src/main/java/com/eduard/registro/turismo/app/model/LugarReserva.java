package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lugares_reserva")
public class LugarReserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private BusDisponible bus;
    
    @Column(nullable = false)
    private Integer numero;
    
    @Column(nullable = false)
    private boolean disponible = true;
    
    @Column(name = "fecha_reserva")
    private LocalDateTime fechaReserva;
    
    @Column(name = "usuario_reserva")
    private String usuarioReserva;
    
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusDisponible getBus() {
        return bus;
    }

    public void setBus(BusDisponible bus) {
        this.bus = bus;
    }

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

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getUsuarioReserva() {
        return usuarioReserva;
    }

    public void setUsuarioReserva(String usuarioReserva) {
        this.usuarioReserva = usuarioReserva;
    }
}