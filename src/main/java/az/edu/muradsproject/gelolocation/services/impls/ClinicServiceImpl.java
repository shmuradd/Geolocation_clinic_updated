package az.edu.muradsproject.gelolocation.services.impls;

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    ClinicRepository clinicRepository;
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    ModelMapper modelMapper;

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

    public List<ClinicDto> searchClinicsByDoctorAndCity(ClinicSearchRequest searchRequest) {
        // Logic to search clinics based on the doctorName, specialty, and city from the request
        List<Clinic> clinics = clinicRepository.findClinicsByDoctorAndCity(
                searchRequest.getDoctorName(),
                searchRequest.getSpecialty(),
                searchRequest.getCity()
        );

        // Convert Clinic entities to ClinicDto
        return clinics.stream()
                .map(clinic -> convertToDto(clinic))
                .collect(Collectors.toList());
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
