package com.eduard.registro.turismo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.eduard.registro.turismo.app.model.Reserva;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
<<<<<<< HEAD
    public void enviarCorreoReserva(String destinatario, String codigoReserva, String terminal, 
                                   String numeroLugar, String horaSalida, String fechaReserva, 
                                   String destino, String transporte) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Confirmación de Reserva - Wanni Connect");
        
        String contenido = String.format(
            "Estimado usuario,\n\n" +
            "Su reserva ha sido confirmada con éxito. Aquí están los detalles:\n\n" +
            "Código de Reserva: %s\n" +
            "Terminal: %s\n" +
            "Destino: %s\n" +
            "Número de Asiento: %s\n" +
            "Hora de Salida: %s\n" +
            "Fecha de Reserva: %s\n" +
            "Transporte: %s\n\n" +
            "Por favor, presente este código al embarcar.\n\n" +
            "Gracias por usar Wanni Connect.",
            codigoReserva, terminal, destino, numeroLugar, horaSalida, fechaReserva, transporte
        );
        
        message.setText(contenido);
        mailSender.send(message);
=======
    @Autowired
    private QRCodeService qrCodeService;

    public void enviarCorreoReserva(Reserva reserva) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reserva.getEmailUsuario());
            message.setSubject("Confirmación de Reserva");
            message.setText("Hola " + reserva.getNombreUsuario() + ",\n\n" +
                            "Tu reserva ha sido creada con éxito.\n" +
                            "Código de reserva: " + reserva.getCodigoUnico() + "\n\n" +
                            "Por favor, guarda este código para validar tu reserva en el sitio turístico.");
            
            // Enviar el correo con un timeout más largo
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de reserva: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo de confirmación", e);
        }
    }

    public void enviarCorreoValidacion(Reserva reserva) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reserva.getEmailUsuario());
            message.setSubject("Reserva Validada");
            message.setText("Hola " + reserva.getNombreUsuario() + ",\n\n" +
                            "Tu reserva con código " + reserva.getCodigoUnico() + " ha sido validada.\n\n" +
                            "Gracias por visitarnos.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de validación: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo de validación", e);
        }
    }

    public void enviarCorreoEliminacion(Reserva reserva) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reserva.getEmailUsuario());
            message.setSubject("Reserva Eliminada del Sistema");
            message.setText("Hola " + reserva.getNombreUsuario() + ",\n\n" +
                            "Tu reserva con código " + reserva.getCodigoUnico() + " ha sido eliminada de nuestro sistema después de ser validada.\n\n" +
                            "Gracias por tu visita.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de eliminación: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo de eliminación", e);
        }
    }

    public void enviarCorreoReservaBus(com.eduard.registro.turismo.app.model.ReservaBus reserva) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(reserva.getEmailUsuario());
            helper.setSubject("Confirmación de Reserva de Bus");
            helper.setText("Hola " + reserva.getNombreUsuario() + ",\n\n" +
                          "Tu reserva de bus ha sido creada con éxito.\n" +
                          "Código de reserva: " + reserva.getCodigoUnico() + "\n" +
                          "Lugar número: " + reserva.getNumeroLugar() + "\n\n" +
                          "Presenta el código QR adjunto para validar tu reserva.");
            
            // Generar y adjuntar QR
            byte[] qrCode = qrCodeService.generateQRCode(reserva.getCodigoUnico());
            helper.addAttachment("reserva-qr.png", new ByteArrayDataSource(qrCode, "image/png"));
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de reserva de bus: " + e.getMessage());
        }
    }

    public void enviarCorreoValidacionBus(com.eduard.registro.turismo.app.model.ReservaBus reserva) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reserva.getEmailUsuario());
            message.setSubject("Reserva de Bus Validada");
            message.setText("Hola " + reserva.getNombreUsuario() + ",\n\n" +
                            "Tu reserva de bus con código " + reserva.getCodigoUnico() + " ha sido validada.\n" +
                            "Lugar número: " + reserva.getNumeroLugar() + "\n\n" +
                            "Gracias por usar nuestro servicio.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de validación de bus: " + e.getMessage());
        }
    }

    public void enviarCorreoEliminacionBus(com.eduard.registro.turismo.app.model.ReservaBus reserva) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reserva.getEmailUsuario());
            message.setSubject("Reserva de Bus Eliminada");
            message.setText("Hola " + reserva.getNombreUsuario() + ",\n\n" +
                            "Tu reserva de bus con código " + reserva.getCodigoUnico() + " ha sido eliminada del sistema.\n" +
                            "El lugar " + reserva.getNumeroLugar() + " estará disponible en 5 minutos.\n\n" +
                            "Gracias por tu visita.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de eliminación de bus: " + e.getMessage());
        }
    }
    
    public void enviarCorreoLiberacionBus(com.eduard.registro.turismo.app.model.ReservaBus reserva) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reserva.getEmailUsuario());
            message.setSubject("Reserva de Bus Liberada Automáticamente");
            message.setText("Hola " + reserva.getNombreUsuario() + ",\n\n" +
                            "Tu reserva de bus con código " + reserva.getCodigoUnico() + " ha sido liberada automáticamente después de 24 horas.\n" +
                            "El lugar " + reserva.getNumeroLugar() + " ya está disponible para otros usuarios.\n\n" +
                            "Gracias por usar nuestro servicio.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de liberación de bus: " + e.getMessage());
        }
    }
    
    public void enviarCorreoReservaBusMultiple(java.util.List<com.eduard.registro.turismo.app.model.ReservaBus> reservas) {
        if (reservas == null || reservas.isEmpty()) return;
        
        try {
            com.eduard.registro.turismo.app.model.ReservaBus primeraReserva = reservas.get(0);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(primeraReserva.getEmailUsuario());
            helper.setSubject("Confirmación de Reserva de Bus - Múltiples Lugares");
            
            StringBuilder lugares = new StringBuilder();
            for (com.eduard.registro.turismo.app.model.ReservaBus reserva : reservas) {
                lugares.append("Lugar ").append(reserva.getNumeroLugar())
                       .append(": ").append(reserva.getCodigoUnico()).append("\n");
            }
            
            helper.setText("Hola " + primeraReserva.getNombreUsuario() + ",\n\n" +
                          "Tus reservas de bus han sido creadas con éxito.\n\n" +
                          "Detalles de las reservas:\n" + lugares.toString() + "\n" +
                          "Presenta los códigos QR adjuntos para validar tus reservas.");
            
            // Generar y adjuntar QR para cada reserva
            for (int i = 0; i < reservas.size(); i++) {
                com.eduard.registro.turismo.app.model.ReservaBus reserva = reservas.get(i);
                byte[] qrCode = qrCodeService.generateQRCode(reserva.getCodigoUnico());
                helper.addAttachment("reserva-lugar-" + reserva.getNumeroLugar() + "-qr.png", 
                                   new ByteArrayDataSource(qrCode, "image/png"));
            }
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de reservas múltiples: " + e.getMessage());
        }
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b
    }
}