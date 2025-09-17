package com.eduard.registro.turismo.app.model;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "imagenes_alquiler_vehiculos")
public class ImagenAlquilerVehiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "La URL de la imagen es obligatoria")
    @URL(message = "URL de imagen inválida")
    private String url;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alquiler_vehiculo_id", nullable = false)
    private AlquilerVehiculo alquilerVehiculo;
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public AlquilerVehiculo getAlquilerVehiculo() {
        return alquilerVehiculo;
    }
    
    public void setAlquilerVehiculo(AlquilerVehiculo alquilerVehiculo) {
        this.alquilerVehiculo = alquilerVehiculo;
    }
}