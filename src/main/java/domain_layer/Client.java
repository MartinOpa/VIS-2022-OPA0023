package domain_layer;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
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
    
    public Client() {
        
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
    
    public void save(Client client) {
        client.getClientDB().save(client);
    }
    
    public void saveRes(Client client, Reservation reservation) {
        client.getReservationDB().save(reservation);
    }
    
    public void saveVeh(Client client, Vehicle vehicle) {
        client.getVehicleDB().save(vehicle);
    }

    public List<Vehicle> filter(Client client, String parameter) {
        List<Vehicle> list = client.getVehicleList();
        List<Vehicle> filteredList = new LinkedList<>();
        for (Vehicle vehicle : list) {
            if(vehicle.getLogin().matches(".*" + parameter + ".*") || vehicle.getMake().matches(".*" + parameter + ".*") || vehicle.getModel().matches(".*" + parameter + ".*")
                    || vehicle.getVin().matches(".*" + parameter + ".*") || vehicle.getYear().matches(".*" + parameter + ".*") || vehicle.getPlate().matches(".*" + parameter + ".*")) {
                filteredList.add(vehicle);
            }
        }
        
        return filteredList;
    }
    
    public List<Reservation> filter(String parameter, Client client) {
        List<Reservation> list = client.getReservationList();
        List<Reservation> filteredList = new LinkedList<>();
        for (Reservation reservation : list) {
            if(reservation.getLogin().matches(".*" + parameter + ".*") || reservation.getDateTime().matches(".*" + parameter + ".*")
                    || reservation.getVin().matches(".*" + parameter + ".*") || reservation.getIssue().matches(".*" + parameter + ".*")) {
                filteredList.add(reservation);
            }
        }
        
        return filteredList;
    }
    
    public boolean checkCredentials(String loginInput, String passwordInput) {
        for (String element : getData()) {
            int space = element.indexOf(" ");
            int divider = element.indexOf("|");
            int ID = Integer.parseInt(element.substring(0, divider));
            String login = element.substring(divider+1, space);
            String password = element.substring(space+1, element.length());
            
            if (login.equals(loginInput) && password.equals(passwordInput)) {
                ClientHolder holder = ClientHolder.getInstance();
                holder.setClient(new Client(ID, login, null, null, null, 0));
                holder.setClient(holder.getClient().getClientDB().load(holder.getClient()));
                return true;
            }
        }
        
        return false;
    }
    
    private List<String> getData() {
        Path p = Paths.get("./src/main/resources/users.txt");
        List<String> result = new ArrayList<String>();
        
        try (InputStream in = Files.newInputStream(p);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            in.close();
        } catch (IOException x) {
            System.err.println(x);
        }
        
        return result;
    }
    
    public int checkDuplicateLogin(String loginInput) {
        for (String element : getData()) {
            int space = element.indexOf(" ");
            int divider = element.indexOf("|");
            
            String existinglogin = element.substring(divider+1, space);
            
            if (existinglogin.equals(login)) {
                ID = 0;
                return -1;
            }
            ID += 1;
        }
        
        return ID;
    }
    
    public void storeUser(int ID, String login, String password) {
        String p = "./src/main/resources/users.txt";
        try {
            FileWriter out = new FileWriter(p, true);
            out.write("\n" + Integer.toString(ID) + "|" + login + " " + password);
            out.close();           
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}
