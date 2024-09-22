package az.edu.muradsproject.gelolocation.dto;

import az.edu.muradsproject.gelolocation.models.Clinic;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Data
public class DoctorDto
{
    private String name;
    private String specialty;
    private String photoUrl;
    private String qualifications;
    private String experience;
    private boolean isActive;
    private List<ClinicDto> clinics;
}
