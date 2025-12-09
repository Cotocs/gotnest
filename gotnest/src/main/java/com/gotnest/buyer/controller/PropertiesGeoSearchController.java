package com.gotnest.buyer.controller;

import com.gotnest.buyer.service.BboxValidationService;
import com.gotnest.buyer.service.MockGeoPropertiesService;
import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.dto.PropertyResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bff/v1/properties")
public class PropertiesGeoSearchController {

    private final MockGeoPropertiesService mockService;
    private final BboxValidationService bboxValidationService;

    @Autowired
    public PropertiesGeoSearchController(MockGeoPropertiesService mockService,
                                         BboxValidationService bboxValidationService) {
        this.mockService = mockService;
        this.bboxValidationService = bboxValidationService;
    }

    /**
     * Endpoint geo-search agora retorna apenas os campos "coordinates", "priceShort", "id" e "pinColor"
     * Exemplo de resposta:
     * {
     *   "features": [
     *     { "id": "prop-123", "priceShort": "450K", "pinColor": "GREEN", "coordinates": [-95.3698, 29.7604] },
     *     ...
     *   ]
     * }
     */
    @GetMapping(path = "/geo-search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> geoSearch(@RequestParam(name = "bbox", required = false) String bbox) {
        if (bbox != null && !bbox.isBlank()) {
            bboxValidationService.validateOrThrowIfOutsideHouston(bbox);
        }
        return mockService.simplifiedFeatureCollection();
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchProperties(@RequestBody PropertySearchFilterDTO filter) {
        // Chama o método de filtro do serviço e aplica a lógica de simplificação
        return mockService.filterPropertiesByCriteria(filter)
                .flatMap(result -> {
                    List<Map<String, Object>> features = getFeatures(result);
                    List<Map<String, Object>> simpleFeatures = features.stream().map(f -> {
                        Map<String, Object> props = getProperties(f);
                        Map<String, Object> geometry = getGeometry(f);
                        Map<String, Object> simple = new HashMap<>();
                        simple.put("id", props.get("id"));
                        simple.put("pinColor", props.get("pinColor"));
                        simple.put("priceShort", props.get("priceShort"));
                        simple.put("coordinates", geometry.get("coordinates"));
                        return simple;
                    }).toList();
                    return Mono.just(Map.of("features", simpleFeatures));
                });
    }

    @GetMapping(path = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getPropertyById(@PathVariable("id") String id) {
        return mockService.getPropertyById(id);
    }

    private Map<String, Object> getProperties(Map<String, Object> feature) {
        // Extrai as propriedades do objeto "feature"
        return (Map<String, Object>) feature.get("properties");
    }

    private Map<String, Object> getGeometry(Map<String, Object> feature) {
        // Extrai a geometria do objeto "feature"
        return (Map<String, Object>) feature.get("geometry");
    }

    private List<Map<String, Object>> getFeatures(Map<String, Object> result) {
        // Extrai a lista de features do resultado
        return (List<Map<String, Object>>) result.get("features");
    }
}