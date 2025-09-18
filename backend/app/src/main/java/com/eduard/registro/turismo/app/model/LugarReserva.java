package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lugares_reserva")
public class LugarReserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "terminal_id", nullable = false)
    private TerminalBus terminal;

    private String nombre;
    private boolean disponible = true;

    // Getters y Setters

    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public TerminalBus getTerminal() {
        return terminal;
    }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setTerminal(TerminalBus terminal) {
        this.terminal = terminal;
    }
    public boolean isDisponible() {
        return disponible;
    }
}
