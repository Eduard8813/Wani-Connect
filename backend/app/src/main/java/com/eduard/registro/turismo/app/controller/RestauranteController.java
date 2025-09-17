package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.dto.RestauranteDTO;
import com.eduard.registro.turismo.app.model.Restaurante;
import com.eduard.registro.turismo.app.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {
    
    @Autowired
    private RestauranteService service;
    
    @PostMapping
    public ResponseEntity<Restaurante> crearRestaurante(
            @Valid @RequestBody RestauranteDTO restauranteDTO) {
        Restaurante nuevoRestaurante = service.crearRestaurante(restauranteDTO);
        return new ResponseEntity<>(nuevoRestaurante, HttpStatus.CREATED);
    }
    
    @GetMapping
    public List<Restaurante> obtenerTodos() {
        return service.obtenerTodos();
    }
    
    @GetMapping("/{codigoUnico}")
    public ResponseEntity<Restaurante> obtenerPorCodigoUnico(@PathVariable String codigoUnico) {
        Restaurante restaurante = service.obtenerPorCodigoUnico(codigoUnico);
        return restaurante != null ? ResponseEntity.ok(restaurante) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRestaurante(@PathVariable Long id) {
        service.eliminarRestaurante(id);
        return ResponseEntity.noContent().build();
    }
}