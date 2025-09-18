package com.eduard.registro.turismo.app.service;

import com.eduard.registro.turismo.app.dto.LugarReservaDTO;
import com.eduard.registro.turismo.app.dto.ReservaDTO;
import com.eduard.registro.turismo.app.dto.ReservaRequest;
import com.eduard.registro.turismo.app.model.LugarReserva;
import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.model.TerminalBus;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.LugarReservaRepository;
import com.eduard.registro.turismo.app.repository.ReservaRepository;
import com.eduard.registro.turismo.app.repository.TerminalBusRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;
import com.eduard.registro.turismo.app.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private LugarReservaRepository lugarReservaRepository;
    
    @Autowired
    private TerminalBusRepository terminalBusRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Transactional
    public ReservaDTO crearReserva(String token, ReservaRequest reservaRequest) {
        // 1. Verificar el token y extraer el username
        String username = jwtTokenUtil.extractUsername(token);
        
        // 2. Obtener el usuario desde la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // 3. Obtener el email del usuario
        String userEmail = user.getEmail();
        
        // 4. Obtener terminal
        TerminalBus terminal = terminalBusRepository.findById(reservaRequest.getTerminalId())
                .orElseThrow(() -> new RuntimeException("Terminal no encontrada"));
        
        // 5. Obtener lugar
        LugarReserva lugar = lugarReservaRepository.findById(reservaRequest.getLugarId())
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
        
        // 6. Verificar si el lugar está disponible
        if (!lugar.isDisponible()) {
            throw new RuntimeException("El lugar no está disponible");
        }
        
        // 7. Crear reserva
        Reserva reserva = new Reserva();
        reserva.setUser(user);
        reserva.setTerminal(terminal);
        reserva.setLugarReservado(lugar.getNombre());
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setConfirmada(true);
        
        // 8. Actualizar disponibilidad del lugar
        lugar.setDisponible(false);
        lugarReservaRepository.save(lugar);
        
        // 9. Guardar reserva
        reserva = reservaRepository.save(reserva);
        
        // 10. Enviar correo al email del usuario desde la base de datos
        try {
            emailService.enviarCorreoReserva(userEmail, reserva.getCodigoUnico(), terminal.getNombre());
            System.out.println("Correo enviado exitosamente a: " + userEmail);
        } catch (Exception e) {
            // Manejar error de correo pero continuar con la reserva
            System.err.println("Error al enviar correo: " + e.getMessage());
            // Aquí podrías implementar un sistema de reintentos o logging
        }
        
        // 11. Convertir a DTO
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setCodigoUnico(reserva.getCodigoUnico());
        dto.setTerminalId(terminal.getId());
        dto.setTerminalNombre(terminal.getNombre());
        dto.setUserId(user.getId());
        dto.setUserName(user.getUsername());
        dto.setLugarReservado(reserva.getLugarReservado());
        dto.setFechaReserva(reserva.getFechaReserva().toString());
        dto.setConfirmada(reserva.isConfirmada());
        
        return dto;
    }
    
    public List<LugarReservaDTO> obtenerLugaresPorTerminal(Long terminalId) {
        List<LugarReserva> lugares = lugarReservaRepository.findByTerminalId(terminalId);
        return lugares.stream()
                .map(this::convertirALugarReservaDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReservaDTO> obtenerReservasPorUsuario(Long userId) {
        List<Reserva> reservas = reservaRepository.findByUserId(userId);
        return reservas.stream()
                .map(this::convertirAReservaDTO)
                .collect(Collectors.toList());
    }
    
    public ReservaDTO obtenerReservaPorCodigo(String codigoUnico) {
        Reserva reserva = reservaRepository.findByCodigoUnico(codigoUnico)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return convertirAReservaDTO(reserva);
    }
    
    private LugarReservaDTO convertirALugarReservaDTO(LugarReserva lugar) {
        LugarReservaDTO dto = new LugarReservaDTO();
        dto.setId(lugar.getId());
        dto.setNombre(lugar.getNombre());
        dto.setDisponible(lugar.isDisponible());
        dto.setTerminalId(lugar.getTerminal().getId());
        return dto;
    }
    
    private ReservaDTO convertirAReservaDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setCodigoUnico(reserva.getCodigoUnico());
        dto.setTerminalId(reserva.getTerminal().getId());
        dto.setTerminalNombre(reserva.getTerminal().getNombre());
        dto.setUserId(reserva.getUser().getId());
        dto.setUserName(reserva.getUser().getUsername());
        dto.setLugarReservado(reserva.getLugarReservado());
        dto.setFechaReserva(reserva.getFechaReserva().toString());
        dto.setConfirmada(reserva.isConfirmada());
        return dto;
    }
}