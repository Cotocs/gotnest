package com.gotnest.buyer.controller;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.service.MockGeoPropertiesService;


@RestController
public class PropertiesGeoSearchController {

    private final MockGeoPropertiesService mockService;

    public PropertiesGeoSearchController(MockGeoPropertiesService mockService) {
        this.mockService = mockService;
    }

    @GetMapping(path = "/bff/v1/properties/geo-search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> geoSearch(@RequestParam(name = "bbox") String bbox) {
        if (!StringUtils.hasText(bbox)) {
            return Mono.error(new IllegalArgumentException("bbox is required"));
        }
        return mockService.mockFeatureCollection(bbox);
    }

    @PostMapping(path = "/bff/v1/properties/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchProperties(@RequestBody PropertySearchFilterDTO filter) {
        return mockService.filterProperties(filter);
    }
}
