package az.edu.muradsproject.gelolocation.services;

import az.edu.muradsproject.gelolocation.dto.ClinicDto;
import az.edu.muradsproject.gelolocation.dto.ClinicSearchRequest;
import az.edu.muradsproject.gelolocation.models.Clinic;

import java.util.List;

public interface ClinicService {
    //return clinics by doctorId
    public List<ClinicDto> getClinicsByDoctorId(Long doctorId);
    public String getCityNameByClinicId(Long clinicId);
    public List<ClinicDto> searchClinicsByDoctorAndCity(ClinicSearchRequest searchRequest);public List<ClinicDto> getClinics();
    public String[] extractCoordinatesFromGoogleMapsLink(String googleMapsLink);
    public double calculateDistance(double userLat, double userLon, double clinicLat, double clinicLon);
    public ClinicDto convertToDto(Clinic clinic);
}
