package com.eduard.registro.turismo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.Historial;

public interface HistorialRepository extends JpaRepository<Historial, Long> {
    
}