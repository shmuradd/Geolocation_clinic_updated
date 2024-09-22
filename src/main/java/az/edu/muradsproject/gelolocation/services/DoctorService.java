package az.edu.muradsproject.gelolocation.services;

import az.edu.muradsproject.gelolocation.dto.ClinicDto;
import az.edu.muradsproject.gelolocation.dto.DoctorDto;
import az.edu.muradsproject.gelolocation.payloads.ApiResponse;


import java.util.List;

public interface DoctorService {
    ApiResponse createDoctor(DoctorDto doctorDto);
    List<DoctorDto> getDoctorsBySearchCriteria(String name, String specialty, String address);
    public ApiResponse assignClinicToDoctor(Long doctorId, Long clinicId);
    public List<String> printClinicsWithCoordinates(List<ClinicDto> clinics);
}
