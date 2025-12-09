package com.gotnest.buyer.dto;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
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
    private String subdivisions;
    private String homeType;
    private Integer yearBuilt;

}
