package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.dto.HospedajeDTO;
import com.eduard.registro.turismo.app.model.Hospedaje;
import com.eduard.registro.turismo.app.service.HospedajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospedajes")
public class HospedajeController {
    
    @Autowired
    private HospedajeService service;
    
    @PostMapping
    public ResponseEntity<Hospedaje> crearHospedaje(
            @Valid @RequestBody HospedajeDTO hospedajeDTO) {
        Hospedaje nuevoHospedaje = service.crearHospedaje(hospedajeDTO);
        return new ResponseEntity<>(nuevoHospedaje, HttpStatus.CREATED);
    }
    
    @GetMapping
    public List<Hospedaje> obtenerTodos() {
        return service.obtenerTodos();
    }
    
    @GetMapping("/{codigoUnico}")
    public ResponseEntity<Hospedaje> obtenerPorCodigoUnico(@PathVariable String codigoUnico) {
        Hospedaje hospedaje = service.obtenerPorCodigoUnico(codigoUnico);
        return hospedaje != null ? ResponseEntity.ok(hospedaje) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHospedaje(@PathVariable Long id) {
        service.eliminarHospedaje(id);
        return ResponseEntity.noContent().build();
    }
}