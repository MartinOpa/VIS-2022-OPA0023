package data_layer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import domain_layer.Vehicle;
import domain_layer.Client;

public class VehicleMapper {
    public VehicleMapper() {
        
        try(Connection con = getConnection()) {
            initTable(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public List<Vehicle> load(Client client) {
        List<Vehicle> vehicles = new LinkedList<>();
        List<Vehicle> cleanVehicles = new LinkedList<>();
        String loginInput = client.getLogin();
        try(Connection con = getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT login, make, model, modelyear, vin, plate FROM vehicle");
            while(rs.next()) {
                String login = rs.getString(1);
                String make = rs.getString(2);
                String model = rs.getString(3);
                String modelyear = rs.getString(4);
                String vin = rs.getString(5);
                String plate = rs.getString(6);
                vehicles.add(new Vehicle(login, make, model, modelyear, vin, plate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // if(admin) -> return all
        if (loginInput.equals("admin")) {
            return vehicles;
        }
        
        // get vehicles by user login
        for (Vehicle element : vehicles) {
            if (element.getLogin().equals(loginInput)) {
                cleanVehicles.add(element);
            }
        }
        
        return cleanVehicles;
    }
    
    public List<Vehicle> filter(Client client, String parameter) {
        // filter the logged in user's List<Vehicle> by the given parameter
        List<Vehicle> list = client.getVehicleList();
        List<Vehicle> filteredList = new LinkedList<>();
        for (Vehicle vehicle : list) {
            if(vehicle.getMake().matches(".*" + parameter + ".*")
                    || vehicle.getModel().matches(".*" + parameter + ".*")
                    || vehicle.getVin().matches(".*" + parameter + ".*")
                    || vehicle.getYear().matches(".*" + parameter + ".*")
                    || vehicle.getPlate().matches(".*" + parameter + ".*")) {
                filteredList.add(vehicle);
            }
        }
        
        return filteredList;
    }

    public void save(Vehicle vehicle) {
        try( Connection con = getConnection()) {
            PreparedStatement pstm = con.prepareStatement("INSERT INTO vehicle (login, make, model, modelyear, vin, plate) VALUES (?, ?, ?, ?, ?, ?)");
                pstm.setString(1, vehicle.getLogin()); // FK
                pstm.setString(2, vehicle.getMake());
                pstm.setString(3, vehicle.getModel());
                pstm.setString(4, vehicle.getYear());
                pstm.setString(5, vehicle.getVin());
                pstm.setString(6, vehicle.getPlate());
                pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public void delete(Vehicle vehicle) {
        try( Connection con = getConnection()) {
            // update client info
            PreparedStatement pstm = con.prepareStatement("DELETE FROM vehicle WHERE vin = ? AND login = ?");
                pstm.setString(1, vehicle.getVin());
                pstm.setString(2, vehicle.getLogin());
                pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initTable(Connection con) {
        Statement stmt;
        try {
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE vehicle ("
                    + "   ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "   login VARCHAR(255) NOT NULL,"
                    + "   make VARCHAR(255) NOT NULL,"
                    + "   model VARCHAR(255) NOT NULL,"
                    + "   modelyear VARCHAR(255) NOT NULL,"
                    + "   vin VARCHAR(255) NOT NULL,"
                    + "   plate VARCHAR(255) NOT NULL,"
                    + "   PRIMARY KEY (ID))");
        } catch (SQLException e) {
            if(e.getSQLState().equals("X0Y32")) {
                return;
            }
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:derby:derbydb;create=true");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}