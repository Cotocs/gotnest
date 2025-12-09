package com.gotnest.buyer.controller;

import com.gotnest.buyer.service.BboxValidationService;
import com.gotnest.buyer.service.MockGeoPropertiesService;
import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

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
        // Chama o método de filtro do serviço, que já retorna o formato simplificado
        return mockService.filterPropertiesByCriteria(filter);
    }

    @GetMapping(path = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getPropertyById(@PathVariable("id") String id) {
        return mockService.getPropertyById(id);
    }
}