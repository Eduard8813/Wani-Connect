package com.eduard.registro.turismo.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduard.registro.turismo.app.model.Conductor;
import com.eduard.registro.turismo.app.service.ConductorService;

@RestController
@RequestMapping("/api/conductores")
public class ConductorController {
    @Autowired
    private ConductorService conductorService;
    
    @PostMapping
    public ResponseEntity<Conductor> guardarConductor(@RequestBody Conductor conductor) {
        return ResponseEntity.ok(conductorService.guardarConductor(conductor));
    }
    
    @GetMapping("/{codigoUnico}")
    public ResponseEntity<Conductor> obtenerConductor(
            @PathVariable String codigoUnico,
            @RequestParam(required = false) String claveAdmin) {
        return ResponseEntity.ok(conductorService.obtenerConductorPorCodigo(codigoUnico, claveAdmin));
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<Conductor>> obtenerConductoresDisponibles() {
        return ResponseEntity.ok(conductorService.obtenerConductoresDisponibles());
    }
    
    @PutMapping("/{codigoUnico}")
    public ResponseEntity<Conductor> actualizarConductor(
            @PathVariable String codigoUnico,
            @RequestBody Conductor conductorActualizado) {
        return ResponseEntity.ok(conductorService.actualizarConductor(codigoUnico, conductorActualizado));
    }
}
