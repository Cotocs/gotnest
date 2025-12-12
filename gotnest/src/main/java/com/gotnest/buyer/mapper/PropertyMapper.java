package com.gotnest.buyer.mapper;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PropertyMapper {

    public List<Map<String, Object>> toSimplifiedFeatures(List<Map<String, Object>> features) {
        return features.stream()
                .map(this::toSimplifiedFeature)
                .collect(Collectors.toList());
    }

    public Map<String, Object> toSimplifiedFeature(Map<String, Object> feature) {
        Map<String, Object> props = getProperties(feature);
        Map<String, Object> geometry = getGeometry(feature);

        Map<String, Object> simple = new HashMap<>();
        simple.put("id", props.get("id"));
        simple.put("pinColor", props.get("pinColor"));
        simple.put("priceShort", props.get("priceShort"));
        simple.put("coordinates", geometry.get("coordinates"));

        return simple;
    }

    public List<String> extractAddresses(List<Map<String, Object>> features) {
        return features.stream()
                .map(f -> {
                    Map<String, Object> props = getProperties(f);
                    Map<String, Object> cardData = (Map<String, Object>) props.get("cardData");
                    return cardData != null ? (String) cardData.get("address") : null;
                })
                .filter(addr -> addr != null && !addr.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> toCleanResults(List<Map<String, Object>> features) {
        return features.stream()
                .map(this::toCleanResult)
                .collect(Collectors.toList());
    }

    private Map<String, Object> toCleanResult(Map<String, Object> feature) {
        Map<String, Object> props = getProperties(feature);
        Map<String, Object> cleanResponse = new HashMap<>();

        cleanResponse.put("geometry", feature.get("geometry"));

        Map<String, Object> cleanProperties = new HashMap<>();
        cleanProperties.put("id", props.get("id"));
        cleanProperties.put("cardData", props.get("cardData"));

        cleanResponse.put("properties", cleanProperties);
        return cleanResponse;
    }

    public Map<String, Object> getProperties(Map<String, Object> feature) {
        Object p = feature.get("properties");
        if (p instanceof Map) {
            return (Map<String, Object>) p;
        }
        return Map.of();
    }

    public Map<String, Object> getGeometry(Map<String, Object> feature) {
        Object g = feature.get("geometry");
        if (g instanceof Map) {
            return (Map<String, Object>) g;
        }
        return Map.of();
    }

    public List<Map<String, Object>> getFeatures(Map<String, Object> featureCollection) {
        Object features = featureCollection.get("features");
        if (features instanceof List) {
            return (List<Map<String, Object>>) features;
        }
        return Collections.emptyList();
    }
}

