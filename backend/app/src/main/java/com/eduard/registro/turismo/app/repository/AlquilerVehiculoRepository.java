package com.eduard.registro.turismo.app.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eduard.registro.turismo.app.model.AlquilerVehiculo;

import java.util.List;

public interface AlquilerVehiculoRepository extends JpaRepository<AlquilerVehiculo, Long> {
    List<AlquilerVehiculo> findByTipoVehiculo(String tipoVehiculo);
    AlquilerVehiculo findByCodigoUnico(String codigoUnico);
}