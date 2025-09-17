package com.eduard.registro.turismo.app.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "restaurantes")
public class Restaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, updatable = false)
    private String codigoUnico;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El tipo de comida es obligatorio")
    private String tipoComida;
    
    @Embedded
    private UbicacionGeografica ubicacion;
    
    @NotBlank(message = "El horario es obligatorio")
    private String horario;
    
    @DecimalMin(value = "0.0", message = "El precio debe ser positivo")
    private Double precioPromedio;
    
    @Column(length = 10000)
    private String menuDigital;
    
    @Column(length = 10000)
    private String opcionesEspeciales;
    
    private String reservasDisponibles;
    
    @Column(length = 10000)
    private String metodosPago;
    
    @Column(length = 10000)
    private String historiaRestaurante;
    
    @Column(length = 10000)
    private String platosTipicos;
    
    @Column(length = 10000)
    private String eventosGastronomicos;
    
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenRestaurante> imagenes;

    @PrePersist
    public void generarCodigoUnico() {
        this.codigoUnico = "R-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCodigoUnico() {
        return codigoUnico;
    }
    
    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTipoComida() {
        return tipoComida;
    }
    
    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }
    
    public UbicacionGeografica getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(UbicacionGeografica ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public String getHorario() {
        return horario;
    }
    
    public void setHorario(String horario) {
        this.horario = horario;
    }
    
    public Double getPrecioPromedio() {
        return precioPromedio;
    }
    
    public void setPrecioPromedio(Double precioPromedio) {
        this.precioPromedio = precioPromedio;
    }
    
    public String getMenuDigital() {
        return menuDigital;
    }
    
    public void setMenuDigital(String menuDigital) {
        this.menuDigital = menuDigital;
    }
    
    public String getOpcionesEspeciales() {
        return opcionesEspeciales;
    }
    
    public void setOpcionesEspeciales(String opcionesEspeciales) {
        this.opcionesEspeciales = opcionesEspeciales;
    }
    
    public String getReservasDisponibles() {
        return reservasDisponibles;
    }
    
    public void setReservasDisponibles(String reservasDisponibles) {
        this.reservasDisponibles = reservasDisponibles;
    }
    
    public String getMetodosPago() {
        return metodosPago;
    }
    
    public void setMetodosPago(String metodosPago) {
        this.metodosPago = metodosPago;
    }
    
    public String getHistoriaRestaurante() {
        return historiaRestaurante;
    }
    
    public void setHistoriaRestaurante(String historiaRestaurante) {
        this.historiaRestaurante = historiaRestaurante;
    }
    
    public String getPlatosTipicos() {
        return platosTipicos;
    }
    
    public void setPlatosTipicos(String platosTipicos) {
        this.platosTipicos = platosTipicos;
    }
    
    public String getEventosGastronomicos() {
        return eventosGastronomicos;
    }
    
    public void setEventosGastronomicos(String eventosGastronomicos) {
        this.eventosGastronomicos = eventosGastronomicos;
    }
    
    public List<ImagenRestaurante> getImagenes() {
        return imagenes;
    }
    
    public void setImagenes(List<ImagenRestaurante> imagenes) {
        this.imagenes = imagenes;
    }
}