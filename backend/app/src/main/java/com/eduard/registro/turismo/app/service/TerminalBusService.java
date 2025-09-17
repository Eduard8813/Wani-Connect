package com.eduard.registro.turismo.app.service;
import com.eduard.registro.turismo.app.dto.TerminalBusDTO;
import com.eduard.registro.turismo.app.dto.UbicacionTerminalDTO;
import com.eduard.registro.turismo.app.model.TerminalBus;
import com.eduard.registro.turismo.app.repository.TerminalBusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TerminalBusService {
    
    @Autowired
    private TerminalBusRepository repository;
    
    @Transactional
    public TerminalBus crearTerminal(@Valid TerminalBusDTO terminalBusDTO) {
        TerminalBus terminalBus = terminalBusDTO.getTerminalBus();
        return repository.save(terminalBus);
    }
    
    @Transactional
    public TerminalBus actualizarTerminal(Long id, @Valid TerminalBus terminalBusActualizada) {
        TerminalBus terminalExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terminal no encontrada"));
        
        terminalExistente.setNombre(terminalBusActualizada.getNombre());
        terminalExistente.setLocalidad(terminalBusActualizada.getLocalidad());
        terminalExistente.setUbicacionGeografica(terminalBusActualizada.getUbicacionGeografica());
        
        return repository.save(terminalExistente);
    }
    
    public List<TerminalBus> obtenerTodas() {
        return repository.findAll();
    }
    
    public TerminalBus obtenerPorCodigoUnico(String codigoUnico) {
        return repository.findByCodigoUnico(codigoUnico);
    }
    
    public List<UbicacionTerminalDTO> obtenerUbicaciones() {
        List<TerminalBus> terminales = repository.findAll();
        return terminales.stream()
                .map(terminal -> {
                    UbicacionTerminalDTO dto = new UbicacionTerminalDTO();
                    dto.setNombre(terminal.getNombre());
                    dto.setLocalidad(terminal.getLocalidad());
                    dto.setLatitud(terminal.getUbicacionGeografica().getLatitud());
                    dto.setLongitud(terminal.getUbicacionGeografica().getLongitud());
                    dto.setUrlImagen("https://via.placeholder.com/400x300?text=Terminal+Bus"); // Imagen por defecto
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void eliminarTerminal(Long id) {
        repository.deleteById(id);
    }
}