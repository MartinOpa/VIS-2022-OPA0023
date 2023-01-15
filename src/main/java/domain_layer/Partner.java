package domain_layer;

public class Partner extends Client {
    Partner(int ID, String login, String firstName, String lastName, Address address, int phone) {
        super(ID, login, firstName, lastName, address, phone);
        this.accountType = 1;
    }
}
