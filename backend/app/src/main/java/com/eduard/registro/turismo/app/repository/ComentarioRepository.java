package com.eduard.registro.turismo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.Comentario;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByConductorCodigoUnico(String codigoUnico);
}