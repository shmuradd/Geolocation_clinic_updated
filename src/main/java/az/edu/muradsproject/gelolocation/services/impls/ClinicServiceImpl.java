package az.edu.muradsproject.gelolocation.services.impls;

import az.edu.muradsproject.gelolocation.controllers.LocationController;
import az.edu.muradsproject.gelolocation.dto.ClinicDto;
import az.edu.muradsproject.gelolocation.dto.ClinicSearchRequest;
import az.edu.muradsproject.gelolocation.helpers.GoogleMapHelper;
import az.edu.muradsproject.gelolocation.models.Clinic;
import az.edu.muradsproject.gelolocation.models.Doctor;
import az.edu.muradsproject.gelolocation.repositories.ClinicRepository;
import az.edu.muradsproject.gelolocation.repositories.DoctorRepository;
import az.edu.muradsproject.gelolocation.services.ClinicService;
import az.edu.muradsproject.gelolocation.services.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    ClinicRepository clinicRepository;
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private LocationController locationController; // Inject the LocationController


    @Override
    public List<ClinicDto> getClinicsByDoctorId(Long doctorId) {
        // Fetch the doctor by ID
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Get the list of clinics associated with the doctor
        List<Clinic> clinics = doctor.getClinics();

        // Convert the Clinic entities to ClinicDto objects
        return clinics.stream()
                .map(this::convertToDto) // Assuming you have a method to convert Clinic to ClinicDto
                .collect(Collectors.toList());
    }

    // Method to convert Clinic to ClinicDto
    @Override
    public ClinicDto convertToDto(Clinic clinic) {
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.setName(clinic.getName());
        clinicDto.setAddress(clinic.getAddress());
        clinicDto.setContactDetails(clinic.getContactDetails());
        clinicDto.setCity(clinic.getCity());
        return clinicDto;
    }

    @Override
    public String getCityNameByClinicId(Long clinicId) {
        return clinicRepository.findById(clinicId)
                .map(Clinic::getCity)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));
    }

    //Find clinics by Doctor Name and speciality
    public List<ClinicDto> searchByDoctor(String doctorName, String speciality) {
        List<Doctor> doctors = doctorRepository.findByNameAndSpecialty(doctorName, speciality);
        List<ClinicDto> clinics = new ArrayList<>();
        for (Doctor doctor : doctors) {
            List<Clinic> doctorClinics = doctor.getClinics();
            for (Clinic clinic : doctorClinics) {
                ClinicDto clinicDto = modelMapper.map(clinic, ClinicDto.class);
                clinics.add(clinicDto);
            }
        }
        return clinics;
    }

    @Override
    public List<ClinicDto> searchClinicsByDoctorAndCity(ClinicSearchRequest searchRequest) {
        // Step 1: Find doctors matching the given name and specialization
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyContainingIgnoreCase(
                searchRequest.getDoctorName(),
                searchRequest.getSpecialization()
        );

        // Step 2: Find clinics associated with these doctors that match the given clinic name
        Set<Clinic> clinics = new HashSet<>();
        for (Doctor doctor : doctors) {
            clinics.addAll(doctor.getClinics().stream()
                    .filter(clinic -> searchRequest.getClinicName() == null || clinic.getName().equalsIgnoreCase(searchRequest.getClinicName()))
                    .collect(Collectors.toList()));
        }

        // Step 3: Convert Clinic entities to ClinicDto
        List<ClinicDto> clinicDtos = clinics.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // Step 4: Extract coordinates for the provided location
        LocationController.LocationRequest request = new LocationController.LocationRequest();
        request.setLocation(searchRequest.getLocation());

        LocationController.CoordinatesResponse coordinatesResponse = locationController.getCoordinates(request);

        double userLat = coordinatesResponse != null ? coordinatesResponse.getLatitude() : 0;
        double userLon = coordinatesResponse != null ? coordinatesResponse.getLongitude() : 0;

        // Fallback to default coordinates for Baku if location is not found or invalid
        if (coordinatesResponse == null || (userLat == 0 && userLon == 0)) {
            userLat = 40.40982397041654; // Default latitude
            userLon = 49.87154756154538; // Default longitude
        }

        // Step 5: Calculate distance for each clinic and set the distance value in the DTO
        for (ClinicDto clinicDto : clinicDtos) {
            String googleMapsLink = clinicDto.getAddress();
            String[] coordinates = extractCoordinatesFromGoogleMapsLink(googleMapsLink);
            if (coordinates != null && coordinates.length == 2) {
                try {
                    double clinicLat = Double.parseDouble(coordinates[0]);
                    double clinicLon = Double.parseDouble(coordinates[1]);
                    double distance = calculateDistance(userLat, userLon, clinicLat, clinicLon);
                    clinicDto.setDistance(distance);
                } catch (NumberFormatException e) {
                    // Log error or handle incorrect format gracefully
                }
            }
        }

        // Step 6: Sort clinics by distance
        clinicDtos.sort(Comparator.comparingDouble(ClinicDto::getDistance));

        return clinicDtos;
    }


    @Override
    public List<ClinicDto> getClinics() {
        List<Clinic> clinics = clinicRepository.findAll();
        return clinics.stream()
                .map(clinic -> convertToDto(clinic))
                .collect(Collectors.toList());
    }

    @Override
    public String[] extractCoordinatesFromGoogleMapsLink(String googleMapsLink) {
        String[] coordinatesList= new String[2];
        String[] longLat = GoogleMapHelper.getLongLatFromGoogleMapLink(googleMapsLink);
        // Assuming longLat has the format: {latitude, longitude}
        coordinatesList[0] = longLat[0];
        coordinatesList[1] = longLat[1];
        return coordinatesList;
    }

    @Override
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

}
