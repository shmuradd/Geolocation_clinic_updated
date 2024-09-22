package az.edu.muradsproject.gelolocation.services.impls;

import az.edu.muradsproject.gelolocation.dto.ClinicDto;
import az.edu.muradsproject.gelolocation.dto.DoctorDto;
import az.edu.muradsproject.gelolocation.helpers.GoogleMapHelper;
import az.edu.muradsproject.gelolocation.models.Clinic;
import az.edu.muradsproject.gelolocation.models.Doctor;
import az.edu.muradsproject.gelolocation.payloads.ApiResponse;
import az.edu.muradsproject.gelolocation.repositories.ClinicRepository;
import az.edu.muradsproject.gelolocation.repositories.DoctorRepository;
import az.edu.muradsproject.gelolocation.services.ClinicService;
import az.edu.muradsproject.gelolocation.services.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    ClinicRepository clinicRepository;
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    ModelMapper modelMapper;

    // Implement the createDoctor method
    @Override
    public ApiResponse createDoctor(DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        doctor.setName(doctorDto.getName());
        doctor.setSpecialty(doctorDto.getSpecialty());
        doctor.setPhotoUrl(doctorDto.getPhotoUrl());
        doctor.setQualifications(doctorDto.getQualifications());
        doctor.setExperience(doctorDto.getExperience());
        doctor.setActive(doctorDto.isActive());



        doctorRepository.save(doctor);
        return null;

    }
    // Method to assign a clinic to an existing doctor
    @Override
    public ApiResponse assignClinicToDoctor(Long doctorId, Long clinicId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        Clinic clinic = clinicRepository.findById(clinicId).orElseThrow();
        doctor.addClinic(clinicRepository.findById(clinicId).orElseThrow());
        doctorRepository.saveAndFlush(doctor);
        return new ApiResponse(true, "Clinic assigned to doctor successfully");
    }


    @Override
    public List<String> printClinicsWithCoordinates(List<ClinicDto> clinics) {
        List<String> coordinatesList = new ArrayList<>();

        for (ClinicDto clinic : clinics) {
            String address = clinic.getAddress();
            String[] longLat = GoogleMapHelper.getLongLatFromGoogleMapLink(address);

            // Assuming longLat has the format: {latitude, longitude}
            coordinatesList.add(longLat[0]); // Add latitude
            coordinatesList.add(longLat[1]); // Add longitude
        }

        return coordinatesList; // Return a list of coordinates
    }





    @Override
    public List<DoctorDto> getDoctorsBySearchCriteria(String name, String specialty, String address) {
        // Example usage of the helper method
        String[] longLat = GoogleMapHelper.getLongLatFromGoogleMapLink(address);
        String latitude = longLat[0];
        String longitude = longLat[1];

        // Implement logic for searching based on name, specialty, and extracted coordinates
        return List.of();
    }

    //we have Clinic
    //convert


}
