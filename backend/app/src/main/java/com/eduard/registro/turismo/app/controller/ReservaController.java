package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;
    
    @PostMapping
    public ResponseEntity<Reserva> crearReserva(
            @RequestParam Long idUsuario,
            @RequestParam String categoria,
            @RequestParam int capacidad) {
        return ResponseEntity.ok(reservaService.crearReserva(idUsuario, categoria, capacidad));
    }
    
    @PostMapping("/confirmar")
    public ResponseEntity<Void> confirmarReserva(@RequestParam String token) {
        reservaService.confirmarReserva(token);
        return ResponseEntity.ok().build();
    }
}
