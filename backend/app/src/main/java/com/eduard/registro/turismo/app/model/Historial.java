package com.eduard.registro.turismo.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

// Historial.java
@Entity
@Data
@NoArgsConstructor
public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long idUsuario;
    private String tokenVerificacion;
    private LocalDateTime fechaReserva;
    
    @ManyToOne
    private Conductor conductor;
    
    @ManyToOne
    private Vehiculo vehiculo;
}
