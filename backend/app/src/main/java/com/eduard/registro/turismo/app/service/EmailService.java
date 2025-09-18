package com.eduard.registro.turismo.app.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void enviarCorreoReserva(String to, String codigoReserva, String destino) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de Reserva - Sistema de Terminales");
        
        String contenido = buildEmailContent(codigoReserva, destino);
        message.setText(contenido);
        
        mailSender.send(message);
    }
    
    private String buildEmailContent(String codigoReserva, String destino) {
        return "Estimado/a usuario/a,\n\n" +
               "Su reserva ha sido confirmada exitosamente.\n\n" +
               "DETALLES DE LA RESERVA:\n" +
               "--------------------------------\n" +
               "Código de reserva: " + codigoReserva + "\n" +
               "Terminal: " + destino + "\n" +
               "Fecha: " + java.time.LocalDate.now() + "\n\n" +
               "INSTRUCCIONES:\n" +
               "--------------------------------\n" +
               "1. Presente este código de reserva al llegar a la terminal\n" +
               "2. Muestre este correo en su dispositivo móvil o impreso\n" +
               "3. Diríjase al mostrador de atención al cliente\n\n" +
               "IMPORTANTE:\n" +
               "--------------------------------\n" +
               "- Este código es válido solo para la fecha de reserva\n" +
               "- Llegue con al menos 30 minutos de anticipación\n" +
               "- Mantenga este correo como comprobante de su reserva\n\n" +
               "Gracias por confiar en nuestro servicio.\n\n" +
               "Atentamente,\n" +
               "El equipo de Terminales de Buses\n\n" +
               "--------------------------------\n" +
               "Este es un correo automático, por favor no responda.";
    }
    
    public void enviarCorreoContacto(String to, String asunto, String mensaje) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(asunto);
        message.setText(mensaje);
        mailSender.send(message);
    }
    
    public void enviarCorreoNotificacion(String to, String asunto, String mensaje) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(asunto);
        message.setText(mensaje);
        mailSender.send(message);
    }
}