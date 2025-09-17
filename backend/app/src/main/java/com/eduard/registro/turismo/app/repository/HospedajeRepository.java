package com.eduard.registro.turismo.app.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.Hospedaje;

import java.util.List;

public interface HospedajeRepository extends JpaRepository<Hospedaje, Long> {
    List<Hospedaje> findByTipo(String tipo);
    Hospedaje findByCodigoUnico(String codigoUnico);
}