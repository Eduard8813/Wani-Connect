package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoUnico;

    @ManyToOne
    @JoinColumn(name = "sitio_turistico_id", nullable = false)
    private SitioTuristicos sitioTuristicos;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime fechaReserva;

    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion; // Nuevo campo para programar eliminación


    private boolean validada = false;

    // Getters y Setters
<<<<<<< HEAD
    @PrePersist
    public void generarCodigoUnico() {
        this.codigoUnico = "RES-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public TerminalBus getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalBus terminal) {
        this.terminal = terminal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLugarReservado() {
        return lugarReservado;
    }

    public void setLugarReservado(String lugarReservado) {
        this.lugarReservado = lugarReservado;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }
=======
    public String getCodigoUnico() { return codigoUnico; }
    public void setCodigoUnico(String codigoUnico) { this.codigoUnico = codigoUnico; }
    public String getEmailUsuario() { return email; }
    public void setEmailUsuario(String emailUsuario) { this.email = emailUsuario; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreUsuario() { return username; }
    public void setNombreUsuario(String nombreUsuario) { this.username = nombreUsuario; }
    public SitioTuristicos getSitioTuristicos() { return sitioTuristicos; }
    public void setSitioTuristicos(SitioTuristicos sitioTuristicos) { this.sitioTuristicos = sitioTuristicos; }
    public boolean isValidada() { return validada; }
    public void setValidada(boolean validada) { this.validada = validada; }
    public LocalDateTime getFechaEliminacion() { return fechaEliminacion; }
    public void setFechaEliminacion(LocalDateTime fechaEliminacion) { this.fechaEliminacion = fechaEliminacion; }
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b
}