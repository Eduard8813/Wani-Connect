package com.eduard.registro.turismo.app.service;

import com.eduard.registro.turismo.app.dto.RestauranteDTO;
import com.eduard.registro.turismo.app.model.ImagenRestaurante;
import com.eduard.registro.turismo.app.model.Restaurante;
import com.eduard.registro.turismo.app.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;

@Service
public class RestauranteService {
    
    @Autowired
    private RestauranteRepository repository;
    
    @Transactional
    public Restaurante crearRestaurante(@Valid RestauranteDTO restauranteDTO) {
        Restaurante restaurante = restauranteDTO.getRestaurante();
        List<ImagenRestaurante> imagenes = restauranteDTO.getImagenes();
        
        // Primero guardamos el restaurante sin imágenes para obtener el ID
        Restaurante restauranteGuardado = repository.save(restaurante);
        
        // Luego establecemos la relación bidireccional y guardamos las imágenes
        if (imagenes != null && !imagenes.isEmpty()) {
            for (ImagenRestaurante imagen : imagenes) {
                imagen.setRestaurante(restauranteGuardado);
            }
            restauranteGuardado.setImagenes(imagenes);
            // Volvemos a guardar el restaurante con las imágenes
            restauranteGuardado = repository.save(restauranteGuardado);
        }
        
        return restauranteGuardado;
    }
    
    public List<Restaurante> obtenerTodos() {
        return repository.findAll();
    }
    
    public Restaurante obtenerPorCodigoUnico(String codigoUnico) {
        return repository.findByCodigoUnico(codigoUnico);
    }
    
    @Transactional
    public void eliminarRestaurante(Long id) {
        repository.deleteById(id);
    }
}