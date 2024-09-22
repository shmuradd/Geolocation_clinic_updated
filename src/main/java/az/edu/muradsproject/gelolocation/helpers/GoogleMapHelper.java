package az.edu.muradsproject.gelolocation.helpers;

import org.springframework.context.annotation.Bean;

public class GoogleMapHelper {
    //convert google map link to longitude and latitude
    public static String[] getLongLatFromGoogleMapLink(String googleMapLink) {
        String[] longLat = new String[2];
        String[] split = googleMapLink.split("@");
        String[] split1 = split[1].split(",");
        longLat[0] = split1[0];
        longLat[1] = split1[1];
        return longLat;
    }
}
