package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas_bus")
public class ReservaBus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String codigoUnico;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", nullable = false)
    private TerminalBus terminal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private BusDisponible bus;
    
    @NotNull(message = "El número de lugar es obligatorio")
    @Column(nullable = false)
    private Integer numeroLugar;
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Column(nullable = false)
    private String nombreUsuario;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false)
    private String emailUsuario;
    
    @Column(nullable = false)
    private LocalDateTime fechaReserva;
    
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;
    
    @Column(nullable = false)
    private boolean validada = false;
    
    @Column
    private LocalDateTime fechaLiberacion;
    
    // Getters y Setters
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

    public BusDisponible getBus() {
        return bus;
    }

    public void setBus(BusDisponible bus) {
        this.bus = bus;
    }

    public Integer getNumeroLugar() {
        return numeroLugar;
    }

    public void setNumeroLugar(Integer numeroLugar) {
        this.numeroLugar = numeroLugar;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public boolean isValidada() {
        return validada;
    }

    public void setValidada(boolean validada) {
        this.validada = validada;
    }
    
    public LocalDateTime getFechaLiberacion() {
        return fechaLiberacion;
    }

    public void setFechaLiberacion(LocalDateTime fechaLiberacion) {
        this.fechaLiberacion = fechaLiberacion;
    }


    
    @PrePersist
    public void validarAntesDePersistir() {
        if (this.fechaReserva == null) {
            this.fechaReserva = LocalDateTime.now();
        }
    }
}