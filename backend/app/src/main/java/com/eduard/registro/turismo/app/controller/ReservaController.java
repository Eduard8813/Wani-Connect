package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.dto.LugarReservaDTO;
import com.eduard.registro.turismo.app.dto.ReservaDTO;
import com.eduard.registro.turismo.app.dto.ReservaRequest;
import com.eduard.registro.turismo.app.service.ReservaService;
import com.eduard.registro.turismo.app.security.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    
    @Autowired
    private ReservaService reservaService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @GetMapping("/lugares/{terminalId}")
    public List<LugarReservaDTO> obtenerLugaresPorTerminal(@PathVariable Long terminalId) {
        return reservaService.obtenerLugaresPorTerminal(terminalId);
    }
    
    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ReservaRequest reservaRequest) {
        // Extraer el token del header (quitar "Bearer ")
        String jwtToken = token.substring(7);
        
        // Llamar al servicio con el token
        ReservaDTO reserva = reservaService.crearReserva(jwtToken, reservaRequest);
        return ResponseEntity.ok(reserva);
    }
    
    @GetMapping("/usuario/{userId}")
    public List<ReservaDTO> obtenerReservasPorUsuario(@PathVariable Long userId) {
        return reservaService.obtenerReservasPorUsuario(userId);
    }
    
    @GetMapping("/codigo/{codigoUnico}")
    public ResponseEntity<ReservaDTO> obtenerReservaPorCodigo(@PathVariable String codigoUnico) {
        ReservaDTO reserva = reservaService.obtenerReservaPorCodigo(codigoUnico);
        return ResponseEntity.ok(reserva);
    }
}