package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private EmailService emailService;
    
    @GetMapping("/enviar-correo")
    public String enviarCorreoDePrueba(
            @RequestParam String to,
            @RequestParam String codigo,
            @RequestParam String destino) {
        try {
            Reserva reserva = new Reserva();
            reserva.setEmailUsuario(to);
            reserva.setCodigoUnico(codigo);
            reserva.setNombreUsuario("Usuario de Prueba");
            
            emailService.enviarCorreoReserva(reserva);
            return "Correo enviado exitosamente a " + to;
        } catch (Exception e) {
            return "Error al enviar correo: " + e.getMessage();
        }
    }
}