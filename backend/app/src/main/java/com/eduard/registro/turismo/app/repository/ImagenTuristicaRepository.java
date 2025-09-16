package com.eduard.registro.turismo.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.ImagenTuristica;

public interface ImagenTuristicaRepository extends JpaRepository<ImagenTuristica, Long> {
    List<ImagenTuristica> findBySitioTuristicoId(Long sitioId);
}

