package com.eduard.registro.turismo.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduard.registro.turismo.app.model.EstadoVehiculo;
import com.eduard.registro.turismo.app.model.Vehiculo;
import com.eduard.registro.turismo.app.service.VehiculoService;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {
    @Autowired
    private VehiculoService vehiculoService;
    
    @PostMapping
    public ResponseEntity<Vehiculo> guardarVehiculo(@RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.guardarVehiculo(vehiculo));
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculosPorCategoria(categoria));
    }
    
    @GetMapping("/sugerir")
    public ResponseEntity<List<Vehiculo>> sugerirVehiculos(
            @RequestParam String categoria,
            @RequestParam int capacidad) {
        return ResponseEntity.ok(vehiculoService.sugerirVehiculos(categoria, capacidad));
    }
    
    @PutMapping("/{codigoUnico}")
    public ResponseEntity<Vehiculo> actualizarVehiculo(
            @PathVariable String codigoUnico,
            @RequestBody Vehiculo vehiculoActualizado) {
        return ResponseEntity.ok(vehiculoService.actualizarVehiculo(codigoUnico, vehiculoActualizado));
    }
    
    @DeleteMapping("/{codigoUnico}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable String codigoUnico) {
        vehiculoService.eliminarVehiculo(codigoUnico);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosPorEstado(@PathVariable EstadoVehiculo estado) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculosPorEstado(estado));
    }
}
