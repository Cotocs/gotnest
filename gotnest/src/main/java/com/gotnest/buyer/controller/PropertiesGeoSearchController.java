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

    /**
     * GET /bff/v1/properties/geo-search
     * Returns simplified property features for map display.
     *
     * @param bbox Optional bounding box parameter (validated for Houston area)
     * @return Simplified feature collection
     */
    @GetMapping(path = "/geo-search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> geoSearch(@RequestParam(name = "bbox", required = false) String bbox) {
        if (bbox != null && !bbox.isBlank()) {
            bboxValidationService.validateOrThrowIfOutsideHouston(bbox);
        }
        return propertySearchService.getSimplifiedFeatures();
    }

    /**
     * POST /bff/v1/properties/search
     * Searches properties based on filter criteria and optional address.
     *
     * @param address Optional address header for location-based search
     * @param filter  Property filter criteria (price, bedrooms, etc.)
     * @return Search results with features, addresses, and total count
     */
    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchProperties(
            @RequestHeader(name = "X-Address", required = false) String address,
            @RequestBody PropertySearchFilterDTO filter) {
        return propertySearchService.searchProperties(filter, address);
    }

    /**
     * GET /bff/v1/properties/details/{id}
     * Retrieves detailed information for a specific property including nearby properties.
     *
     * @param id Property identifier
     * @return Property details and nearby properties
     */
    @GetMapping(path = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getPropertyDetails(@PathVariable("id") String id) {
        return propertyDetailService.getPropertyDetails(id);
    }
}