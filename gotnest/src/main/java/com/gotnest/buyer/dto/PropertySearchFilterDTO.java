package com.gotnest.buyer.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

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

}
