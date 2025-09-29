package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "sitios_turisticos")
public class SitioTuristicos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, updatable = false)
    private String codigoUnico; 
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El tipo de lugar es obligatorio")
    private String tipoLugar;
    
    @Embedded
    private UbicacionGeografica ubicacionGeografica;
    
    @NotBlank(message = "El horario es obligatorio")
    private String horarioAtencion;
    
    @DecimalMin(value = "0.0", message = "El costo debe ser positivo")
    private Double costoEntrada;
    
    @Column(length = 10000)
    private String historiaResumida;
    
    @Column(length = 10000)
    private String eventosHistoricos;
    
    @Column(length = 10000)
    private String personajesAsociados;
    
    @Column(length = 10000)
    private String audioguias;
    
    @Column(length = 10000)
    private String serviciosDisponibles;
    
    @Column(length = 10000)
    private String actividadesRecomendadas;
    
    private String nivelAccesibilidad;
    
    @Column(length = 10000)
    private String reglasLugar;
    
    @URL(message = "URL de reserva inválida")
    private String enlaceReserva;
    
    @OneToMany(mappedBy = "sitioTuristico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenSitio> imagenes;

    @PrePersist
    public void generarCodigoUnico() {
        this.codigoUnico = "ST-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getActividadesRecomendadas() {
        return actividadesRecomendadas;
    }
    
    public String getAudioguias() {
        return audioguias;
    }
    
    public String getCodigoUnico() {
        return codigoUnico;
    }
    
    public Double getCostoEntrada() {
        return costoEntrada;
    }
    
    public String getEnlaceReserva() {
        return enlaceReserva;
    }
    
    public String getEventosHistoricos() {
        return eventosHistoricos;
    }
    
    public String getHistoriaResumida() {
        return historiaResumida;
    }
    
    public String getHorarioAtencion() {
        return horarioAtencion;
    }
    
    public List<ImagenSitio> getImagenes() {
        return imagenes;
    }
    
    public String getNivelAccesibilidad() {
        return nivelAccesibilidad;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getPersonajesAsociados() {
        return personajesAsociados;
    }
    
    public String getReglasLugar() {
        return reglasLugar;
    }
    
    public String getServiciosDisponibles() {
        return serviciosDisponibles;
    }
    
    public String getTipoLugar() {
        return tipoLugar;
    }
    
    public UbicacionGeografica getUbicacionGeografica() {
        return ubicacionGeografica;
    }
    
    public void setActividadesRecomendadas(String actividadesRecomendadas) {
        this.actividadesRecomendadas = actividadesRecomendadas;
    }
    
    public void setAudioguias(String audioguias) {
        this.audioguias = audioguias;
    }
    
    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }
    
    public void setCostoEntrada(Double costoEntrada) {
        this.costoEntrada = costoEntrada;
    }
    
    public void setEnlaceReserva(String enlaceReserva) {
        this.enlaceReserva = enlaceReserva;
    }
    
    public void setEventosHistoricos(String eventosHistoricos) {
        this.eventosHistoricos = eventosHistoricos;
    }
    
    public void setHistoriaResumida(String historiaResumida) {
        this.historiaResumida = historiaResumida;
    }
    
    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }
    
    public void setImagenes(List<ImagenSitio> imagenes) {
        this.imagenes = imagenes;
    }
    
    public void setNivelAccesibilidad(String nivelAccesibilidad) {
        this.nivelAccesibilidad = nivelAccesibilidad;
    }
    
    public void setPersonajesAsociados(String personajesAsociados) {
        this.personajesAsociados = personajesAsociados;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setReglasLugar(String reglasLugar) {
        this.reglasLugar = reglasLugar;
    }
    
    public void setServiciosDisponibles(String serviciosDisponibles) {
        this.serviciosDisponibles = serviciosDisponibles;
    }
    
    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
    }
    
    public void setUbicacionGeografica(UbicacionGeografica ubicacionGeografica) {
        this.ubicacionGeografica = ubicacionGeografica;
    }
}
