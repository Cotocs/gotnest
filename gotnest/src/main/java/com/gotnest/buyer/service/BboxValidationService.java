package com.gotnest.buyer.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class BboxValidationService {

    private static final double H_MIN_LON = -95.909;
    private static final double H_MIN_LAT = 29.383;
    private static final double H_MAX_LON = -95.014;
    private static final double H_MAX_LAT = 30.154;

    public void validateOrThrowIfOutsideHouston(String bbox) {
        double[] box = parseBboxOrThrow(bbox);
        double minLon = box[0];
        double minLat = box[1];
        double maxLon = box[2];
        double maxLat = box[3];

        boolean inside = minLon >= H_MIN_LON && maxLon <= H_MAX_LON &&
                minLat >= H_MIN_LAT && maxLat <= H_MAX_LAT;
        if (!inside) {
            throw unsupportedRegion();
        }
    }

    private double[] parseBboxOrThrow(String bbox) {
        try {
            String[] parts = bbox.split(",");
            if (parts.length != 4) {
                throw unsupportedRegion();
            }
            double minLon = Double.parseDouble(parts[0]);
            double minLat = Double.parseDouble(parts[1]);
            double maxLon = Double.parseDouble(parts[2]);
            double maxLat = Double.parseDouble(parts[3]);
            if (minLon > maxLon || minLat > maxLat) {
                throw unsupportedRegion();
            }
            if (minLon < -180 || maxLon > 180 || minLat < -90 || maxLat > 90) {
                throw unsupportedRegion();
            }
            return new double[]{minLon, minLat, maxLon, maxLat};
        } catch (NumberFormatException ex) {
            throw unsupportedRegion();
        }
    }

    private ResponseStatusException unsupportedRegion() {
        return new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "We don't have this location available at the moment.");
    }
}

