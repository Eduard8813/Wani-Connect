package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/test")
    public ResponseEntity<String> testEmail() {
        try {
            // Crear un usuario de prueba con un correo real
            User usuario = new User();
            usuario.setId(1L);
            usuario.setEmail("Eduardmora88@gmail.com");
            usuario.setUsername("Usuario de Prueba");
            
            // Crear una reserva de prueba
            Reserva reserva = new Reserva();
            reserva.setTokenVerificacion("TEST-TOKEN-123");
            reserva.setFechaReserva(LocalDateTime.now());
            
            // Enviar correo de prueba
            emailService.enviarCorreoReserva(usuario.getId(), reserva);
            
            return ResponseEntity.ok("Correo enviado exitosamente a " + usuario.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar correo: " + e.getMessage());
        }
    }
    
    @PostMapping("/reserva")
    public ResponseEntity<String> sendReservationEmail(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String token) {
        
        try {
            // Crear un usuario con los parámetros recibidos
            User usuario = new User();
            usuario.setId(1L);
            usuario.setEmail(email);
            usuario.setUsername(username);
            
            // Crear una reserva con el token recibido
            Reserva reserva = new Reserva();
            reserva.setTokenVerificacion(token);
            reserva.setFechaReserva(LocalDateTime.now());
            
            // Enviar correo de reserva
            emailService.enviarCorreoReserva(usuario.getId(), reserva);
            
            return ResponseEntity.ok("Correo de reserva enviado exitosamente a " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar correo: " + e.getMessage());
        }
    }
}