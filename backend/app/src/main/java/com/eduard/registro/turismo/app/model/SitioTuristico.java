package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class SitioTuristico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;
    private String ubicacion;
    private String horarioAtencion;
    private Float costoEntrada;

    @Column(unique = true)
    private String codigoUnico;

    @Column(length = 1000)
    private String historiaResumida;
    private String eventosHistoricos;
    private String personajesAsociados;

    private String audioguiaUrl;

    @ElementCollection
    private List<String> comentarios = new ArrayList<>();

    private String serviciosDisponibles;
    private String actividadesRecomendadas;
    private String nivelAccesibilidad;
    private String reglasLugar;
    private String enlaceReserva;

    // Relación con imágenes
    @OneToMany(mappedBy = "sitioTuristico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenTuristica> galeriaImagenes = new ArrayList<>();
}
