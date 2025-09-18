package com.eduard.registro.turismo.app.repository;

import com.eduard.registro.turismo.app.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUserId(Long userId);
    Optional<Reserva> findByCodigoUnico(String codigoUnico);
}