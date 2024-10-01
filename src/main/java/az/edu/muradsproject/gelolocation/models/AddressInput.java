package az.edu.muradsproject.gelolocation.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressInput {

    // Add user's latitude and longitude
    private double userLat;
    private double userLon;
}
