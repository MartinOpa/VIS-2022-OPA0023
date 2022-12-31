package domain_layer;

public class Address {
    String street;
    String city;
    String postalCode;
    
    public Address(String street, String city, String postalCode) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }
    
    public String getStreet() {
        return this.street;
    }
    
    public String getCity() {
        return this.city;
    }
    
    public String getPostalCode() {
        return this.postalCode;
    }
}
