package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "terminales_buses")
public class TerminalBus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, updatable = false)
    private String codigoUnico;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La localidad es obligatoria")
    private String localidad;
    
    @Embedded
    private UbicacionGeografica ubicacionGeografica;
    
    @OneToMany(mappedBy = "terminalBus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference  // Esto maneja la referencia circular
    private List<BusDisponible> busesDisponibles;
    
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public UbicacionGeografica getUbicacionGeografica() {
        return ubicacionGeografica;
    }

    public void setUbicacionGeografica(UbicacionGeografica ubicacionGeografica) {
        this.ubicacionGeografica = ubicacionGeografica;
    }

    public List<BusDisponible> getBusesDisponibles() {
        return busesDisponibles;
    }

    public void setBusesDisponibles(List<BusDisponible> busesDisponibles) {
        this.busesDisponibles = busesDisponibles;
    }

    // Generar código único automáticamente
    @PrePersist
    public void generarCodigoUnico() {
        this.codigoUnico = "TB-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
}