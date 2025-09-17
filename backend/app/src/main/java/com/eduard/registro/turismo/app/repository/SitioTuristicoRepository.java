package com.eduard.registro.turismo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.SitioTuristicos;

import java.util.List;

public interface SitioTuristicoRepository extends JpaRepository<SitioTuristicos, String> {
    List<SitioTuristicos> findByTipoLugar(String tipoLugar);
    SitioTuristicos findByCodigoUnico(String codigoUnico);
}