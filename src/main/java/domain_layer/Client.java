package domain_layer;

import java.util.List;

import data_layer.ClientDB;
import data_layer.ReservationsDB;
import data_layer.VehicleDB;

public class Client extends User {
    private ClientDB ClientDB;
    private ReservationsDB ReservationDB;
    private VehicleDB VehicleDB;
    private List<Vehicle> VehicleList;
    private List<Reservation> ReservationList;
    private LoginValidation LoginValidation;
    
    public Client() {
        this.LoginValidation = new LoginValidation();
    }
    
    public Client(int ID, String login, String firstName, String lastName, Address address, int phone) {
        this.ID = ID;
        this.accountType = 0;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.ClientDB = new ClientDB();
        this.ReservationDB = new ReservationsDB();
        this.VehicleDB = new VehicleDB();
    }
    
    public ClientDB getClientDB() {
        return this.ClientDB;
    }
    
    public ReservationsDB getReservationDB() {
        return this.ReservationDB;
    }
    
    public VehicleDB getVehicleDB() {
        return this.VehicleDB;
    }
    
    public void setVehicleList(Client client) {
        this.VehicleList = client.getVehicleDB().load(client);
    }
    
    public List<Vehicle> getVehicleList() {
        return this.VehicleList;
    }
    
    public void setReservationList(Client client) {
        this.ReservationList = client.getReservationDB().load(client);
    }
    
    public List<Reservation> getReservationList() {
        return this.ReservationList;
    }
    
    public boolean checkIfAvailable(String dateTime) {
        List<Reservation> tempList = this.ReservationDB.load(new Client(0, "admin", null, null, null, 0));
        
        for (Reservation element : tempList) {
            if (dateTime.equals(element.getDateTime())) {
                return false;
            }
        }
        
        return true;
    }
    
    public void save(Client client) {
        this.ClientDB.save(client);
    }
    
    public void saveRes(Reservation reservation) {
        this.ReservationDB.save(reservation);
    }
    
    public void saveVeh(Vehicle vehicle) {
        this.VehicleDB.save(vehicle);
    }

    public List<Vehicle> filter(Client client, String parameter) {
        return this.VehicleDB.filter(client, parameter);
    }
    
    public List<Reservation> filter(String parameter, Client client) {
        return this.ReservationDB.filter(parameter, client);
    }
    
    public boolean checkCredentials(String loginInput, String passwordInput) {
        return this.LoginValidation.checkCredentials(loginInput, passwordInput);
    }
    
    public int checkDuplicateLogin(String loginInput) {
        return this.LoginValidation.checkDuplicateLogin(loginInput);
    }
    
    public void storeUser(int ID, String login, String password) {
        this.LoginValidation.storeUser(ID, login, password);
    }
    
    public boolean tryPasswordReset(String loginInput, String newpassword) {
        return this.LoginValidation.tryPasswordReset(loginInput, newpassword);
    }
}
