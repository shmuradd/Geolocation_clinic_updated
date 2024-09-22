package az.edu.muradsproject.gelolocation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicSearchRequest {
    private String doctorName;
    private String specialty;
    private String city;
}
