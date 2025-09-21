package com.eduard.registro.turismo.app.service;

<<<<<<< HEAD
import com.eduard.registro.turismo.app.dto.LugarReservaDTO;
import com.eduard.registro.turismo.app.dto.ReservaDTO;
import com.eduard.registro.turismo.app.dto.ReservaRequest;
import com.eduard.registro.turismo.app.model.BusDisponible;
import com.eduard.registro.turismo.app.model.LugarReserva;
import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.model.TerminalBus;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.BusDisponibleRepository;
import com.eduard.registro.turismo.app.repository.LugarReservaRepository;
import com.eduard.registro.turismo.app.repository.ReservaRepository;
import com.eduard.registro.turismo.app.repository.TerminalBusRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;
import com.eduard.registro.turismo.app.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eduard.registro.turismo.app.dto.ReservaDTO;
import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.model.SitioTuristicos;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.ReservaRepository;
import com.eduard.registro.turismo.app.repository.SitioTuristicoRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;

import java.time.LocalDateTime;
<<<<<<< HEAD
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
=======
import java.util.Random;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b

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
<<<<<<< HEAD
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
=======
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b

    @Transactional
    public Reserva crearReserva(ReservaDTO reservaDTO) {
        // Generar código único
        String codigoUnico = generarCodigoUnico();
        
<<<<<<< HEAD
        // 2. Obtener el usuario desde la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // 3. Obtener terminal
        TerminalBus terminal = terminalBusRepository.findById(reservaRequest.getTerminalId())
                .orElseThrow(() -> new RuntimeException("Terminal no encontrada"));
        
        // 4. Obtener el bus
        BusDisponible bus = busDisponibleRepository.findById(reservaRequest.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus no encontrado"));
        
        // 5. Obtener el lugar por número y bus
        LugarReserva lugar = lugarReservaRepository.findByBusIdAndNumero(reservaRequest.getBusId(), reservaRequest.getNumeroLugar())
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
        
        // 6. Verificar si el lugar está disponible
        if (!lugar.isDisponible()) {
            throw new RuntimeException("El lugar no está disponible");
=======
        // Buscar el sitio turístico
        Optional<SitioTuristicos> sitioOpt = sitioTuristicoRepository.findById(reservaDTO.getSitioTuristicoId());
        if (!sitioOpt.isPresent()) {
            throw new RuntimeException("Sitio turístico no encontrado");
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b
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
<<<<<<< HEAD
        reserva.setUser(user);
        reserva.setTerminal(terminal);
        reserva.setLugarReservado(String.valueOf(reservaRequest.getNumeroLugar()));
=======
        reserva.setCodigoUnico(codigoUnico);
        reserva.setSitioTuristicos(sitioOpt.get());
        reserva.setNombreUsuario(reservaDTO.getNombreUsuario());
        reserva.setEmailUsuario(reservaDTO.getEmailUsuario());
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b
        reserva.setFechaReserva(LocalDateTime.now());
        
<<<<<<< HEAD
        // 8. Actualizar disponibilidad del lugar
        lugar.setDisponible(false);
        lugar.setFechaReserva(LocalDateTime.now());
        lugar.setUsuarioReserva(username);
        lugarReservaRepository.save(lugar);
        
        // 9. Guardar reserva
        reserva = reservaRepository.save(reserva);
        
        // 10. Enviar correo al email del usuario
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            emailService.enviarCorreoReserva(
                user.getEmail(), 
                reserva.getCodigoUnico(), 
                terminal.getNombre(),
                reserva.getLugarReservado(),
                bus.getHoraSalida().toString(),
                reserva.getFechaReserva().format(formatter),
                bus.getDestino(),
                "Wanni Connect"
            );
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
        }
        
        // 11. Programar la liberación del lugar después de 1 minuto
        programarLiberacionLugar(reservaRequest.getBusId(), reservaRequest.getNumeroLugar());
        
        // 12. Convertir a DTO
        return convertirAReservaDTO(reserva);
    }
    
    private void programarLiberacionLugar(Long busId, Integer numeroLugar) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        Runnable task = () -> {
            try {
                // Verificar si el lugar sigue reservado
                LugarReserva lugar = lugarReservaRepository.findByBusIdAndNumero(busId, numeroLugar)
                    .orElse(null);
                
                if (lugar != null && !lugar.isDisponible()) {
                    // Verificar si hay una reserva confirmada para este lugar
                    boolean reservaConfirmada = reservaRepository.existsByTerminalIdAndLugarReservadoAndConfirmadaTrue(
                        lugar.getBus().getTerminalBus().getId(), 
                        String.valueOf(numeroLugar)
                    );
                    
                    if (!reservaConfirmada) {
                        // Liberar el lugar
                        lugar.setDisponible(true);
                        lugar.setFechaReserva(null);
                        lugar.setUsuarioReserva(null);
                        lugarReservaRepository.save(lugar);
                        System.out.println("Lugar liberado automáticamente: Bus " + busId + ", Lugar " + numeroLugar);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al liberar lugar: " + e.getMessage());
            }
        };
        
        scheduler.schedule(task, 1, TimeUnit.MINUTES);
        scheduler.shutdown();
    }
    
    // Tarea programada para limpiar reservas no confirmadas
    @Scheduled(fixedRate = 30000) // Cada 30 segundos
    @Transactional
    public void liberarLugaresNoConfirmados() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(1);
        List<LugarReserva> lugaresNoConfirmados = lugarReservaRepository.findReservasExpiradas(limite);
        
        for (LugarReserva lugar : lugaresNoConfirmados) {
            // Verificar si hay una reserva confirmada para este lugar
            boolean reservaConfirmada = reservaRepository.existsByTerminalIdAndLugarReservadoAndConfirmadaTrue(
                lugar.getBus().getTerminalBus().getId(), 
                String.valueOf(lugar.getNumero())
            );
            
            if (!reservaConfirmada) {
                // Liberar el lugar
                lugar.setDisponible(true);
                lugar.setFechaReserva(null);
                lugar.setUsuarioReserva(null);
                lugarReservaRepository.save(lugar);
                System.out.println("Lugar liberado por tarea programada: Bus " + lugar.getBus().getId() + ", Lugar " + lugar.getNumero());
            }
        }
    }
    
    public List<LugarReservaDTO> obtenerLugaresPorBus(Long busId) {
        List<LugarReserva> lugares = lugarReservaRepository.findByBusId(busId);
        return lugares.stream()
                .map(this::convertirALugarReservaDTO)
                .collect(Collectors.toList());
    }
    
    private LugarReservaDTO convertirALugarReservaDTO(LugarReserva lugar) {
        LugarReservaDTO dto = new LugarReservaDTO();
        dto.setId(lugar.getId());
        dto.setNumero(lugar.getNumero());
        dto.setDisponible(lugar.isDisponible());
        dto.setUsuarioReserva(lugar.getUsuarioReserva());
        
        // Calcular tiempo restante si está reservado
        if (!lugar.isDisponible() && lugar.getFechaReserva() != null) {
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime limite = lugar.getFechaReserva().plusMinutes(1);
            
            if (ahora.isBefore(limite)) {
                long segundos = java.time.Duration.between(ahora, limite).getSeconds();
                dto.setTiempoRestante(String.format("%02d:%02d", segundos / 60, segundos % 60));
            } else {
                // Si ya pasó el tiempo, marcar como disponible
                lugar.setDisponible(true);
                lugar.setFechaReserva(null);
                lugar.setUsuarioReserva(null);
                lugarReservaRepository.save(lugar);
                dto.setDisponible(true);
            }
        }
        
        dto.setBusId(lugar.getBus().getId());
        return dto;
=======
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
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b
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