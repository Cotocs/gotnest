package com.gotnest.buyer.controller;

import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.service.BboxValidationService;
import com.gotnest.buyer.service.PropertyDetailService;
import com.gotnest.buyer.service.PropertySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/bff/v1/properties")
@RequiredArgsConstructor
public class PropertiesGeoSearchController {

    private final PropertySearchService propertySearchService;
    private final PropertyDetailService propertyDetailService;
    private final BboxValidationService bboxValidationService;

    @GetMapping(path = "/geo-search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> geoSearch(@RequestParam(name = "bbox", required = false) String bbox) {
        if (bbox != null && !bbox.isBlank()) {
            bboxValidationService.validateOrThrowIfOutsideHouston(bbox);
        }
        return propertySearchService.getSimplifiedFeatures();
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchProperties(
            @RequestHeader(name = "X-Address", required = false) String address,
            @RequestBody PropertySearchFilterDTO filter) {
        return propertySearchService.searchProperties(filter, address);
    }

    @GetMapping(path = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getPropertyDetails(@PathVariable("id") String id) {
        return propertyDetailService.getPropertyDetails(id);
    }
}