package com.eduard.registro.turismo.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.eduard.registro.turismo.app.model.SitioTuristico;
import com.eduard.registro.turismo.app.repository.SitioTuristicoRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final SitioTuristicoRepository sitioRepo;
    private final WebClient.Builder webClientBuilder; // Inyectar WebClient para consumir OpenRouteService

    // Ejemplo básico de optimización de ruta usando OpenRouteService
    public Map<String, Object> optimizarRuta(Map<String, Object> request) {
        // request: { origen: "lat,lng", destinos: ["lat1,lng1", ...] }
        // Aquí se haría la llamada real a OpenRouteService, pero lo simulamos:
        Map<String, Object> simulatedResult = new HashMap<>();
        simulatedResult.put("ruta", Arrays.asList(request.get("origen"), request.get("destinos")));
        simulatedResult.put("distancia", 12500); // metros simulados
        simulatedResult.put("duracion", 1800); // segundos simulados
        return simulatedResult;
    }

    // Obtener ruta en GeoJSON para visualizar en mapa
    public Map<String, Object> obtenerRutaEnGeoJson(Long sitioId) {
        SitioTuristico sitio = sitioRepo.findById(sitioId).orElseThrow();
        // Simulación GeoJSON:
        Map<String, Object> geojson = new HashMap<>();
        geojson.put("type", "Feature");
        geojson.put("geometry", Map.of(
            "type", "Point",
            "coordinates", Arrays.stream(sitio.getUbicacion().split(",")).map(Double::parseDouble).toList()
        ));
        geojson.put("properties", Map.of("nombre", sitio.getNombre()));
        return geojson;
    }

    // Comparar rutas históricas del usuario (simulado)
    public List<Map<String, Object>> compararHistorico(Long userId) {
        // Simulación: devolver rutas dummy
        Map<String, Object> ruta1 = Map.of("ruta", List.of("12.13,-86.25", "12.15,-86.24"), "duracion", 1200);
        Map<String, Object> ruta2 = Map.of("ruta", List.of("12.13,-86.25", "12.16,-86.21"), "duracion", 1500);
        return List.of(ruta1, ruta2);
    }

    // Guardar historial (simulado)
    public boolean guardarEnHistorial(String token, Map<String, Object> request) {
        // Aquí se validaría el token, y se guardaría la ruta en la tabla de historial, por ahora solo simulado:
        return true;
    }
}