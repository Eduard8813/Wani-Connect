package com.eduard.registro.turismo.app.service;

import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private UserRepository usuarioRepository;

    public void enviarCorreoReserva(Long idUsuario, Reserva reserva) {
        try {
            User usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Crear un mensaje MIME para soportar HTML
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(usuario.getEmail());
            helper.setSubject("Confirmación de Reserva - Wani Connect");
            
            // Contenido HTML del correo
            String htmlContent = buildHtmlEmail(usuario, reserva);
            helper.setText(htmlContent, true); // true indica que es HTML
            
            mailSender.send(message);
            
            logger.info("Correo enviado exitosamente a: {}", usuario.getEmail());
        } catch (MessagingException e) {
            logger.error("Error al construir el correo: {}", e.getMessage());
            throw new RuntimeException("Error al construir el correo: " + e.getMessage());
        } catch (MailException e) {
            logger.error("Error al enviar correo: {}", e.getMessage());
            throw new RuntimeException("Error al enviar correo: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al enviar correo: {}", e.getMessage());
            throw new RuntimeException("Error inesperado al enviar correo: " + e.getMessage());
        }
    }
    
    private String buildHtmlEmail(User usuario, Reserva reserva) {
        // Logo de la compañía (usaremos una imagen en base64 o una URL)
        // Aquí usaremos una URL de ejemplo. Puedes reemplazarla con tu logo.
        String logoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRYS320TOiaGnhsFfETv1yLTXmpptKzWC9BnA&s";
        
        // Datos de reserva inventados
        String nombreConductor = "Carlos Rodríguez";
        String modeloVehiculo = "Toyota Prius 2023";
        String capacidad = "4 pasajeros";
        String categoria = "Híbrido";
        String fechaReserva = reserva.getFechaReserva().toString();
        String token = reserva.getTokenVerificacion();
        
        // Construir el HTML
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
                + ".container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; }"
                + ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }"
                + ".header img { max-width: 150px; }"
                + ".content { padding: 20px; }"
                + ".reservation-details { background-color: #f9f9f9; border-left: 4px solid #4CAF50; padding: 15px; margin: 20px 0; }"
                + ".reservation-details h2 { margin-top: 0; color: #333; }"
                + ".reservation-details p { margin: 5px 0; color: #555; }"
                + ".footer { background-color: #333; color: white; padding: 20px; text-align: center; font-size: 12px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<img src='" + logoUrl + "' alt='Wani Connect Logo'>"
                + "<h1>Wani Connect</h1>"
                + "<p>Tu servicio de transporte confiable</p>"
                + "</div>"
                + "<div class='content'>"
                + "<h2>Confirmación de Reserva</h2>"
                + "<p>Hola " + usuario.getUsername() + ",</p>"
                + "<p>Tu reserva ha sido confirmada. Aquí están los detalles:</p>"
                + "<div class='reservation-details'>"
                + "<h2>Detalles de la Reserva</h2>"
                + "<p><strong>Código de Reserva:</strong> " + token + "</p>"
                + "<p><strong>Fecha y Hora:</strong> " + fechaReserva + "</p>"
                + "<p><strong>Conductor:</strong> " + nombreConductor + "</p>"
                + "<p><strong>Vehículo:</strong> " + modeloVehiculo + "</p>"
                + "<p><strong>Categoría:</strong> " + categoria + "</p>"
                + "<p><strong>Capacidad:</strong> " + capacidad + "</p>"
                + "</div>"
                + "<p>Por favor, presenta este código al conductor: <strong>" + token + "</strong></p>"
                + "<p>Gracias por elegir Wani Connect. ¡Buen viaje!</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>© 2023 Wani Connect. Todos los derechos reservados.</p>"
                + "<p>Este es un correo automático, por favor no responder.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}