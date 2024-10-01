package az.edu.muradsproject.gelolocation.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@RestController
@RequestMapping("/api")
public class LocationController {

    private final GeoApiContext geoApiContext;

    public LocationController() {
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyCt-YiA9TJ2hNVuVWbytkAcbqEMga-nGLs") // Replace with your API key
                .build();
    }

    // Request body class for location
    public static class LocationRequest {
        @JsonProperty("location")
        private String location;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

    // API endpoint to get coordinates
    @PostMapping("/get-coordinates")
    public CoordinatesResponse getCoordinates(@RequestBody LocationRequest request) {
        String location = request.getLocation();
        CoordinatesResponse response = new CoordinatesResponse();

        try {
            GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, location).await();
            if (results.length > 0) {
                response.setLatitude(results[0].geometry.location.lat);
                response.setLongitude(results[0].geometry.location.lng);
                response.setAddress(results[0].formattedAddress);
            } else {
                response.setError("No results found for this location.");
            }
        } catch (Exception e) {
            response.setError("Error retrieving location: " + e.getMessage());
        }

        return response;
    }

    // Response class for coordinates
    public static class CoordinatesResponse {
        private double latitude;
        private double longitude;
        private String address;
        private String error;

        // Getters and Setters
        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
