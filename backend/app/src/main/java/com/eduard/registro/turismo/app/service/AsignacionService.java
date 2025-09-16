package com.eduard.registro.turismo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduard.registro.turismo.app.model.Conductor;
import com.eduard.registro.turismo.app.model.EstadoVehiculo;
import com.eduard.registro.turismo.app.model.Vehiculo;
import com.eduard.registro.turismo.app.repository.ConductorRepository;
import com.eduard.registro.turismo.app.repository.VehiculoRepository;

@Service
public class AsignacionService {
    @Autowired
    private ConductorRepository conductorRepository;
    
    @Autowired
    private VehiculoRepository vehiculoRepository;
    
    public void asignarConductorVehiculo(String codigoConductor, String codigoVehiculo) {
        Conductor conductor = conductorRepository.findByCodigoUnico(codigoConductor);
        Vehiculo vehiculo = vehiculoRepository.findByCodigoUnico(codigoVehiculo);
        
        if (conductor == null || vehiculo == null) {
            throw new RuntimeException("Conductor o vehículo no encontrado");
        }
        
        if (conductor.getVehiculoAsignado() != null || vehiculo.getConductor() != null) {
            throw new RuntimeException("Conductor o vehículo ya asignado");
        }
        
        conductor.setVehiculoAsignado(vehiculo);
        vehiculo.setConductor(conductor);
        vehiculo.setEstado(EstadoVehiculo.EN_USO);
        
        conductorRepository.save(conductor);
        vehiculoRepository.save(vehiculo);
    }
}
