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

    // Datos simbólicos/culturales
    @Column(length = 1000)
    private String historiaResumida;
    private String eventosHistoricos;
    private String personajesAsociados;

    @ElementCollection
    private List<String> galeriaImagenes = new ArrayList<>(); // URLs/base64

    private String audioguiaUrl;

    @ElementCollection
    private List<String> comentarios = new ArrayList<>();

    // Datos funcionales
    private String serviciosDisponibles;
    private String actividadesRecomendadas;
    private String nivelAccesibilidad;
    private String reglasLugar;
    private String enlaceReserva;
}