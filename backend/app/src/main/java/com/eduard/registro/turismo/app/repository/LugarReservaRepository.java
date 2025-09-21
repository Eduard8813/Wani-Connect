package com.eduard.registro.turismo.app.repository;

import com.eduard.registro.turismo.app.model.LugarReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LugarReservaRepository extends JpaRepository<LugarReserva, Long> {
    List<LugarReserva> findByBusId(Long busId);
    
    Optional<LugarReserva> findByBusIdAndNumero(Long busId, Integer numero);
    
    @Query("SELECT lr FROM LugarReserva lr WHERE lr.disponible = false AND lr.fechaReserva < :fechaLimite")
    List<LugarReserva> findReservasExpiradas(@Param("fechaLimite") LocalDateTime fechaLimite);
    
    @Query("SELECT lr FROM LugarReserva lr WHERE lr.bus.id = :busId AND lr.disponible = false")
    List<LugarReserva> findLugaresReservadosByBusId(@Param("busId") Long busId);
}