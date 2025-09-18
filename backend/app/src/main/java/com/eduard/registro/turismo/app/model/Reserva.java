package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String codigoUnico;

    @ManyToOne
    @JoinColumn(name = "terminal_id", nullable = false)
    private TerminalBus terminal;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String lugarReservado;

    private LocalDateTime fechaReserva;

    private boolean confirmada = false;

    // Getters y Setters
    @PrePersist
    public void generarCodigoUnico() {
        this.codigoUnico = "RES-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }
    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }
    public Long getId() {
        return id;
    }
    public String getLugarReservado() {
        return lugarReservado;
    }
    public TerminalBus getTerminal() {
        return terminal;
    }
    public User getUser() {
        return user;
    }
    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }
    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }
    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setLugarReservado(String lugarReservado) {
        this.lugarReservado = lugarReservado;
    }
    public void setTerminal(TerminalBus terminal) {
        this.terminal = terminal;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public boolean isConfirmada() {
        return confirmada;
    }
}