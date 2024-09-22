package az.edu.muradsproject.gelolocation.models;

public class AddressDistance {
    private String address;
    private double distance; // Distance in kilometers

    public AddressDistance(String address, double distance) {
        this.address = address;
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public double getDistance() {
        return distance;
    }
}
