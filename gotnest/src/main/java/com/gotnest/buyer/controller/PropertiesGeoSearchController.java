package com.gotnest.buyer.controller;

import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.service.MockGeoPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/bff/v1/properties")
public class PropertiesGeoSearchController {

    private final MockGeoPropertiesService mockService;

    @Autowired
    public PropertiesGeoSearchController(MockGeoPropertiesService mockService) {
        this.mockService = mockService;
    }

    @GetMapping(path = "/geo-search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> geoSearch(@RequestParam(name = "bbox") String bbox) {
        return mockService.mockFeatureCollection();
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchProperties(@RequestBody PropertySearchFilterDTO filter) {
        return mockService.filterProperties(filter);
    }

    @GetMapping(path = "/search/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchByAddress(@RequestParam(name = "q") String query) {
        return mockService.searchByAddressWithBboxAndCount(query);
    }

    @GetMapping(path = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getPropertyById(@PathVariable("id") String id) {
        return mockService.getPropertyById(id);
    }
}
