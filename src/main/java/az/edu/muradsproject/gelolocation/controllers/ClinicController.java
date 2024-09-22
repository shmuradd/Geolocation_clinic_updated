package az.edu.muradsproject.gelolocation.controllers;

import az.edu.muradsproject.gelolocation.dto.ClinicDto;
import az.edu.muradsproject.gelolocation.dto.ClinicSearchRequest;
import az.edu.muradsproject.gelolocation.models.AddressDistance;
import az.edu.muradsproject.gelolocation.models.AddressInput;
import az.edu.muradsproject.gelolocation.services.ClinicService;
import az.edu.muradsproject.gelolocation.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/clinics")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private DoctorService doctorService;


    @PostMapping("/search")
    public ResponseEntity<List<ClinicDto>> searchClinics(@RequestBody ClinicSearchRequest searchRequest) {
        List<ClinicDto> clinics = clinicService.searchClinicsByDoctorAndCity(searchRequest);
        return ResponseEntity.ok(clinics);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<ClinicDto>> getAllClinics() {
        List<ClinicDto> clinics = clinicService.getClinics();
        return ResponseEntity.ok(clinics);
    }

    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<List<ClinicDto>> getClinicsByDoctorId(@PathVariable Long doctorId) {
        List<ClinicDto> clinics = clinicService.getClinicsByDoctorId(doctorId);
        return ResponseEntity.ok(clinics);
    }

    @PostMapping("/sort")
    public List<ClinicDto> sortClinicsByDistance(@RequestBody AddressInput addressInput) {
        // Get the list of clinics (assuming this is how you're retrieving clinics)
        List<ClinicDto> clinics = clinicService.getClinics();

        // Get user's latitude and longitude (if available)
        double userLat = addressInput.getUserLat();
        double userLon = addressInput.getUserLon();

        // If geolocation is denied or not available, use default coordinates
        if (userLat == 0 && userLon == 0) {
            // Fallback to a default location (e.g., center of the city)
            userLat = 40.40982397041654; // Example default: Baku's latitude
            userLon = 49.87154756154538; // Example default: Baku's longitude
        }
        // Process each ClinicDto's address (Google Maps link) and calculate the distance
        for (ClinicDto clinic : clinics) {
            String googleMapsLink = clinic.getAddress(); // Assuming address contains the Google Maps link
            String[] coordinates = clinicService.extractCoordinatesFromGoogleMapsLink(googleMapsLink);
            if (coordinates != null) {
                double clinicLat = Double.parseDouble(coordinates[0]);
                double clinicLon = Double.parseDouble(coordinates[1]);

                // Calculate distance using a helper method (e.g., Haversine formula)
                double distance =clinicService.calculateDistance(userLat, userLon, clinicLat, clinicLon);

                // Set the distance in ClinicDto
                clinic.setDistance(distance);
            }
        }

        // Sort clinics by distance
        clinics.sort(Comparator.comparingDouble(ClinicDto::getDistance));

        return clinics;
    }
}
