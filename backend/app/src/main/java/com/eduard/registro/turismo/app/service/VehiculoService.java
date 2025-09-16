package com.eduard.registro.turismo.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduard.registro.turismo.app.model.EstadoVehiculo;
import com.eduard.registro.turismo.app.model.Vehiculo;
import com.eduard.registro.turismo.app.repository.VehiculoRepository;

@Service
public class VehiculoService {
    @Autowired
    private VehiculoRepository vehiculoRepository;
    
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }
    
    public List<Vehiculo> obtenerVehiculosPorCategoria(String categoria) {
        return vehiculoRepository.findByCategoria(categoria);
    }
    
    public List<Vehiculo> sugerirVehiculos(String categoria, int capacidad) {
        List<Vehiculo> similares = vehiculoRepository.findByCapacidadGreaterThanEqual(capacidad);
        return similares.stream()
                .filter(v -> v.getCategoria().equals(categoria) && v.getEstado() == EstadoVehiculo.DISPONIBLE)
                .collect(Collectors.toList());
    }
    
    public Vehiculo actualizarVehiculo(String codigoUnico, Vehiculo vehiculoActualizado) {
        Vehiculo vehiculo = vehiculoRepository.findByCodigoUnico(codigoUnico);
        if (vehiculo == null) {
            throw new RuntimeException("Vehículo no encontrado");
        }
        
        // Actualizar campos permitidos (no el código único)
        vehiculo.setMarca(vehiculoActualizado.getMarca());
        vehiculo.setModelo(vehiculoActualizado.getModelo());
        vehiculo.setCategoria(vehiculoActualizado.getCategoria());
        vehiculo.setCapacidad(vehiculoActualizado.getCapacidad());
        
        return vehiculoRepository.save(vehiculo);
    }
    
    public void eliminarVehiculo(String codigoUnico) {
        Vehiculo vehiculo = vehiculoRepository.findByCodigoUnico(codigoUnico);
        if (vehiculo != null) {
            vehiculoRepository.delete(vehiculo);
        }
    }
    
    public List<Vehiculo> obtenerVehiculosPorEstado(EstadoVehiculo estado) {
        return vehiculoRepository.findByEstado(estado);
    }
}