package com.eduard.registro.turismo.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.Conductor;

public interface ConductorRepository extends JpaRepository<Conductor, Long> {
    Conductor findByCodigoUnico(String codigoUnico);
    List<Conductor> findByVehiculoAsignadoIsNull();
}