package com.eduard.registro.turismo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eduard.registro.turismo.app.model.ReservaBus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaBusRepository extends JpaRepository<ReservaBus, Long> {
    
    Optional<ReservaBus> findByCodigoUnico(String codigoUnico);
    
    List<ReservaBus> findAllByCodigoUnico(String codigoUnico);
    
    @Query("SELECT r FROM ReservaBus r WHERE r.bus.id = :busId AND r.numeroLugar = :numeroLugar AND r.fechaExpiracion > :ahora AND r.validada = false")
    Optional<ReservaBus> findReservaActivaByBusAndLugar(@Param("busId") Long busId, @Param("numeroLugar") Integer numeroLugar, @Param("ahora") LocalDateTime ahora);
    
    @Query("SELECT r FROM ReservaBus r WHERE r.bus.id = :busId AND ((r.fechaExpiracion > :ahora AND r.validada = false) OR (r.validada = true))")
    List<ReservaBus> findReservasActivasByBus(@Param("busId") Long busId, @Param("ahora") LocalDateTime ahora);
    
    @Query("SELECT r FROM ReservaBus r WHERE r.fechaExpiracion <= :ahora AND r.validada = false")
    List<ReservaBus> findReservasExpiradas(@Param("ahora") LocalDateTime ahora);
    
    @Query("SELECT r FROM ReservaBus r WHERE r.validada = true AND r.fechaLiberacion <= :ahora")
    List<ReservaBus> findReservasValidadasExpiradas(@Param("ahora") LocalDateTime ahora);
    
    List<ReservaBus> findByNombreUsuario(String nombreUsuario);
    
    @Query("SELECT r FROM ReservaBus r WHERE r.bus.id = :busId")
    List<ReservaBus> findAllByBusId(@Param("busId") Long busId);
}