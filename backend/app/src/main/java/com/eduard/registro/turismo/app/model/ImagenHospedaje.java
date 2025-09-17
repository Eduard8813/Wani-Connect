package com.eduard.registro.turismo.app.model;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "imagenes_hospedajes")
public class ImagenHospedaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "La URL de la imagen es obligatoria")
    @URL(message = "URL de imagen inválida")
    private String url;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospedaje_id", nullable = false)
    private Hospedaje hospedaje;
    
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
    
    public Hospedaje getHospedaje() {
        return hospedaje;
    }
    
    public void setHospedaje(Hospedaje hospedaje) {
        this.hospedaje = hospedaje;
    }
}