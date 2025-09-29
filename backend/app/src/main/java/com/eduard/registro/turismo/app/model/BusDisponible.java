package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "buses_disponibles")
public class BusDisponible {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", nullable = false)
    @JsonBackReference  // Esto rompe la referencia circular
    private TerminalBus terminalBus;
    
    @NotBlank(message = "El número de bus es obligatorio")
    @Column(nullable = false)
    private String numeroBus;
    
    @NotBlank(message = "El destino es obligatorio")
    @Column(nullable = false)
    private String destino;
    
    @NotNull(message = "La hora de salida es obligatoria")
    @Column(nullable = false)
    private LocalTime horaSalida;
    
    @NotNull(message = "El total de lugares es obligatorio")
    @Min(value = 1, message = "Debe haber al menos un lugar")
    @Column(nullable = false)
    private Integer totalLugares;
    
    @NotNull(message = "Los lugares disponibles son obligatorios")
    @Min(value = 0, message = "Los lugares disponibles no pueden ser negativos")
    @Column(nullable = false)
    private Integer lugaresDisponibles;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private Double precio;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(name = "precio", nullable = false)
    private Double precio;
    
    // Getters y Setters
    public Long getId() {
        return id; 
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TerminalBus getTerminalBus() {
        return terminalBus;
    }

    public void setTerminalBus(TerminalBus terminalBus) {
        this.terminalBus = terminalBus;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}