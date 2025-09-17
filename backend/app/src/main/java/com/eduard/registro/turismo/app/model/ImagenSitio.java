package com.eduard.registro.turismo.app.model;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "imagenes_sitios")
public class ImagenSitio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "La URL de la imagen es obligatoria")
    @URL(message = "URL de imagen inválida")
    private String url;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sitio_turistico_id", nullable = false)
    private SitioTuristicos sitioTuristico;
    
    // Getters y Setters

    public Long getId() {
        return id;
    }
    public SitioTuristicos getSitioTuristico() {
        return sitioTuristico;
    }
    public String getUrl() {
        return url;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setSitioTuristico(SitioTuristicos sitioTuristico) {
        this.sitioTuristico = sitioTuristico;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}