package com.eduard.registro.turismo.app.service;

import com.eduard.registro.turismo.app.dto.HospedajeDTO;
import com.eduard.registro.turismo.app.model.Hospedaje;
import com.eduard.registro.turismo.app.model.ImagenHospedaje;
import com.eduard.registro.turismo.app.repository.HospedajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;

@Service
public class HospedajeService {
    
    @Autowired
    private HospedajeRepository repository;
    
    @Transactional
    public Hospedaje crearHospedaje(@Valid HospedajeDTO hospedajeDTO) {
        Hospedaje hospedaje = hospedajeDTO.getHospedaje();
        List<ImagenHospedaje> imagenes = hospedajeDTO.getImagenes();
        
        // Primero guardamos el hospedaje sin imágenes para obtener el ID
        Hospedaje hospedajeGuardado = repository.save(hospedaje);
        
        // Luego establecemos la relación bidireccional y guardamos las imágenes
        if (imagenes != null && !imagenes.isEmpty()) {
            for (ImagenHospedaje imagen : imagenes) {
                imagen.setHospedaje(hospedajeGuardado);
            }
            hospedajeGuardado.setImagenes(imagenes);
            // Volvemos a guardar el hospedaje con las imágenes
            hospedajeGuardado = repository.save(hospedajeGuardado);
        }
        
        return hospedajeGuardado;
    }
    
    public List<Hospedaje> obtenerTodos() {
        return repository.findAll();
    }
    
    public Hospedaje obtenerPorCodigoUnico(String codigoUnico) {
        return repository.findByCodigoUnico(codigoUnico);
    }
    
    @Transactional
    public void eliminarHospedaje(Long id) {
        repository.deleteById(id);
    }
}