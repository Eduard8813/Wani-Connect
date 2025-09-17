package com.eduard.registro.turismo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.Restaurante;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByTipoComida(String tipoComida);
    Restaurante findByCodigoUnico(String codigoUnico);
}