package com.eduard.registro.turismo.app.service;

import com.eduard.registro.turismo.app.dto.SitioTuristicoDTO;
import com.eduard.registro.turismo.app.model.ImagenSitio;
import com.eduard.registro.turismo.app.model.SitioTuristicos;
import com.eduard.registro.turismo.app.repository.SitioTuristicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;

@Service
public class SitioTuristicoService {
    
    @Autowired
    private SitioTuristicoRepository repository;
    
    @Transactional
    public SitioTuristicos crearSitioTuristico(@Valid SitioTuristicoDTO sitioTuristicoDTO) {
        SitioTuristicos sitioTuristico = sitioTuristicoDTO.getSitioTuristico();
        List<ImagenSitio> imagenes = sitioTuristicoDTO.getImagenes();
        
        // Primero guardamos el sitio turístico sin imágenes para obtener el ID
        SitioTuristicos sitioGuardado = repository.save(sitioTuristico);

        // Luego establecemos la relación bidireccional y guardamos las imágenes
        if (imagenes != null && !imagenes.isEmpty()) {
            for (ImagenSitio imagen : imagenes) {
                imagen.setSitioTuristico(sitioGuardado);
            }
            sitioGuardado.setImagenes(imagenes);
            // Volvemos a guardar el sitio con las imágenes
            sitioGuardado = repository.save(sitioGuardado);
        }
        
        return sitioGuardado;
    }
    
    public List<SitioTuristicos> obtenerTodos() {
        return repository.findAll();
    }
    
    public SitioTuristicos obtenerPorCodigoUnico(String codigoUnico) {
        return repository.findByCodigoUnico(codigoUnico);
    }
    
    @Transactional
    public void eliminarSitioTuristico(String codigoUnico) {
        repository.deleteById(codigoUnico);
    }
}