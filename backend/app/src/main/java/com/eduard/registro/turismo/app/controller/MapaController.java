package com.eduard.registro.turismo.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduard.registro.turismo.app.service.SitioTuristicoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mapa")
@RequiredArgsConstructor
public class MapaController {
    private final SitioTuristicoService sitioService;

    @GetMapping("/todos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verTodosEnMapa() {
        return ResponseEntity.ok(sitioService.listarUbicacionesSolo());
    }
}