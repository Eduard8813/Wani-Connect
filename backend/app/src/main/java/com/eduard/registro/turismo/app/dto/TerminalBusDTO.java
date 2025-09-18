package com.eduard.registro.turismo.app.dto;

import java.util.List;

import com.eduard.registro.turismo.app.model.TerminalBus;

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
    
    // Getters y setters...
}