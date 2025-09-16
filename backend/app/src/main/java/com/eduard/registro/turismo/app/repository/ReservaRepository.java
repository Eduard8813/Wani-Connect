package com.eduard.registro.turismo.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Optional<Reserva> findByTokenVerificacion(String tokenVerificacion);

}