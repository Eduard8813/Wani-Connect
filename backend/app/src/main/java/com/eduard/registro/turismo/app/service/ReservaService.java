// ReservaService.java
package com.eduard.registro.turismo.app.service;

import com.eduard.registro.turismo.app.model.Conductor;
import com.eduard.registro.turismo.app.model.EstadoReserva;
import com.eduard.registro.turismo.app.model.EstadoVehiculo;
import com.eduard.registro.turismo.app.model.Historial;
import com.eduard.registro.turismo.app.model.Reserva;
import com.eduard.registro.turismo.app.model.Vehiculo;
import com.eduard.registro.turismo.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private HistorialRepository historialRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ConductorRepository conductorRepository;
    
    @Autowired
    private VehiculoRepository vehiculoRepository;
    
    public Reserva crearReserva(Long idUsuario, String categoria, int capacidad) {
        // Buscar conductor y vehículo disponibles
        List<Conductor> conductoresDisponibles = conductorRepository.findByVehiculoAsignadoIsNull();
        List<Vehiculo> vehiculosDisponibles = vehiculoRepository.findByEstado(EstadoVehiculo.DISPONIBLE);
        
        if (conductoresDisponibles.isEmpty() || vehiculosDisponibles.isEmpty()) {
            throw new RuntimeException("No hay conductores o vehículos disponibles");
        }
        
        // Seleccionar el primer conductor y vehículo disponibles
        Conductor conductor = conductoresDisponibles.get(0);
        Vehiculo vehiculo = vehiculosDisponibles.get(0);
        
        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setIdUsuario(idUsuario);
        reserva.setTokenVerificacion(UUID.randomUUID().toString());
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setConductor(conductor);
        reserva.setVehiculo(vehiculo);
        
        reserva = reservaRepository.save(reserva);
        
        // Enviar correo con datos de reserva
        emailService.enviarCorreoReserva(idUsuario, reserva);
        
        return reserva;
    }
    
    public void confirmarReserva(String token) {
        Reserva reserva = reservaRepository.findByTokenVerificacion(token)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        reservaRepository.save(reserva);
        
        // Actualizar estado del vehículo
        Vehiculo vehiculo = reserva.getVehiculo();
        vehiculo.setEstado(EstadoVehiculo.EN_USO);
        vehiculoRepository.save(vehiculo);
        
        // Guardar en historial
        Historial historial = new Historial();
        historial.setIdUsuario(reserva.getIdUsuario());
        historial.setTokenVerificacion(reserva.getTokenVerificacion());
        historial.setFechaReserva(reserva.getFechaReserva());
        historial.setConductor(reserva.getConductor());
        historial.setVehiculo(reserva.getVehiculo());
        historialRepository.save(historial);
        
        // Enviar correo de confirmación
        emailService.enviarCorreoConfirmacion(reserva.getIdUsuario(), reserva);
    }
}