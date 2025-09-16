package com.eduard.registro.turismo.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduard.registro.turismo.app.service.RecomendacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recomendacion")
@RequiredArgsConstructor
public class RecomendacionController {
    private final RecomendacionService recomendacionService;

    @GetMapping("/ruta/{userId}")
    public ResponseEntity<?> rutaRecomendada(@PathVariable Long userId) {
        return ResponseEntity.ok(recomendacionService.recomendarRuta(userId));
    }
}