package domain_layer;

public class Reservation {
    private String login;
    private String dateTime;
    private Vehicle vehicle;
    private String vin;
    private String issue;
    
    public Reservation(String loginInput, String dateTime, Vehicle vehicle, String issueDescription) {
        this.login = loginInput;
        this.dateTime = dateTime;
        this.vehicle = vehicle;
        this.vin = vehicle.getVin();
        this.issue = issueDescription;
    }
    
    public Reservation(String loginInput, String dateTime, String vin, String issueDescription) {
        this.login = loginInput;
        this.dateTime = dateTime;
        this.vin = vin;
        this.issue = issueDescription;
    }
    
    public String getLogin() {
        return this.login;
    }
    
    public String getDateTime() {
        return this.dateTime;
    }
    
    public Vehicle getVehicle() {
        return this.vehicle;
    }
    
    public String getVin() {
        return this.vin;
    }
    
    public String getIssue() {
        return this.issue;
    }
}
