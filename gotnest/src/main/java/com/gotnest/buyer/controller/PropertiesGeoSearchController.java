package com.gotnest.buyer.controller;

import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.service.BboxValidationService;
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
    private final BboxValidationService bboxValidationService;

    @Autowired
    public PropertiesGeoSearchController(MockGeoPropertiesService mockService,
                                         BboxValidationService bboxValidationService) {
        this.mockService = mockService;
        this.bboxValidationService = bboxValidationService;
    }

    @GetMapping(path = "/geo-search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> geoSearch(@RequestParam(name = "bbox") String bbox) {
        bboxValidationService.validateOrThrowIfOutsideHouston(bbox);
        return mockService.mockFeatureCollection();
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchProperties(@RequestBody(required = false) PropertySearchFilterDTO filter,
                                                      @RequestParam(name = "q", required = false) String query,
                                                      @RequestParam(name = "bbox", required = false) String bbox) {
        if (filter == null) filter = new PropertySearchFilterDTO();
        if (bbox != null && !bbox.isBlank()) {
            bboxValidationService.validateOrThrowIfOutsideHouston(bbox);
            filter.setBbox(bbox);
        }
        if (query != null && !query.isBlank()) {
            return mockService.searchByAddressWithFilters(query, filter);
        }
        return mockService.filterProperties(filter);
    }

    @GetMapping(path = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getPropertyById(@PathVariable("id") String id) {
        return mockService.getPropertyById(id);
    }
}
