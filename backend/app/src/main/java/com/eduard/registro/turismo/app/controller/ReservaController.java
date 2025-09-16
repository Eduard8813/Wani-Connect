package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.dto.ConfirmarReservaRequest;
import com.eduard.registro.turismo.app.dto.ReservaRequest;
import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reserva> crearReserva(@RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.crearReserva(request.getIdUsuario(), request.getCategoria(), request.getCapacidad()));
    }

    @PostMapping(value = "/confirmar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmarReserva(@RequestBody ConfirmarReservaRequest request) {
        reservaService.confirmarReserva(request.getToken());
        return ResponseEntity.ok().build();
    }
}