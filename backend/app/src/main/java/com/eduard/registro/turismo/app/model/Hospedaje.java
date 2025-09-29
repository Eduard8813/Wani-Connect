package com.eduard.registro.turismo.app.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table; 
import jakarta.validation.constraints.DecimalMin; 
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "hospedajes")
public class Hospedaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, updatable = false)
    private String codigoUnico;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El tipo de hospedaje es obligatorio")
    private String tipo;
    
    @Embedded
    private UbicacionGeografica ubicacion;
    
    @DecimalMin(value = "0.0", message = "El precio debe ser positivo")
    private Double precioPorNoche;
    
    @Min(value = 1, message = "La categoría debe ser al menos 1")
    @Max(value = 5, message = "La categoría máxima es 5")
    private Integer categoria;

    @Column(length = 10000)
    private String serviciosIncluidos;
    
    @Column(length = 10000)
    private String politicaCancelacion;
    
    private String disponibilidadFechas;
    
    @Column(length = 10000)
    private String metodosPago;
    
    @Column(length = 10000)
    private String estiloArquitectonico;
    
    @Column(length = 10000)
    private String historiaLugar;
    
    @Column(length = 10000)
    private String relacionCulturaLocal;
    
    @OneToMany(mappedBy = "hospedaje", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenHospedaje> imagenes;

    @PrePersist
    public void generarCodigoUnico() {
        this.codigoUnico = "H-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
    
    // Getters y Setters

    public Integer getCategoria() {
        return categoria;
    }
    public String getCodigoUnico() {
        return codigoUnico;
    }
    public String getDisponibilidadFechas() {
        return disponibilidadFechas;
    }
    public String getEstiloArquitectonico() {
        return estiloArquitectonico;
    }
    public String getHistoriaLugar() {
        return historiaLugar;
    }
    public Long getId() {
        return id;
    }
    public List<ImagenHospedaje> getImagenes() {
        return imagenes;
    }
    public String getMetodosPago() {
        return metodosPago;
    }
    public String getNombre() {
        return nombre;
    }
    public String getPoliticaCancelacion() {
        return politicaCancelacion;
    }
    public Double getPrecioPorNoche() {
        return precioPorNoche;
    }
    public String getRelacionCulturaLocal() {
        return relacionCulturaLocal;
    }
    public String getServiciosIncluidos() {
        return serviciosIncluidos;
    }
    public String getTipo() {
        return tipo;
    }
    public UbicacionGeografica getUbicacion() {
        return ubicacion;
    }
    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }
    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }
    public void setDisponibilidadFechas(String disponibilidadFechas) {
        this.disponibilidadFechas = disponibilidadFechas;
    }
    public void setEstiloArquitectonico(String estiloArquitectonico) {
        this.estiloArquitectonico = estiloArquitectonico;
    }
    public void setHistoriaLugar(String historiaLugar) {
        this.historiaLugar = historiaLugar;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setImagenes(List<ImagenHospedaje> imagenes) {
        this.imagenes = imagenes;
    }
    public void setMetodosPago(String metodosPago) {
        this.metodosPago = metodosPago;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPoliticaCancelacion(String politicaCancelacion) {
        this.politicaCancelacion = politicaCancelacion;
    }
    public void setPrecioPorNoche(Double precioPorNoche) {
        this.precioPorNoche = precioPorNoche;
    }
    public void setRelacionCulturaLocal(String relacionCulturaLocal) {
        this.relacionCulturaLocal = relacionCulturaLocal;
    }
    public void setServiciosIncluidos(String serviciosIncluidos) {
        this.serviciosIncluidos = serviciosIncluidos;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setUbicacion(UbicacionGeografica ubicacion) {
        this.ubicacion = ubicacion;
    }
}
    