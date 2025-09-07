package com.eduard.registro.turismo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduard.registro.turismo.app.model.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
    Foto findByUserId(Long userId);
}