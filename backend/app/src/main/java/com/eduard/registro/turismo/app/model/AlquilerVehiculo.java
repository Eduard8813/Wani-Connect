package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "alquiler_vehiculos")
public class AlquilerVehiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, updatable = false)
    private String codigoUnico;
    
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombreEmpresa;
    
    @Embedded
    private UbicacionGeografica ubicacion;
    
    @NotBlank(message = "El tipo de vehículo es obligatorio")
    private String tipoVehiculo;
    
    @DecimalMin(value = "0.0", message = "El precio debe ser positivo")
    private Double precioPorDia;
    
    private Integer capacidad;
    
    @Column(length = 10000)
    private String caracteristicasTecnicas;
    
    private String disponibilidadFechas;
    
    @Column(length = 10000)
    private String seguroIncluido;
    
    @Column(length = 10000)
    private String metodosPago;
    
    @Column(length = 10000)
    private String politicaDevolucion;
    
    @OneToMany(mappedBy = "alquilerVehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenAlquilerVehiculo> imagenes;

    @PrePersist
    public void generarCodigoUnico() {
        this.codigoUnico = "V-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
    
    // Getters y Setters
    public Integer getCapacidad() {
        return capacidad;
    }
    public String getCaracteristicasTecnicas() {
        return caracteristicasTecnicas;
    }
    public String getCodigoUnico() {
        return codigoUnico;
    }
    public String getDisponibilidadFechas() {
        return disponibilidadFechas;
    }
    public Long getId() {
        return id;
    }
    public List<ImagenAlquilerVehiculo> getImagenes() {
        return imagenes;
    }
    public String getMetodosPago() {
        return metodosPago;
    }
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }
    public String getPoliticaDevolucion() {
        return politicaDevolucion;
    }
    public Double getPrecioPorDia() {
        return precioPorDia;
    }
    public String getSeguroIncluido() {
        return seguroIncluido;
    }
    public String getTipoVehiculo() {
        return tipoVehiculo;
    }
    public UbicacionGeografica getUbicacion() {
        return ubicacion;
    }
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
    public void setCaracteristicasTecnicas(String caracteristicasTecnicas) {
        this.caracteristicasTecnicas = caracteristicasTecnicas;
    }
    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }
    public void setDisponibilidadFechas(String disponibilidadFechas) {
        this.disponibilidadFechas = disponibilidadFechas;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setImagenes(List<ImagenAlquilerVehiculo> imagenes) {
        this.imagenes = imagenes;
    }
    public void setMetodosPago(String metodosPago) {
        this.metodosPago = metodosPago;
    }
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
    public void setPoliticaDevolucion(String politicaDevolucion) {
        this.politicaDevolucion = politicaDevolucion;
    }
    public void setPrecioPorDia(Double precioPorDia) {
        this.precioPorDia = precioPorDia;
    }
    public void setSeguroIncluido(String seguroIncluido) {
        this.seguroIncluido = seguroIncluido;
    }
    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
    public void setUbicacion(UbicacionGeografica ubicacion) {
        this.ubicacion = ubicacion;
    }
}