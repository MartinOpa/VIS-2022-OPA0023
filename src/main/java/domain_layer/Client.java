package domain_layer;

import java.util.List;

import data_layer.ClientMapper;
import data_layer.ReservationsMapper;
import data_layer.VehicleMapper;

public class Client extends User {
    private ClientMapper ClientDB;
    private ReservationsMapper ReservationDB;
    private VehicleMapper VehicleDB;
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
        this.ClientDB = new ClientMapper();
        this.ReservationDB = new ReservationsMapper();
        this.VehicleDB = new VehicleMapper();
        this.LoginValidation = new LoginValidation();
    }
    
    public ClientMapper getClientDB() {
        return this.ClientDB;
    }
    
    public ReservationsMapper getReservationDB() {
        return this.ReservationDB;
    }
    
    public VehicleMapper getVehicleDB() {
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
        return this.ReservationDB.checkIfAvailable(dateTime);
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
