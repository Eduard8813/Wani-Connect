package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.eduard.registro.turismo.app.dto.ReservaDTO;
import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.service.ReservaService;
import com.eduard.registro.turismo.app.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    
    @Autowired
    private UserService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearReserva(@Valid @RequestBody ReservaDTO reservaDTO,
                                         @RequestHeader("Authorization") String token) {
        try {
            // Validar token y obtener usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Token inválido o expirado");
            }
            
            String username = authentication.getName();
            
            // Verificar que el usuario del token coincide con el de la reserva
            if (!username.equals(reservaDTO.getNombreUsuario())) {
                return ResponseEntity.status(403).body("El usuario de la reserva no coincide con el del token");
            }
            
            // Verificar que el usuario existe y obtener sus datos
            User usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Verificar que el email coincide con el del usuario (case-insensitive)
            if (!usuario.getEmail().equalsIgnoreCase(reservaDTO.getEmailUsuario())) {
                return ResponseEntity.status(400).body("El email no coincide con el registrado para este usuario");
            }
            
            // Crear reserva
            Reserva reserva = reservaService.crearReserva(reservaDTO);
            return ResponseEntity.ok(reserva);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear la reserva: " + e.getMessage());
        }
    }

    @GetMapping("/validar/{codigoUnico}")
    public ResponseEntity<?> validarReserva(@PathVariable String codigoUnico) {
        try {
            boolean validada = reservaService.validarReserva(codigoUnico);
            
            if (!validada) {
                return ResponseEntity.badRequest().body("La reserva no existe o ya fue validada");
            }
            
            // Obtener detalles de la reserva para mostrar fecha de eliminación
            Reserva reserva = reservaService.findByCodigoUnico(codigoUnico)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
            
            String mensaje = "Reserva validada correctamente.";
            if (reserva.getFechaEliminacion() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                mensaje += " Será eliminada automáticamente el " + reserva.getFechaEliminacion().format(formatter);
            }
            
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al validar la reserva: " + e.getMessage());
        }
    }
    
    private String capitalizarPrimeraLetra(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }
}