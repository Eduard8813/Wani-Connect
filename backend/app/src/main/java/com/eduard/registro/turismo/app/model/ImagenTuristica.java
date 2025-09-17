package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ImagenTuristica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10000) // Para base64 o URLs extensas
    private String galeriaImagen;

    @ManyToOne
    @JoinColumn(name = "sitio_turistico_id")
    private SitioTuristico sitioTuristico;
}
