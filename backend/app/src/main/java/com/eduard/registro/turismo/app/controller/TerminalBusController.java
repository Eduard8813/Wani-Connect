package com.eduard.registro.turismo.app.controller;
import com.eduard.registro.turismo.app.dto.TerminalBusDTO;
import com.eduard.registro.turismo.app.dto.UbicacionTerminalDTO;
import com.eduard.registro.turismo.app.model.TerminalBus;
import com.eduard.registro.turismo.app.service.TerminalBusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terminales-buses")
public class TerminalBusController {
    
    @Autowired
    private TerminalBusService service;
    
    @PostMapping
    public ResponseEntity<TerminalBus> crearTerminal(
            @Valid @RequestBody TerminalBusDTO terminalBusDTO) {
        TerminalBus nuevaTerminal = service.crearTerminal(terminalBusDTO);
        return new ResponseEntity<>(nuevaTerminal, HttpStatus.CREATED);
    }
    
    @GetMapping
    public List<TerminalBus> obtenerTodas() {
        return service.obtenerTodas();
    }
    
    @GetMapping("/{codigoUnico}")
    public ResponseEntity<TerminalBus> obtenerPorCodigoUnico(@PathVariable String codigoUnico) {
        TerminalBus terminal = service.obtenerPorCodigoUnico(codigoUnico);
        return terminal != null ? ResponseEntity.ok(terminal) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TerminalBus> actualizarTerminal(
            @PathVariable Long id,
            @Valid @RequestBody TerminalBus terminalActualizada) {
        TerminalBus terminal = service.actualizarTerminal(id, terminalActualizada);
        return ResponseEntity.ok(terminal);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTerminal(@PathVariable Long id) {
        service.eliminarTerminal(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/ubicaciones")
    public List<UbicacionTerminalDTO> obtenerUbicaciones() {
        return service.obtenerUbicaciones();
    }
}