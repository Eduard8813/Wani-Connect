package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.dto.AlquilerVehiculoDTO;
import com.eduard.registro.turismo.app.model.AlquilerVehiculo;
import com.eduard.registro.turismo.app.service.AlquilerVehiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alquiler-vehiculos")
public class AlquilerVehiculoController {
    
    @Autowired
    private AlquilerVehiculoService service;
    
    @PostMapping
    public ResponseEntity<AlquilerVehiculo> crearAlquilerVehiculo(
            @Valid @RequestBody AlquilerVehiculoDTO alquilerVehiculoDTO) {
        AlquilerVehiculo nuevoAlquiler = service.crearAlquilerVehiculo(alquilerVehiculoDTO);
        return new ResponseEntity<>(nuevoAlquiler, HttpStatus.CREATED);
    }
    
    @GetMapping
    public List<AlquilerVehiculo> obtenerTodos() {
        return service.obtenerTodos();
    }
    
    @GetMapping("/{codigoUnico}")
    public ResponseEntity<AlquilerVehiculo> obtenerPorCodigoUnico(@PathVariable String codigoUnico) {
        AlquilerVehiculo alquiler = service.obtenerPorCodigoUnico(codigoUnico);
        return alquiler != null ? ResponseEntity.ok(alquiler) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlquilerVehiculo(@PathVariable Long id) {
        service.eliminarAlquilerVehiculo(id);
        return ResponseEntity.noContent().build();
    }
}