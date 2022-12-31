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

public class VehicleDB {
    public VehicleDB() {
        
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
        
        if (loginInput.equals("admin")) {
            return vehicles;
        }
        
        for (Vehicle element : vehicles) {
            if (element.getLogin().equals(loginInput)) {
                cleanVehicles.add(element);
            }
        }
        
        return cleanVehicles;
    }

    public void save(Vehicle vehicle) {
        try( Connection con = getConnection()) {
            //getConnection().createStatement().execute("DELETE FROM score");
            PreparedStatement pstm = con.prepareStatement("INSERT INTO vehicle (login, make, model, modelyear, vin, plate) VALUES (?, ?, ?, ?, ?, ?)");
                pstm.setString(1, vehicle.getLogin());
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
            return DriverManager.getConnection("jdbc:derby:vehicle;create=true");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}