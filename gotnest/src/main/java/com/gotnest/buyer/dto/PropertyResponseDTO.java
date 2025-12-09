package com.gotnest.buyer.dto;

import lombok.Getter;

import java.util.List;


@Getter
public class PropertyResponseDTO {

    private String pinColor;
    private String priceShort;
    private List<Double> coordinates;
    private String id;

    public PropertyResponseDTO(String pinColor, String priceShort, List<Double> coordinates, String id) {
        this.pinColor = pinColor;
        this.priceShort = priceShort;
        this.coordinates = coordinates;
        this.id = id;
    }

    public void setPinColor(String pinColor) {
        this.pinColor = pinColor;
    }

    public void setPriceShort(String priceShort) {
        this.priceShort = priceShort;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public void setId(String id) {
        this.id = id;
    }
}
