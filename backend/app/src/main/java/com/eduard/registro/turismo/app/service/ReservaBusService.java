package com.eduard.registro.turismo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eduard.registro.turismo.app.dto.ReservaBusDTO;
import com.eduard.registro.turismo.app.dto.LugarBusDTO;
import com.eduard.registro.turismo.app.model.ReservaBus;
import com.eduard.registro.turismo.app.model.BusDisponible;
import com.eduard.registro.turismo.app.model.TerminalBus;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.ReservaBusRepository;
import com.eduard.registro.turismo.app.repository.BusDisponibleRepository;
import com.eduard.registro.turismo.app.repository.TerminalBusRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;
import com.eduard.registro.turismo.app.service.EmailService;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservaBusService {

    private static final AtomicLong counter = new AtomicLong(0);
    private static final Object lockObject = new Object();

    @Autowired
    private ReservaBusRepository reservaBusRepository;
    
    @Autowired
    private BusDisponibleRepository busDisponibleRepository;
    
    @Autowired
    private TerminalBusRepository terminalBusRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Transactional
    public List<ReservaBus> crearReserva(ReservaBusDTO reservaDTO) {
        // Validar que el terminal existe
        TerminalBus terminal = terminalBusRepository.findById(reservaDTO.getTerminalId())
            .orElseThrow(() -> new RuntimeException("Terminal no encontrado"));
        
        // Validar que el bus existe
        BusDisponible bus = busDisponibleRepository.findById(reservaDTO.getBusId())
            .orElseThrow(() -> new RuntimeException("Bus no encontrado"));
        
        // Validar que el usuario existe
        User usuario = userRepository.findByUsername(reservaDTO.getNombreUsuario())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Validar que el email coincide
        if (!usuario.getEmail().equalsIgnoreCase(reservaDTO.getEmailUsuario())) {
            throw new RuntimeException("El email no coincide con el del usuario");
        }
        
        List<Integer> lugares = reservaDTO.getNumerosLugar();
        if (lugares == null || lugares.isEmpty()) {
            throw new IllegalArgumentException("Debe especificar al menos un lugar");
        }
        
        List<ReservaBus> reservasCreadas = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        
        // Filtrar lugares válidos y asignar automáticamente si es necesario
        List<Integer> lugaresValidos = new ArrayList<>();
        for (Integer numeroLugar : lugares) {
            if (numeroLugar >= 1 && numeroLugar <= bus.getTotalLugares()) {
                lugaresValidos.add(numeroLugar);
            }
        }
        
        // Si no hay lugares válidos, asignar automáticamente
        if (lugaresValidos.isEmpty()) {
            for (int i = 1; i <= Math.min(lugares.size(), bus.getTotalLugares()); i++) {
                Optional<ReservaBus> reservaExistente = reservaBusRepository.findReservaActivaByBusAndLugar(
                    bus.getId(), i, ahora);
                if (!reservaExistente.isPresent()) {
                    lugaresValidos.add(i);
                    if (lugaresValidos.size() >= lugares.size()) break;
                }
            }
        }
        
        if (lugaresValidos.isEmpty()) {
            throw new IllegalArgumentException("No hay lugares disponibles en el bus");
        }
        
        // Validar que los lugares estén disponibles
        System.out.println("=== VALIDANDO LUGARES ===");
        System.out.println("Bus ID: " + bus.getId() + ", Lugares a validar: " + lugaresValidos);
        System.out.println("Fecha actual: " + ahora);
        
        for (Integer numeroLugar : lugaresValidos) {
            System.out.println("Validando lugar: " + numeroLugar);
            
            Optional<ReservaBus> reservaExistente = reservaBusRepository.findReservaActivaByBusAndLugar(
                bus.getId(), numeroLugar, ahora);
            
            if (reservaExistente.isPresent()) {
                ReservaBus reserva = reservaExistente.get();
                System.out.println("LUGAR OCUPADO - Lugar: " + numeroLugar + ", Validada: " + reserva.isValidada() + ", Expira: " + reserva.getFechaExpiracion());
                throw new IllegalArgumentException("El lugar " + numeroLugar + " ya está reservado");
            } else {
                System.out.println("Lugar " + numeroLugar + " disponible");
            }
        }
        
        LocalDateTime fechaReserva = LocalDateTime.now();
        
        // Las reservas no validadas expiran en la hora de salida del bus
        LocalDateTime fechaExpiracion;
        LocalDateTime horaSalidaHoy = LocalDateTime.of(fechaReserva.toLocalDate(), bus.getHoraSalida());
        
        if (horaSalidaHoy.isAfter(fechaReserva)) {
            fechaExpiracion = horaSalidaHoy;
        } else {
            fechaExpiracion = horaSalidaHoy.plusDays(1);
        }
        
        // Crear reservas con código único individual
        for (Integer numeroLugar : lugaresValidos) {
            String codigoUnico = generarCodigoUnicoThreadSafe();
            
            ReservaBus reserva = new ReservaBus();
            reserva.setTerminal(terminal);
            reserva.setBus(bus);
            reserva.setNumeroLugar(numeroLugar);
            reserva.setNombreUsuario(reservaDTO.getNombreUsuario());
            reserva.setEmailUsuario(reservaDTO.getEmailUsuario());
            reserva.setCodigoUnico(codigoUnico);
            reserva.setFechaReserva(fechaReserva);
            reserva.setFechaExpiracion(fechaExpiracion);
            
            reservasCreadas.add(reservaBusRepository.save(reserva));
        }
        
        // Actualizar lugares disponibles del bus
        bus.setLugaresDisponibles(bus.getLugaresDisponibles() - lugaresValidos.size());
        busDisponibleRepository.save(bus);
        
        // Enviar correo con todas las reservas
        try {
            emailService.enviarCorreoReservaBusMultiple(reservasCreadas);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de reservas: " + e.getMessage());
        }
        
        return reservasCreadas;
    }
    
    public List<LugarBusDTO> obtenerLugaresBus(Long busId) {
        BusDisponible bus = busDisponibleRepository.findById(busId)
            .orElseThrow(() -> new RuntimeException("Bus no encontrado"));
        
        LocalDateTime ahora = LocalDateTime.now();
        List<ReservaBus> reservasActivas = reservaBusRepository.findReservasActivasByBus(busId, ahora);
        
        List<LugarBusDTO> lugares = new ArrayList<>();
        
        for (int i = 1; i <= bus.getTotalLugares(); i++) {
            final int numeroLugar = i;
            Optional<ReservaBus> reserva = reservasActivas.stream()
                .filter(r -> r.getNumeroLugar().equals(numeroLugar))
                .findFirst();
            
            if (reserva.isPresent()) {
                ReservaBus reservaActual = reserva.get();
                
                {
                    // Lugar reservado - calcular tiempo restante hasta expiración
                    Duration duracion = Duration.between(ahora, reservaActual.getFechaExpiracion());
                    String tiempoRestante = String.format("%02d:%02d", 
                        duracion.toHours(), duracion.toMinutesPart());
                    
                    // Si está validada, calcular tiempo hasta liberación
                    String tiempoLiberacion = null;
                    if (reservaActual.isValidada() && reservaActual.getFechaLiberacion() != null) {
                        Duration duracionLiberacion = Duration.between(ahora, reservaActual.getFechaLiberacion());
                        if (!duracionLiberacion.isNegative()) {
                            tiempoLiberacion = String.format("%02d:%02d:%02d", 
                                duracionLiberacion.toHours(), 
                                duracionLiberacion.toMinutesPart(),
                                duracionLiberacion.toSecondsPart());
                        }
                    }
                    
                    lugares.add(new LugarBusDTO(i, false, tiempoRestante, tiempoLiberacion, reservaActual.isValidada()));
                }
            } else {
                // Lugar disponible
                lugares.add(new LugarBusDTO(i, true));
            }
        }
        
        return lugares;
    }
    
    @Transactional
    public boolean validarReserva(String codigoUnico) {
        List<ReservaBus> reservas = reservaBusRepository.findAllByCodigoUnico(codigoUnico);
        if (reservas.isEmpty()) {
            return false;
        }
        
        ReservaBus primeraReserva = reservas.get(0);
        LocalDateTime ahora = LocalDateTime.now();
        
        // Solo se puede validar si no está validada y no ha expirado
        if (primeraReserva.isValidada() || primeraReserva.getFechaExpiracion().isBefore(ahora)) {
            return false;
        }
        
        // Enviar correo de validación (solo uno para todas las reservas)
        try {
            emailService.enviarCorreoValidacionBus(primeraReserva);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de validación: " + e.getMessage());
        }
        
        // Marcar todas las reservas como validadas y establecer fecha de liberación (24 horas)
        LocalDateTime fechaLiberacion = LocalDateTime.now().plusHours(24);
        for (ReservaBus reserva : reservas) {
            reserva.setValidada(true);
            reserva.setFechaLiberacion(fechaLiberacion);
            reservaBusRepository.save(reserva);
        }
        
        // Programar liberación automática en 24 horas
        final Long busId = primeraReserva.getBus().getId();
        final String codigoUnicoFinal = codigoUnico;
        scheduler.schedule(() -> {
            try {
                // Buscar reservas actuales por código
                List<ReservaBus> reservasActuales = reservaBusRepository.findAllByCodigoUnico(codigoUnicoFinal);
                if (!reservasActuales.isEmpty()) {
                    // Eliminar reservas liberadas
                    reservaBusRepository.deleteAll(reservasActuales);
                    
                    // Liberar lugares
                    BusDisponible busActual = busDisponibleRepository.findById(busId).orElse(null);
                    if (busActual != null) {
                        busActual.setLugaresDisponibles(busActual.getLugaresDisponibles() + reservasActuales.size());
                        busDisponibleRepository.save(busActual);
                    }
                    
                    // Enviar correo de liberación
                    emailService.enviarCorreoLiberacionBus(reservasActuales.get(0));
                }
            } catch (Exception e) {
                System.err.println("Error al liberar reservas: " + e.getMessage());
                e.printStackTrace();
            }
        }, 24, TimeUnit.HOURS);
        
        return true;
    }
    
    @Transactional
    public void limpiarReservasExpiradas() {
        LocalDateTime ahora = LocalDateTime.now();
        
        // Limpiar reservas no validadas expiradas
        List<ReservaBus> reservasExpiradas = reservaBusRepository.findReservasExpiradas(ahora);
        for (ReservaBus reserva : reservasExpiradas) {
            BusDisponible bus = reserva.getBus();
            bus.setLugaresDisponibles(bus.getLugaresDisponibles() + 1);
            busDisponibleRepository.save(bus);
        }
        reservaBusRepository.deleteAll(reservasExpiradas);
        
        // Limpiar reservas validadas que han cumplido 24 horas
        List<ReservaBus> reservasValidadasExpiradas = reservaBusRepository.findReservasValidadasExpiradas(ahora);
        for (ReservaBus reserva : reservasValidadasExpiradas) {
            BusDisponible bus = reserva.getBus();
            bus.setLugaresDisponibles(bus.getLugaresDisponibles() + 1);
            busDisponibleRepository.save(bus);
        }
        reservaBusRepository.deleteAll(reservasValidadasExpiradas);
    }
    
    @Transactional
    public boolean liberarBusPorConductor(Long busId) {
        BusDisponible bus = busDisponibleRepository.findById(busId)
            .orElseThrow(() -> new RuntimeException("Bus no encontrado"));
        
        // Obtener todas las reservas del bus
        List<ReservaBus> todasReservas = reservaBusRepository.findAllByBusId(busId);
        
        if (!todasReservas.isEmpty()) {
            // Eliminar todas las reservas
            reservaBusRepository.deleteAll(todasReservas);
            
            // Restaurar todos los lugares disponibles
            bus.setLugaresDisponibles(bus.getTotalLugares());
            busDisponibleRepository.save(bus);
            
            return true;
        }
        
        return false;
    }
    
    public Optional<ReservaBus> findByCodigoUnico(String codigoUnico) {
        return reservaBusRepository.findByCodigoUnico(codigoUnico);
    }
    
    public BusDisponible obtenerInfoBus(Long busId) {
        return busDisponibleRepository.findById(busId)
            .orElseThrow(() -> new RuntimeException("Bus no encontrado"));
    }
    
    public String debugReservasBus(Long busId) {
        LocalDateTime ahora = LocalDateTime.now();
        List<ReservaBus> todasReservas = reservaBusRepository.findAllByBusId(busId);
        
        StringBuilder debug = new StringBuilder();
        debug.append("=== DEBUG RESERVAS BUS ").append(busId).append(" ===").append("\n");
        debug.append("Fecha actual: ").append(ahora).append("\n");
        debug.append("Total reservas en BD: ").append(todasReservas.size()).append("\n\n");
        
        for (ReservaBus reserva : todasReservas) {
            boolean esActiva = reserva.getFechaExpiracion().isAfter(ahora) && !reserva.isValidada();
            debug.append("Lugar: ").append(reserva.getNumeroLugar())
                 .append(", Validada: ").append(reserva.isValidada())
                 .append(", Expira: ").append(reserva.getFechaExpiracion())
                 .append(", Expirada: ").append(reserva.getFechaExpiracion().isBefore(ahora))
                 .append(", ACTIVA: ").append(esActiva)
                 .append(", Usuario: ").append(reserva.getNombreUsuario())
                 .append(", Código: ").append(reserva.getCodigoUnico())
                 .append("\n");
        }
        
        return debug.toString();
    }
    
    private String generarCodigoUnicoThreadSafe() {
        synchronized (lockObject) {
            String codigo;
            int maxIntentos = 10;
            
            for (int intento = 0; intento < maxIntentos; intento++) {
                // Usar solo UUID para máxima unicidad
                codigo = "RB-" + java.util.UUID.randomUUID().toString();
                
                // Verificar si ya existe
                if (!reservaBusRepository.findByCodigoUnico(codigo).isPresent()) {
                    return codigo;
                }
                
                // Si existe, esperar un poco antes del siguiente intento
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // Si después de todos los intentos no se pudo generar, usar timestamp + UUID
            return "RB-" + System.nanoTime() + "-" + java.util.UUID.randomUUID().toString();
        }
    }
}