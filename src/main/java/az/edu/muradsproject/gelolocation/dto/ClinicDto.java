package az.edu.muradsproject.gelolocation.dto;

import az.edu.muradsproject.gelolocation.models.Doctor;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ClinicDto {
    private String name;
    private String address;
    private String contactDetails;
    private List<Doctor> doctors;
    private double distance; // Distance from user
    private String city;

    public ClinicDto(Long id, String name, double distance, String address) {
        this.name = name;
        this.distance = distance;
        this.address = address;
    }
}
