package com.eduard.registro.turismo.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.EstadoVehiculo;
import com.eduard.registro.turismo.app.model.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Vehiculo findByCodigoUnico(String codigoUnico);
    List<Vehiculo> findByCategoria(String categoria);
    List<Vehiculo> findByEstado(EstadoVehiculo estado);
    List<Vehiculo> findByCapacidadGreaterThanEqual(int capacidad);
}
