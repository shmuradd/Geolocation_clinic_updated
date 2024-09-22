package az.edu.muradsproject.gelolocation.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressInput {
    private String addressLink1;
    private String addressLink2;
    private String addressLink3;
    // Add user's latitude and longitude
    private double userLat;
    private double userLon;
}
