package com.gotnest.buyer.service;

import com.gotnest.buyer.exception.AddressNotFoundException;
import com.gotnest.buyer.mapper.PropertyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PropertyDetailService {

    private final MockGeoPropertiesService mockGeoPropertiesService;
    private final PropertyMapper propertyMapper;

    public Mono<Map<String, Object>> getPropertyDetails(String propertyId) {
        return mockGeoPropertiesService.mockFeatureCollection()
                .map(featureCollection -> {
                    List<Map<String, Object>> features = propertyMapper.getFeatures(featureCollection);

                    Map<String, Object> targetProperty = findPropertyById(features, propertyId);
                    if (targetProperty == null) {
                        throw new AddressNotFoundException();
                    }

                    List<Map<String, Object>> nearbyProperties = findNearbyProperties(features, targetProperty, propertyId);

                    List<Map<String, Object>> results = new ArrayList<>();
                    results.add(targetProperty);
                    results.addAll(nearbyProperties);

                    List<Map<String, Object>> cleanResults = propertyMapper.toCleanResults(results);

                    Map<String, Object> response = new HashMap<>();
                    response.put("results", cleanResults);
                    response.put("total", cleanResults.size());

                    return response;
                });
    }

    private Map<String, Object> findPropertyById(List<Map<String, Object>> features, String propertyId) {
        return features.stream()
                .filter(f -> {
                    Map<String, Object> props = propertyMapper.getProperties(f);
                    return Objects.equals(props.get("id"), propertyId);
                })
                .findFirst()
                .orElse(null);
    }

    private List<Map<String, Object>> findNearbyProperties(
            List<Map<String, Object>> allFeatures,
            Map<String, Object> targetProperty,
            String targetPropertyId) {

        Map<String, Object> targetGeometry = propertyMapper.getGeometry(targetProperty);
        List<?> targetCoords = (List<?>) targetGeometry.get("coordinates");
        double targetLon = ((Number) targetCoords.get(0)).doubleValue();
        double targetLat = ((Number) targetCoords.get(1)).doubleValue();

        return allFeatures.stream()
                .filter(f -> {
                    Map<String, Object> props = propertyMapper.getProperties(f);
                    return !Objects.equals(props.get("id"), targetPropertyId);
                })
                .map(f -> {
                    Map<String, Object> geometry = propertyMapper.getGeometry(f);
                    List<?> coords = (List<?>) geometry.get("coordinates");
                    double lon = ((Number) coords.get(0)).doubleValue();
                    double lat = ((Number) coords.get(1)).doubleValue();

                    double distance = calculateDistance(targetLat, targetLon, lat, lon);

                    Map<String, Object> propertyWithDistance = new HashMap<>(f);
                    propertyWithDistance.put("_distance", distance);
                    return propertyWithDistance;
                })
                .sorted(Comparator.comparingDouble(p -> (double) p.get("_distance")))
                .limit(10)
                .peek(p -> p.remove("_distance"))
                .toList();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}

