package com.eduard.registro.turismo.app.dto;

import java.util.List;

import com.eduard.registro.turismo.app.model.TerminalBus;
import com.eduard.registro.turismo.app.model.UbicacionGeografica;

public class TerminalBusDTO {
    private Long id;
    private String codigoUnico;
    private String nombre;
    private String localidad;
    private UbicacionGeograficaDTO ubicacionGeografica;
    private List<BusDisponibleDTO> busesDisponibles;
    
    // Constructor, getters y setters
    public TerminalBusDTO() {}
    
    // Constructor que recibe una entidad TerminalBus
    public TerminalBusDTO(TerminalBus terminal) {
        this.id = terminal.getId();
        this.codigoUnico = terminal.getCodigoUnico();
        this.nombre = terminal.getNombre();
        this.localidad = terminal.getLocalidad();
        this.ubicacionGeografica = new UbicacionGeograficaDTO(terminal.getUbicacionGeografica());
        // Convertir la lista de buses a DTOs
        this.busesDisponibles = terminal.getBusesDisponibles().stream()
            .map(BusDisponibleDTO::new)
            .collect(java.util.stream.Collectors.toList());
    }
    
    // Método para convertir DTO a entidad
    public TerminalBus getTerminalBus() {
        TerminalBus terminal = new TerminalBus();
        terminal.setId(this.id);
        terminal.setCodigoUnico(this.codigoUnico);
        terminal.setNombre(this.nombre);
        terminal.setLocalidad(this.localidad);
        
        if (this.ubicacionGeografica != null) {
            UbicacionGeografica ubicacion = new UbicacionGeografica();
            ubicacion.setDireccion(this.ubicacionGeografica.getDireccion());
            ubicacion.setLatitud(this.ubicacionGeografica.getLatitud());
            ubicacion.setLongitud(this.ubicacionGeografica.getLongitud());
            terminal.setUbicacionGeografica(ubicacion);
        }
        
        return terminal;
    }
    
    // Getters y setters
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
    
    public String getLocalidad() {
        return localidad;
    }
    
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
    
    public UbicacionGeograficaDTO getUbicacionGeografica() {
        return ubicacionGeografica;
    }
    
    public void setUbicacionGeografica(UbicacionGeograficaDTO ubicacionGeografica) {
        this.ubicacionGeografica = ubicacionGeografica;
    }
    
    public List<BusDisponibleDTO> getBusesDisponibles() {
        return busesDisponibles;
    }
    
    public void setBusesDisponibles(List<BusDisponibleDTO> busesDisponibles) {
        this.busesDisponibles = busesDisponibles;
    }
}