package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.dto.SitioTuristicoDTO;
import com.eduard.registro.turismo.app.model.SitioTuristicos;
import com.eduard.registro.turismo.app.service.SitioTuristicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sitios-turisticos")
public class SitioTuristicoController {
    
    @Autowired
    private SitioTuristicoService service;
    
    @PostMapping
    public ResponseEntity<SitioTuristicos> crearSitioTuristico(
            @Valid @RequestBody SitioTuristicoDTO sitioTuristicoDTO) {
        SitioTuristicos nuevoSitio = service.crearSitioTuristico(sitioTuristicoDTO);
        return new ResponseEntity<>(nuevoSitio, HttpStatus.CREATED);
    }
    
    @GetMapping
    public List<SitioTuristicos> obtenerTodos() {
        return service.obtenerTodos();
    }
    
    @GetMapping("/{codigoUnico}")
    public ResponseEntity<SitioTuristicos> obtenerPorCodigoUnico(@PathVariable String codigoUnico) {
        SitioTuristicos sitio = service.obtenerPorCodigoUnico(codigoUnico);
        return sitio != null ? ResponseEntity.ok(sitio) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{codigoUnico}")
    public ResponseEntity<Void> eliminarSitioTuristico(@PathVariable String codigoUnico) {
        service.eliminarSitioTuristico(codigoUnico);
        return ResponseEntity.noContent().build();
    }
}