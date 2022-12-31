package domain_layer;

public abstract class User {
    int ID;
    int accountType;
    String login;
    String firstName;
    String lastName;
    Address address;
    int phone;

    int getAccountType() {
        return this.accountType;
    }
    
    int getID() {
        return this.ID;
    }
    
    public String getLogin() {
        return this.login;
    }

    public String getFirstName() {
        return this.firstName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public Address getAddress() {
        return this.address;
    }
    
    public int getPhone() {
        return this.phone;
    }
    
    public void setData(String firstName, String lastName, Address address, int phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }
}