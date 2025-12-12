package com.gotnest.buyer.service;

import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.exception.AddressNotFoundException;
import com.gotnest.buyer.mapper.PropertyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PropertySearchService {

    private final MockGeoPropertiesService mockGeoPropertiesService;
    private final PropertyFilterService propertyFilterService;
    private final PropertyMapper propertyMapper;

    public Mono<Map<String, Object>> searchProperties(PropertySearchFilterDTO filter, String address) {
        return mockGeoPropertiesService.mockFeatureCollection()
                .map(featureCollection -> {
                    List<Map<String, Object>> features = propertyMapper.getFeatures(featureCollection);

                    if (address != null && !address.isBlank()) {
                        features = propertyFilterService.filterByAddress(features, address);
                        if (features.isEmpty()) {
                            throw new AddressNotFoundException();
                        }
                    }

                    List<Map<String, Object>> filtered = propertyFilterService.filterByCriteria(features, filter);

                    if (filtered.isEmpty() && address != null && !address.isBlank()) {
                        throw new AddressNotFoundException();
                    }

                    return buildSearchResponse(filtered);
                });
    }

    public Mono<Map<String, Object>> getSimplifiedFeatures() {
        return mockGeoPropertiesService.mockFeatureCollection()
                .map(featureCollection -> {
                    List<Map<String, Object>> features = propertyMapper.getFeatures(featureCollection);
                    List<Map<String, Object>> simplified = propertyMapper.toSimplifiedFeatures(features);

                    return Map.of("features", simplified);
                });
    }

    private Map<String, Object> buildSearchResponse(List<Map<String, Object>> features) {
        List<Map<String, Object>> simpleFeatures = propertyMapper.toSimplifiedFeatures(features);
        List<String> addresses = propertyMapper.extractAddresses(features);

        Map<String, Object> response = new HashMap<>();
        response.put("features", simpleFeatures);
        response.put("addresses", addresses);
        response.put("total", simpleFeatures.size());

        return response;
    }
}

