package com.eduard.registro.turismo.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduard.registro.turismo.app.dto.RutaRequest;
import com.eduard.registro.turismo.app.service.RutaService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
public class RutaController {
    private final RutaService rutaService;

    @PostMapping("/optimizar")
    public ResponseEntity<?> optimizarRuta(@RequestHeader("Authorization") String token, @RequestBody RutaRequest request) {
        // Validar token...
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("origen", request.getOrigen());
        parametros.put("destinos", request.getDestinos());
        return ResponseEntity.ok(rutaService.optimizarRuta(parametros));
    }
    @GetMapping("/ver/{id}")
    public ResponseEntity<?> verRuta(@PathVariable Long id) {
        return ResponseEntity.ok(rutaService.obtenerRutaEnGeoJson(id));
    }

    @GetMapping("/comparar-historicas/{userId}")
    public ResponseEntity<?> compararRutas(@PathVariable Long userId) {
        return ResponseEntity.ok(rutaService.compararHistorico(userId));
    }
}