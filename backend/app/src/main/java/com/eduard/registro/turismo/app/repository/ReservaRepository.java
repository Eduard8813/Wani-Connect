package com.eduard.registro.turismo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eduard.registro.turismo.app.model.Reserva;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Optional<Reserva> findByCodigoUnico(String codigoUnico);

    @Query("SELECT r FROM Reserva r WHERE r.validada = true AND r.fechaEliminacion <= :ahora")
    List<Reserva> findReservasParaEliminar(@Param("ahora") LocalDateTime ahora);
    
    @Modifying
    @Query("DELETE FROM Reserva r WHERE r.validada = true AND r.fechaEliminacion <= :ahora")
    int deleteReservasExpiradas(@Param("ahora") LocalDateTime ahora);
}