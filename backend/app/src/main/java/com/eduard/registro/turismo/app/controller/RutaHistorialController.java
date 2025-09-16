package com.eduard.registro.turismo.app.controller;

import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/historial")
@RequiredArgsConstructor
public class RutaHistorialController {
    private final RutaService rutaService;

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarHistorial(@RequestHeader("Authorization") String token, @RequestBody RutaRequest request) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("origen", request.getOrigen());
        parametros.put("destinos", request.getDestinos());
        return ResponseEntity.ok(rutaService.guardarEnHistorial(token, parametros));
    }
}