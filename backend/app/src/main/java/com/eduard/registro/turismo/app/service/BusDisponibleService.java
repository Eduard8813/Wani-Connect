package com.eduard.registro.turismo.app.service;
import com.eduard.registro.turismo.app.model.BusDisponible;
import com.eduard.registro.turismo.app.model.TerminalBus;
import com.eduard.registro.turismo.app.repository.BusDisponibleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BusDisponibleService {
    
    @Autowired 
    private BusDisponibleRepository busDisponibleRepository;
    
    @Autowired
    private TerminalBusService terminalBusService;
    
    // Obtener todos los buses disponibles
    public List<BusDisponible> findAll() {
        return busDisponibleRepository.findAll();
    }
    
    // Obtener un bus por su ID
    public Optional<BusDisponible> findById(Long id) {
        return busDisponibleRepository.findById(id);
    }
    
    // Obtener buses disponibles por terminal
    public List<BusDisponible> findByTerminal(TerminalBus terminalBus) {
        return busDisponibleRepository.findByTerminalBus(terminalBus);
    }
    
    // Obtener buses disponibles por ID de terminal
    public List<BusDisponible> findByTerminalId(Long terminalId) {
        return busDisponibleRepository.findByTerminalId(terminalId);
    }
    
    // Obtener buses disponibles con lugares libres por terminal
    public List<BusDisponible> findAvailableByTerminalId(Long terminalId) {
        return busDisponibleRepository.findAvailableByTerminalId(terminalId);
    }
    
    // Guardar un nuevo bus disponible
    @Transactional
    public BusDisponible save(BusDisponible busDisponible) {
        // Validar que los lugares disponibles no superen el total
        if (busDisponible.getLugaresDisponibles() > busDisponible.getTotalLugares()) {
            throw new IllegalArgumentException("Los lugares disponibles no pueden superar el total de lugares");
        }
        return busDisponibleRepository.save(busDisponible);
    }
    
    // Actualizar un bus disponible
    @Transactional
    public BusDisponible update(Long id, BusDisponible busDisponible) {
        if (!busDisponibleRepository.existsById(id)) {
            throw new RuntimeException("Bus no encontrado con ID: " + id);
        }
        
        busDisponible.setId(id);
        return save(busDisponible);
    }
    
    // Eliminar un bus disponible
    @Transactional
    public void deleteById(Long id) {
        if (!busDisponibleRepository.existsById(id)) {
            throw new RuntimeException("Bus no encontrado con ID: " + id);
        }
        busDisponibleRepository.deleteById(id);
    }
    
    // Actualizar la cantidad de lugares disponibles
    @Transactional
    public BusDisponible updateLugaresDisponibles(Long busId, int lugaresReservados) {
        BusDisponible bus = busDisponibleRepository.findById(busId)
            .orElseThrow(() -> new RuntimeException("Bus no encontrado con ID: " + busId));
        
        if (bus.getLugaresDisponibles() < lugaresReservados) {
            throw new IllegalArgumentException("No hay suficientes lugares disponibles");
        }
        
        bus.setLugaresDisponibles(bus.getLugaresDisponibles() - lugaresReservados);
        return busDisponibleRepository.save(bus);
    }
    
    // Crear un nuevo bus disponible para una terminal
    @Transactional
    public BusDisponible createBusForTerminal(TerminalBus terminalBus, String numeroBus, 
                                             String destino, LocalTime horaSalida, 
                                             int totalLugares, Double precio) {
        BusDisponible bus = new BusDisponible();
        bus.setTerminalBus(terminalBus);
        bus.setNumeroBus(numeroBus);
        bus.setDestino(destino);
        bus.setHoraSalida(horaSalida);
        bus.setTotalLugares(totalLugares);
        bus.setLugaresDisponibles(totalLugares);
        bus.setPrecio(precio);
        
        return save(bus);
    }

    // En BusDisponibleService.java
@Transactional
public BusDisponible createBusForTerminal(Long terminalId, String numeroBus, 
                                         String destino, LocalTime horaSalida, 
                                         int totalLugares, Double precio) {
    
    TerminalBus terminalBus = terminalBusService.findById(terminalId)
            .orElseThrow(() -> new RuntimeException("Terminal no encontrada con ID: " + terminalId));
    
    BusDisponible bus = new BusDisponible();
    bus.setTerminalBus(terminalBus);
    bus.setNumeroBus(numeroBus);
    bus.setDestino(destino);
    bus.setHoraSalida(horaSalida);
    bus.setTotalLugares(totalLugares);
    bus.setLugaresDisponibles(totalLugares);
    bus.setPrecio(precio);
    return save(bus);
}
}