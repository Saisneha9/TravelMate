package handlers;


public class City {
    public String name;
    public double lat;
    public double lon;
    public int hotelPrice;
    public int foodCost;

    public City(String name, double lat, double lon, int hotelPrice, int foodCost) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.hotelPrice = hotelPrice;
        this.foodCost = foodCost;
    }
}