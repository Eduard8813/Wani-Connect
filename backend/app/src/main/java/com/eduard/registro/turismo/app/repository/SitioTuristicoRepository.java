package com.eduard.registro.turismo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduard.registro.turismo.app.model.SitioTuristico;

import java.util.List;
import java.util.Optional;

@Repository
public interface SitioTuristicoRepository extends JpaRepository<SitioTuristico, Long> {
    Optional<SitioTuristico> findByCodigoUnico(String codigoUnico);
    List<SitioTuristico> findByTipo(String tipo);
}