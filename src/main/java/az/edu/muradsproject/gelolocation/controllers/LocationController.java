package az.edu.muradsproject.gelolocation.controllers;

import az.edu.muradsproject.gelolocation.helpers.GoogleMapHelper;
import az.edu.muradsproject.gelolocation.models.AddressDistance;
import az.edu.muradsproject.gelolocation.models.AddressInput;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @PostMapping("/sort")
    public List<AddressDistance> sortAddresses(@RequestBody AddressInput addressInput) {
        List<AddressDistance> addressDistances = new ArrayList<>();

        // Get user's latitude and longitude (if available)
        double userLat = addressInput.getUserLat();
        double userLon = addressInput.getUserLon();

        // If geolocation is denied or not available, use default coordinates
        if (userLat == 0 && userLon == 0) {
            // Fallback to a default location (e.g., center of the city)
            userLat = 40.4093; // Example default: Baku's latitude
            userLon = 49.8671; // Example default: Baku's longitude
        }

        // Process each Google Maps link
        addAddressDistance(addressDistances, addressInput.getAddressLink1(), userLat, userLon);
        addAddressDistance(addressDistances, addressInput.getAddressLink2(), userLat, userLon);
        addAddressDistance(addressDistances, addressInput.getAddressLink3(), userLat, userLon);

        // Sort by distance
        addressDistances.sort(Comparator.comparingDouble(AddressDistance::getDistance));

        return addressDistances;
    }

    private void addAddressDistance(List<AddressDistance> addressDistances, String googleMapsLink, double userLat, double userLon) {
        String[] coordinates = extractCoordinatesFromGoogleMapsLink(googleMapsLink);
        double clinicLat = Double.parseDouble(coordinates[0]);
        double clinicLon = Double.parseDouble(coordinates[1]);
        double distance = calculateDistance(userLat, userLon, clinicLat, clinicLon);
        addressDistances.add(new AddressDistance(googleMapsLink, distance));
    }

    public double calculateDistance(double userLat, double userLon, double clinicLat, double clinicLon) {
        final int R = 6371; // Radius of the Earth in kilometers

        // Convert degrees to radians
        double latDistance = Math.toRadians(clinicLat - userLat);
        double lonDistance = Math.toRadians(clinicLon - userLon);

        // Apply the Haversine formula
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(clinicLat)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        double distance = R * c;

        return distance;
    }


    private String[] extractCoordinatesFromGoogleMapsLink(String googleMapsLink) {
        String[] coordinatesList = new String[2];
        String[] longLat = GoogleMapHelper.getLongLatFromGoogleMapLink(googleMapsLink);
        // Assuming longLat has the format: {latitude, longitude}
        coordinatesList[0] = longLat[0];
        coordinatesList[1] = longLat[1];
        return coordinatesList;
    }
}
