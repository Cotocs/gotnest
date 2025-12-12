package com.gotnest.buyer.service;

import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.mapper.PropertyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyFilterService {

    private final PropertyMapper propertyMapper;

    public List<Map<String, Object>> filterByCriteria(List<Map<String, Object>> features, PropertySearchFilterDTO filter) {
        return features.stream()
                .filter(f -> matchesFilter(f, filter))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> filterByAddress(List<Map<String, Object>> features, String searchTerm) {
        String normalizedSearch = searchTerm.trim().toLowerCase();
        boolean isNumericSearch = normalizedSearch.matches("^\\d+.*");

        return features.stream()
                .filter(f -> {
                    Map<String, Object> props = propertyMapper.getProperties(f);
                    Map<String, Object> card = getCardData(props);
                    String propertyAddress = (String) card.get("address");

                    if (propertyAddress == null) return false;

                    String normalizedAddress = propertyAddress.toLowerCase();

                    if (isNumericSearch) {
                        return normalizedAddress.matches("^" + Pattern.quote(normalizedSearch) + ".*");
                    } else {
                        return normalizedAddress.contains(normalizedSearch);
                    }
                })
                .collect(Collectors.toList());
    }

    private boolean matchesFilter(Map<String, Object> feature, PropertySearchFilterDTO filter) {
        Map<String, Object> props = propertyMapper.getProperties(feature);
        Map<String, Object> card = getCardData(props);

        String details = (String) card.get("area");
        int propertyBedrooms = extractBedroomsFromDetails(details);
        int propertyBathrooms = extractBathroomsFromDetails(details);
        int propertyArea = extractAreaFromDetails(details);
        BigDecimal propertyPrice = parsePriceFromObject(card.get("fullPrice"));

        return matchesPriceRange(propertyPrice, filter)
                && matchesMonthlyPayment(propertyPrice, filter)
                && matchesBedrooms(propertyBedrooms, filter)
                && matchesBathrooms(propertyBathrooms, filter)
                && matchesLotSize(propertyArea, filter);
    }

    private boolean matchesPriceRange(BigDecimal price, PropertySearchFilterDTO filter) {
        boolean minPriceMatch = Optional.ofNullable(filter.getMinPrice())
                .map(min -> price.compareTo(min) >= 0)
                .orElse(true);

        boolean maxPriceMatch = Optional.ofNullable(filter.getMaxPrice())
                .map(max -> price.compareTo(max) <= 0)
                .orElse(true);

        return minPriceMatch && maxPriceMatch;
    }

    private boolean matchesMonthlyPayment(BigDecimal price, PropertySearchFilterDTO filter) {
        return Optional.ofNullable(filter.getMaxMonthlyPayment())
                .map(max -> price.multiply(BigDecimal.valueOf(0.005)).compareTo(max) <= 0)
                .orElse(true);
    }

    private boolean matchesBedrooms(int propertyBedrooms, PropertySearchFilterDTO filter) {
        if (filter.getBedrooms() == null) return true;
        if (filter.getBedrooms() >= 6) {
            return propertyBedrooms >= 6;
        }
        return propertyBedrooms >= filter.getBedrooms();
    }

    private boolean matchesBathrooms(int propertyBathrooms, PropertySearchFilterDTO filter) {
        if (filter.getBathrooms() == null) return true;
        if (filter.getBathrooms() >= 6) {
            return propertyBathrooms >= 6;
        }
        return propertyBathrooms >= filter.getBathrooms();
    }

    private boolean matchesLotSize(int propertyArea, PropertySearchFilterDTO filter) {
        boolean minMatch = Optional.ofNullable(filter.getMinLotSize())
                .map(min -> propertyArea >= min)
                .orElse(true);

        boolean maxMatch = Optional.ofNullable(filter.getMaxLotSize())
                .map(max -> propertyArea <= max)
                .orElse(true);

        return minMatch && maxMatch;
    }

    private Map<String, Object> getCardData(Map<String, Object> props) {
        Object obj = props.get("cardData");
        if (obj instanceof Map<?, ?>) {
            return (Map<String, Object>) obj;
        }
        return Map.of();
    }

    private int extractBedroomsFromDetails(String details) {
        if (details == null) return 0;
        Pattern pattern = Pattern.compile("(\\d+)bd");
        Matcher matcher = pattern.matcher(details);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private int extractBathroomsFromDetails(String details) {
        if (details == null) return 0;
        Pattern pattern = Pattern.compile("(\\d+)ba");
        Matcher matcher = pattern.matcher(details);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private int extractAreaFromDetails(String details) {
        if (details == null) return 0;
        Pattern pattern = Pattern.compile("([\\d,]+)\\s*sqft");
        Matcher matcher = pattern.matcher(details);
        if (matcher.find()) {
            String areaStr = matcher.group(1).replaceAll(",", "");
            try {
                return Integer.parseInt(areaStr);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private BigDecimal parsePriceFromObject(Object priceObj) {
        if (priceObj == null) return BigDecimal.ZERO;
        String str = priceObj.toString().replaceAll("[^0-9.]", "");
        if (str.isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(str.replaceAll(",", ""));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}

