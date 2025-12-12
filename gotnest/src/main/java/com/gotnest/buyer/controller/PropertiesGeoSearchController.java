package com.gotnest.buyer.controller;

import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.service.BboxValidationService;
import com.gotnest.buyer.service.MockGeoPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
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
    public Mono<Map<String, Object>> geoSearch(@RequestParam(name = "bbox", required = false) String bbox) {
        if (bbox != null && !bbox.isBlank()) {
            bboxValidationService.validateOrThrowIfOutsideHouston(bbox);
        }
        return mockService.simplifiedFeatureCollection();
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> searchProperties(
            @RequestHeader(name = "X-Address", required = false) String address,
            @RequestBody PropertySearchFilterDTO filter) {
        return mockService.filterPropertiesByCriteria(filter, address)
                .flatMap(result -> {
                    List<Map<String, Object>> features = getFeatures(result);

                    List<String> addresses = features.stream()
                            .map(f -> {
                                Map<String, Object> props = getProperties(f);
                                Map<String, Object> cardData = (Map<String, Object>) props.get("cardData");
                                return cardData != null ? (String) cardData.get("address") : null;
                            })
                            .filter(addr -> addr != null && !addr.isBlank())
                            .distinct()
                            .toList();

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

                    Map<String, Object> response = new HashMap<>();
                    response.put("features", simpleFeatures);
                    response.put("addresses", addresses);
                    response.put("total", simpleFeatures.size());

                    return Mono.just(response);
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