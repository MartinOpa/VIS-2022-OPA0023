package domain_layer;

import java.util.List;

public class Partner extends User {
    Partner(int ID, String login, String firstName, String lastName, Address address, int phone) {
        this.ID = ID;
        this.accountType = 1;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }
}
