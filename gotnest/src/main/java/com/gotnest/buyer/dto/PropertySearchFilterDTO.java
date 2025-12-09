package com.gotnest.buyer.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class PropertySearchFilterDTO {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal maxMonthlyPayment;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer minLotSize;
    private Integer maxLotSize;
    private String bbox;
    private String address;

    public static PropertySearchFilterDTO fromFormattedString(String formattedString) {
        PropertySearchFilterDTO filter = new PropertySearchFilterDTO();
        Pattern pattern = Pattern.compile("(\\d+)bd\\s*\\|\\s*(\\d+)ba\\s*\\|\\s*(\\d+,?\\d*) sqft");
        Matcher matcher = pattern.matcher(formattedString);

        if (matcher.find()) {
            filter.setBedrooms(Integer.parseInt(matcher.group(1)));
            filter.setBathrooms(Integer.parseInt(matcher.group(2)));
            filter.setMinLotSize(Integer.parseInt(matcher.group(3).replace(",", "")));
        }

        return filter;
    }
}
