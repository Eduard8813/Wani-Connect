package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduard.registro.turismo.app.service.AsignacionService;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionController {
    @Autowired
    private AsignacionService asignacionService;
    
    @PostMapping
    public ResponseEntity<Void> asignarConductorVehiculo(
            @RequestParam String codigoConductor,
            @RequestParam String codigoVehiculo) {
        asignacionService.asignarConductorVehiculo(codigoConductor, codigoVehiculo);
        return ResponseEntity.ok().build();
    }
}