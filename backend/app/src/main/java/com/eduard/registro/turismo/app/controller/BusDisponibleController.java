package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.model.BusDisponible;
import com.eduard.registro.turismo.app.model.TerminalBus;
import com.eduard.registro.turismo.app.service.BusDisponibleService;
import com.eduard.registro.turismo.app.service.TerminalBusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusDisponibleController {
    
    @Autowired
    private BusDisponibleService busDisponibleService;
    
    @Autowired
    private TerminalBusService terminalBusService;
    
    // Obtener todos los buses disponibles
    @GetMapping
    public ResponseEntity<List<BusDisponible>> getAllBuses() {
        List<BusDisponible> buses = busDisponibleService.findAll();
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
    
    // Obtener un bus por su ID
    @GetMapping("/{id}")
    public ResponseEntity<BusDisponible> getBusById(@PathVariable Long id) {
        return busDisponibleService.findById(id)
                .map(bus -> new ResponseEntity<>(bus, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Obtener buses disponibles por terminal (usando ID)
    @GetMapping("/terminal/{terminalId}")
    public ResponseEntity<List<BusDisponible>> getBusesByTerminal(@PathVariable Long terminalId) {
        List<BusDisponible> buses = busDisponibleService.findByTerminalId(terminalId);
        if (buses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
    
    // Obtener buses disponibles por terminal (usando código único)
    @GetMapping("/terminal/codigo/{codigoUnico}")
    public ResponseEntity<List<BusDisponible>> getBusesByTerminalCodigo(@PathVariable String codigoUnico) {
        TerminalBus terminal = terminalBusService.obtenerPorCodigoUnico(codigoUnico);
        List<BusDisponible> buses = busDisponibleService.findByTerminal(terminal);
        if (buses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
    
    // Obtener buses disponibles con lugares libres por terminal (usando ID)
    @GetMapping("/terminal/{terminalId}/disponibles")
    public ResponseEntity<List<BusDisponible>> getAvailableBusesByTerminal(@PathVariable Long terminalId) {
        List<BusDisponible> buses = busDisponibleService.findAvailableByTerminalId(terminalId);
        if (buses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
    
    // Obtener buses disponibles con lugares libres por terminal (usando código único)
    @GetMapping("/terminal/codigo/{codigoUnico}/disponibles")
    public ResponseEntity<List<BusDisponible>> getAvailableBusesByTerminalCodigo(@PathVariable String codigoUnico) {
        TerminalBus terminal = terminalBusService.obtenerPorCodigoUnico(codigoUnico);
        List<BusDisponible> buses = busDisponibleService.findAvailableByTerminalId(terminal.getId());
        if (buses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
    
    // Crear un nuevo bus disponible (usando ID de terminal)
    @PostMapping
    public ResponseEntity<BusDisponible> createBus(@Valid @RequestBody BusDisponible bus) {
        BusDisponible newBus = busDisponibleService.save(bus);
        return new ResponseEntity<>(newBus, HttpStatus.CREATED);
    }
    
    // Crear un nuevo bus disponible (usando código único de terminal)
    @PostMapping("/terminal/codigo/{codigoUnico}")
    public ResponseEntity<BusDisponible> createBusByTerminalCodigo(
            @PathVariable String codigoUnico,
            @Valid @RequestBody BusDisponible bus) {
        
        TerminalBus terminal = terminalBusService.obtenerPorCodigoUnico(codigoUnico);
        bus.setTerminalBus(terminal);
        
        BusDisponible newBus = busDisponibleService.save(bus);
        return new ResponseEntity<>(newBus, HttpStatus.CREATED);
    }
    
    // Crear un nuevo bus para una terminal específica (usando ID)
    @PostMapping("/terminal/{terminalId}")
    public ResponseEntity<BusDisponible> createBusForTerminal(
            @PathVariable Long terminalId,
            @RequestParam String numeroBus,
            @RequestParam String destino,
            @RequestParam String horaSalida,
            @RequestParam int totalLugares) {
        
        BusDisponible newBus = busDisponibleService.createBusForTerminal(
            terminalId, numeroBus, destino, LocalTime.parse(horaSalida), totalLugares);
        
        return new ResponseEntity<>(newBus, HttpStatus.CREATED);
    }
    
    // Crear un nuevo bus para una terminal específica (usando código único)
    @PostMapping("/terminal/codigo/{codigoUnico}/form")
    public ResponseEntity<BusDisponible> createBusForTerminalByCodigo(
            @PathVariable String codigoUnico,
            @RequestParam String numeroBus,
            @RequestParam String destino,
            @RequestParam String horaSalida,
            @RequestParam int totalLugares) {
        
        TerminalBus terminal = terminalBusService.obtenerPorCodigoUnico(codigoUnico);
        
        BusDisponible newBus = busDisponibleService.createBusForTerminal(
            terminal.getId(), numeroBus, destino, LocalTime.parse(horaSalida), totalLugares);
        
        return new ResponseEntity<>(newBus, HttpStatus.CREATED);
    }
    
    // Actualizar un bus existente
    @PutMapping("/{id}")
    public ResponseEntity<BusDisponible> updateBus(@PathVariable Long id, @Valid @RequestBody BusDisponible bus) {
        BusDisponible updatedBus = busDisponibleService.update(id, bus);
        return new ResponseEntity<>(updatedBus, HttpStatus.OK);
    }
    
    // Eliminar un bus
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        busDisponibleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Actualizar lugares disponibles de un bus
    @PatchMapping("/{id}/lugares")
    public ResponseEntity<BusDisponible> updateAvailableSeats(@PathVariable Long id, @RequestParam int lugaresReservados) {
        try {
            BusDisponible updatedBus = busDisponibleService.updateLugaresDisponibles(id, lugaresReservados);
            return new ResponseEntity<>(updatedBus, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}