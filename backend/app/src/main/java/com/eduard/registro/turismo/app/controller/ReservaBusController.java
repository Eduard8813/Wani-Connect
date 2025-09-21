package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.eduard.registro.turismo.app.dto.ReservaBusDTO;
import com.eduard.registro.turismo.app.dto.LugarBusDTO;
import com.eduard.registro.turismo.app.model.ReservaBus;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.model.BusDisponible;
import com.eduard.registro.turismo.app.service.ReservaBusService;
import com.eduard.registro.turismo.app.service.UserService;
import com.eduard.registro.turismo.app.service.EmailService;
import com.eduard.registro.turismo.app.security.JwtTokenUtil;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/reservas-bus")
public class ReservaBusController {

    @Autowired
    private ReservaBusService reservaBusService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping
    // cSpell:ignore crear Reserva reserva inválido expirado usuario Usuario encontrado
    public ResponseEntity<?> crearReserva(@Valid @RequestBody ReservaBusDTO reservaDTO,
                                         @RequestHeader("Authorization") String token) {
        try {
            System.out.println("=== DEBUG RESERVA ===");
            System.out.println("Datos recibidos: " + reservaDTO.getBusId() + ", lugares: " + reservaDTO.getNumerosLugar());
            System.out.println("Usuario: " + reservaDTO.getNombreUsuario());
            System.out.println("Email: " + reservaDTO.getEmailUsuario());
            System.out.println("Terminal: " + reservaDTO.getTerminalId());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Token inválido o expirado");
            }
            
            String username = authentication.getName();
            
            if (!username.equals(reservaDTO.getNombreUsuario())) {
                return ResponseEntity.status(403).body("El usuario de la reserva no coincide con el del token");
            }
            
            User usuario = userService.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
            
            // Usar siempre el email del usuario autenticado para evitar problemas de sincronización
            reservaDTO.setEmailUsuario(usuario.getEmail());
            System.out.println("Email del usuario autenticado: " + usuario.getEmail());
            
            System.out.println("Iniciando creación de reserva...");
            List<ReservaBus> reservas = reservaBusService.crearReserva(reservaDTO);
            System.out.println("Reservas creadas exitosamente: " + reservas.size());
            
            // Enviar correo de confirmación para cada reserva
            for (ReservaBus reserva : reservas) {
                try {
                    emailService.enviarCorreoReservaBus(reserva);
                } catch (Exception e) {
                    System.err.println("Error al enviar correo: " + e.getMessage());
                }
            }
            
            return ResponseEntity.ok(reservas);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Datos inválidos: " + e.getMessage());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Error de integridad de datos: " + e.getMessage());
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            return ResponseEntity.status(400).body("Violación de restricción: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al crear la reserva: " + e.getMessage());
        }
    }

    @GetMapping("/buses/{busId}/lugares")
    public ResponseEntity<?> obtenerLugaresBus(@PathVariable Long busId) {
        try {
            List<LugarBusDTO> lugares = reservaBusService.obtenerLugaresBus(busId);
            BusDisponible bus = reservaBusService.obtenerInfoBus(busId);
            
            // Crear respuesta con la estructura que espera el frontend
            java.util.Map<String, Object> respuesta = new java.util.HashMap<>();
            respuesta.put("totalLugares", bus.getTotalLugares());
            respuesta.put("lugares", lugares);
            
            System.out.println("=== LUGARES ENVIADOS AL FRONTEND ===");
            System.out.println("Total lugares: " + lugares.size());
            System.out.println("Bus total lugares: " + bus.getTotalLugares());
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al obtener lugares: " + e.getMessage());
        }
    }
    
    @GetMapping("/buses/{busId}/info")
    public ResponseEntity<?> obtenerInfoBus(@PathVariable Long busId) {
        try {
            // Obtener información básica del bus para debugging
            return ResponseEntity.ok(reservaBusService.obtenerInfoBus(busId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al obtener info del bus: " + e.getMessage());
        }
    }
    
    @GetMapping("/buses/{busId}/debug")
    public ResponseEntity<?> debugReservas(@PathVariable Long busId) {
        try {
            return ResponseEntity.ok(reservaBusService.debugReservasBus(busId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error en debug: " + e.getMessage());
        }
    }

    @PostMapping("/validar/{codigoUnico}")
    public ResponseEntity<?> validarReserva(@PathVariable String codigoUnico) {
        try {
            boolean validada = reservaBusService.validarReserva(codigoUnico);
            
            if (!validada) {
                return ResponseEntity.badRequest().body("La reserva no existe o ya fue validada");
            }
            
            return ResponseEntity.ok("Reserva validada correctamente. Confirmación enviada por correo.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al validar la reserva: " + e.getMessage());
        }
    }
    
    @PostMapping("/conductor/liberar-bus/{busId}")
    public ResponseEntity<?> liberarBusPorConductor(@PathVariable Long busId,
                                                   @RequestHeader("Authorization") String token) {
        try {
            // Verificar que el usuario tenga rol de empresa
            if (!esUsuarioEmpresa(token)) {
                return ResponseEntity.status(403).body("Acceso denegado. Solo las cuentas de empresa pueden liberar buses.");
            }
            
            boolean liberado = reservaBusService.liberarBusPorConductor(busId);
            
            if (liberado) {
                return ResponseEntity.ok("Bus liberado correctamente. Todas las reservas han sido eliminadas.");
            } else {
                return ResponseEntity.ok("No había reservas para liberar en este bus.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al liberar el bus: " + e.getMessage());
        }
    }
    
    private boolean esUsuarioEmpresa(String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return false;
            }
            
            String token = authHeader.substring(7);
            String roleFromToken = jwtTokenUtil.extractRole(token);
            
            return "COMPANY".equals(roleFromToken);
        } catch (Exception e) {
            return false;
        }
    }
}