package com.eduard.registro.turismo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;

=======
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eduard.registro.turismo.app.model.Reserva;

import java.time.LocalDateTime;
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Optional<Reserva> findByCodigoUnico(String codigoUnico);
<<<<<<< HEAD
    
    boolean existsByTerminalIdAndLugarReservadoAndConfirmadaTrue(Long terminalId, String lugarReservado);
=======

    @Query("SELECT r FROM Reserva r WHERE r.validada = true AND r.fechaEliminacion <= :ahora")
    List<Reserva> findReservasParaEliminar(@Param("ahora") LocalDateTime ahora);
    
    @Modifying
    @Query("DELETE FROM Reserva r WHERE r.validada = true AND r.fechaEliminacion <= :ahora")
    int deleteReservasExpiradas(@Param("ahora") LocalDateTime ahora);
>>>>>>> 8abe2777ba630eb70a61db9da6a988e72d943d7b
}