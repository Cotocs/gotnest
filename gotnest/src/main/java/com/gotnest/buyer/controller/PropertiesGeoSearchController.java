package com.gotnest.buyer.controller;

import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.service.MockGeoPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Controller responsável pelos endpoints de busca e consulta de propriedades imobiliárias.
 * Segue princípios SOLID e Clean Code para máxima clareza e manutenibilidade.
 */
@RestController
@RequestMapping("/bff/v1/properties")
public class PropertiesGeoSearchController {

    private final MockGeoPropertiesService mockService;

    /**
     * Injeta o serviço de propriedades mockadas.
     * @param mockService serviço de propriedades
     */
    @Autowired
    public PropertiesGeoSearchController(MockGeoPropertiesService mockService) {
        this.mockService = mockService;
    }

    /**
     * Busca propriedades dentro de uma área geográfica (bounding box).
     * @param bbox bounding box no formato esperado
     * @return FeatureCollection de propriedades
     */
    @GetMapping(path = "/geo-search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> geoSearch(@RequestParam(name = "bbox") String bbox) {
        return mockService.mockFeatureCollection(bbox);
    }

    /**
     * Busca propriedades com filtros avançados.
     * @param filter filtros de busca
     * @return FeatureCollection de propriedades filtradas
     */
    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchProperties(@RequestBody PropertySearchFilterDTO filter) {
        return mockService.filterProperties(filter);
    }

    /**
     * Busca propriedades por endereço (query string).
     * @param query termo de busca do endereço
     * @return FeatureCollection de propriedades encontradas
     */
    @GetMapping(path = "/search/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchByAddress(@RequestParam(name = "q") String query) {
        return mockService.searchByAddress(query);
    }

    /**
     * Busca detalhes de uma propriedade pelo id.
     * @param id identificador da propriedade
     * @return Detalhes da propriedade
     */
    @GetMapping(path = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getPropertyById(@PathVariable("id") String id) {
        return mockService.getPropertyById(id);
    }
}

// Controller de rotas legadas para compatibilidade
@RestController
class LegacyPropertiesController {
    private final MockGeoPropertiesService mockService;

    @Autowired
    public LegacyPropertiesController(MockGeoPropertiesService mockService) {
        this.mockService = mockService;
    }

    @GetMapping(path = "/api/search/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchByAddress(@RequestParam(name = "q") String query) {
        return mockService.searchByAddress(query);
    }

    @GetMapping(path = "/api/properties/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getPropertyById(@PathVariable("id") String id) {
        return mockService.getPropertyById(id);
    }
}
