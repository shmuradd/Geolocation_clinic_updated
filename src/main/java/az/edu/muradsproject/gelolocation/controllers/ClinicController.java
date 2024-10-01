package az.edu.muradsproject.gelolocation.controllers;

import az.edu.muradsproject.gelolocation.dto.ClinicDto;
import az.edu.muradsproject.gelolocation.dto.ClinicSearchRequest;
import az.edu.muradsproject.gelolocation.services.ClinicService;
import az.edu.muradsproject.gelolocation.services.DoctorService;
import az.edu.muradsproject.gelolocation.services.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private GeocodingService geocodingService;
    @Autowired
    private LocationController locationController; // Inject the LocationController


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



    @PostMapping("/search")
    public ResponseEntity<List<ClinicDto>> searchDoctors(@RequestBody ClinicSearchRequest searchRequest) {
        // Call the service to perform the search and sort clinics by distance
        List<ClinicDto> clinics = clinicService.searchClinicsByDoctorAndCity(searchRequest);

        // Return the sorted list of clinics
        return ResponseEntity.ok(clinics);
    }







}
