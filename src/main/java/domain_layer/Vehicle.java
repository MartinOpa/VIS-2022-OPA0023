package domain_layer;

public class Vehicle {
    private String login;
    private String make;
    private String model;
    private String year;
    private String vin;
    private String plate;

    public Vehicle(String login, String make, String model, String year, String vin, String plate) {
        this.login = login;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.plate = plate;
    }
    
    public String getLogin() {
        return this.login;
    }
    
    public String getMake() {
        return this.make;
    }
    
    public String getModel() {
        return this.model;
    }
    
    public String getYear() {
        return this.year;
    }
    
    public String getVin() {
        return this.vin;
    }
    
    public String getPlate() {
        return this.plate;
    }
}
