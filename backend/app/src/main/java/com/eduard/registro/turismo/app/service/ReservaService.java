package com.eduard.registro.turismo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eduard.registro.turismo.app.dto.ReservaDTO;
import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.model.SitioTuristicos;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.BusDisponibleRepository;
import com.eduard.registro.turismo.app.repository.ReservaRepository;
import com.eduard.registro.turismo.app.repository.SitioTuristicoRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private SitioTuristicoRepository sitioTuristicoRepository;
    
    @Autowired
    private UserRepository usuarioRepository;
    
    @Autowired
    private BusDisponibleRepository busDisponibleRepository;
    
    @Autowired
    private EmailService emailService;

    @Transactional
    public Reserva crearReserva(ReservaDTO reservaDTO) {
        // Generar código único
        String codigoUnico = generarCodigoUnico();
        
        // Buscar el sitio turístico
        Optional<SitioTuristicos> sitioOpt = sitioTuristicoRepository.findById(reservaDTO.getSitioTuristicoId());
        if (!sitioOpt.isPresent()) {
            throw new RuntimeException("Sitio turístico no encontrado");
        }
        
        // Validar que el usuario existe
        Optional<User> usuarioOpt = usuarioRepository.findByUsername(reservaDTO.getNombreUsuario());
        if (!usuarioOpt.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        // Validar que el email coincide con el del usuario
        User usuario = usuarioOpt.get();
        if (!usuario.getEmail().equalsIgnoreCase(reservaDTO.getEmailUsuario())) {
            throw new RuntimeException("El email no coincide con el del usuario");
        }
        
        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setCodigoUnico(codigoUnico);
        reserva.setSitioTuristicos(sitioOpt.get());
        reserva.setNombreUsuario(reservaDTO.getNombreUsuario());
        reserva.setEmailUsuario(reservaDTO.getEmailUsuario());
        reserva.setFechaReserva(LocalDateTime.now());
        
        // Guardar reserva primero
        reserva = reservaRepository.save(reserva);
        
        // Enviar correo de forma asíncrona para no bloquear la respuesta
        enviarCorreoAsincrono(reserva);
        
        return reserva;
    }

    @Async
    public CompletableFuture<Void> enviarCorreoAsincrono(Reserva reserva) {
        return CompletableFuture.runAsync(() -> {
            try {
                emailService.enviarCorreoReserva(reserva);
                System.out.println("Correo de reserva enviado exitosamente");
            } catch (Exception e) {
                System.err.println("Error al enviar correo de reserva: " + e.getMessage());
                // No lanzamos la excepción para no afectar la respuesta al cliente
            }
        });
    }

    public boolean validarReserva(String codigoUnico) {
        Optional<Reserva> reservaOpt = reservaRepository.findByCodigoUnico(codigoUnico);
        if (!reservaOpt.isPresent()) {
            return false;
        }
        
        Reserva reserva = reservaOpt.get();
        if (reserva.isValidada()) {
            return false;
        }
        
        reserva.setValidada(true);
        reservaRepository.save(reserva);
        
        // Enviar correo de validación de forma asíncrona
        enviarCorreoValidacionAsincrono(reserva);
        
        return true;
    }

    @Async
    public CompletableFuture<Void> enviarCorreoValidacionAsincrono(Reserva reserva) {
        return CompletableFuture.runAsync(() -> {
            try {
                emailService.enviarCorreoValidacion(reserva);
                System.out.println("Correo de validación enviado exitosamente");
            } catch (Exception e) {
                System.err.println("Error al enviar correo de validación: " + e.getMessage());
                // No lanzamos la excepción para no afectar la respuesta al cliente
            }
        });
    }

    public Optional<Reserva> findByCodigoUnico(String codigoUnico) {
        return reservaRepository.findByCodigoUnico(codigoUnico);
    }

    private String generarCodigoUnico() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Método programado que se ejecuta cada minuto para eliminar reservas expiradas
    // @Scheduled(fixedRate = 60000) // 60 segundos = 1 minuto
    @Transactional
    public void eliminarReservasExpiradas() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Reserva> reservasParaEliminar = reservaRepository.findReservasParaEliminar(ahora);
        
        if (!reservasParaEliminar.isEmpty()) {
            System.out.println("Eliminando " + reservasParaEliminar.size() + " reservas expiradas...");
            reservaRepository.deleteAll(reservasParaEliminar);
            
            // Opcional: Enviar notificación de eliminación
            reservasParaEliminar.forEach(reserva -> {
                enviarCorreoEliminacionAsincrono(reserva);
            });
        }
    }

    @Async
    public CompletableFuture<Void> enviarCorreoEliminacionAsincrono(Reserva reserva) {
        return CompletableFuture.runAsync(() -> {
            try {
                emailService.enviarCorreoEliminacion(reserva);
                System.out.println("Correo de eliminación enviado exitosamente");
            } catch (Exception e) {
                System.err.println("Error al enviar correo de eliminación: " + e.getMessage());
                // No lanzamos la excepción para no afectar la respuesta al cliente
            }
        });
    }
}