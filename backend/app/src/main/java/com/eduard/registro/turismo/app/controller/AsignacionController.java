package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.dto.AsignacionRequest;
import com.eduard.registro.turismo.app.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionController {
    @Autowired
    private AsignacionService asignacionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> asignarConductorVehiculo(@RequestBody AsignacionRequest request) {
        asignacionService.asignarConductorVehiculo(request.getCodigoConductor(), request.getCodigoVehiculo());
        return ResponseEntity.ok().build();
    }
}