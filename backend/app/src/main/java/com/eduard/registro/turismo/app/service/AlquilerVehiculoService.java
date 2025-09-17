package com.eduard.registro.turismo.app.service;

import com.eduard.registro.turismo.app.dto.AlquilerVehiculoDTO;
import com.eduard.registro.turismo.app.model.AlquilerVehiculo;
import com.eduard.registro.turismo.app.model.ImagenAlquilerVehiculo;
import com.eduard.registro.turismo.app.repository.AlquilerVehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;

@Service
public class AlquilerVehiculoService {
    
    @Autowired
    private AlquilerVehiculoRepository repository;
    
    @Transactional
    public AlquilerVehiculo crearAlquilerVehiculo(@Valid AlquilerVehiculoDTO alquilerVehiculoDTO) {
        AlquilerVehiculo alquilerVehiculo = alquilerVehiculoDTO.getAlquilerVehiculo();
        List<ImagenAlquilerVehiculo> imagenes = alquilerVehiculoDTO.getImagenes();
        
        // Primero guardamos el alquiler de vehículo sin imágenes para obtener el ID
        AlquilerVehiculo alquilerGuardado = repository.save(alquilerVehiculo);
        
        // Luego establecemos la relación bidireccional y guardamos las imágenes
        if (imagenes != null && !imagenes.isEmpty()) {
            for (ImagenAlquilerVehiculo imagen : imagenes) {
                imagen.setAlquilerVehiculo(alquilerGuardado);
            }
            alquilerGuardado.setImagenes(imagenes);
            // Volvemos a guardar el alquiler de vehículo con las imágenes
            alquilerGuardado = repository.save(alquilerGuardado);
        }
        
        return alquilerGuardado;
    }
    
    public List<AlquilerVehiculo> obtenerTodos() {
        return repository.findAll();
    }
    
    public AlquilerVehiculo obtenerPorCodigoUnico(String codigoUnico) {
        return repository.findByCodigoUnico(codigoUnico);
    }
    
    @Transactional
    public void eliminarAlquilerVehiculo(Long id) {
        repository.deleteById(id);
    }
}