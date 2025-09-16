package com.eduard.registro.turismo.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "conductores")
public class Conductor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(unique = true, nullable = false)
    private String codigoUnico;

     private String nombre;
    private String apellido;
    private String email;
    
    @Column(name = "telefono_oculto")
    private String telefono; // Almacenado con últimos 6 dígitos ocultos
    
    @OneToOne(mappedBy = "conductor", cascade = CascadeType.ALL)
    private Vehiculo vehiculoAsignado;
    
    @OneToMany(mappedBy = "conductor", cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();
    
    @PrePersist
    public void generarCodigoUnico() {
        if (codigoUnico == null) {
            codigoUnico = "COND-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}