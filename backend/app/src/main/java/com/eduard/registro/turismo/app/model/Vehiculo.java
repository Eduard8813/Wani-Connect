package com.eduard.registro.turismo.app.model;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String codigoUnico;
    
    private String marca;
    private String modelo;
    private String categoria;
    private int capacidad;
    
    @Enumerated(EnumType.STRING)
    private EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Conductor conductor;
    
    @PrePersist
    public void generarCodigoUnico() {
        if (codigoUnico == null) {
            codigoUnico = "VEH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}