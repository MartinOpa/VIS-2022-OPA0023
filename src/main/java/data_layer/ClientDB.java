package data_layer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import domain_layer.Address;
import domain_layer.Client;

public class ClientDB {
    public ClientDB() {
        
        try(Connection con = getConnection()) {
            initTable(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public Client load(Client client) {
        List<Client> clients = new LinkedList<>();
        String loginInput = client.getLogin();
        Address address;
        try(Connection con = getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID, login, firstName, lastName, street, city, postalCode, phone FROM client");
            while(rs.next()) {
                int ID = rs.getInt(1);
                String login = rs.getString(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                String street = rs.getString(5);
                String city = rs.getString(6);
                String postalCode = rs.getString(7);
                int phone = rs.getInt(8);
                address = new Address(street, city, postalCode);
                clients.add(new Client(ID, login, firstName, lastName, address, phone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        for (Client element : clients) {
            if (element.getLogin().equals(loginInput)) {
                client = element;
                return client;
            }
        }
        
        return client;
    }

    public void save(Client client) {
        try( Connection con = getConnection()) {
            //getConnection().createStatement().execute("DELETE FROM score");
            PreparedStatement pstm = con.prepareStatement("INSERT INTO client (login, firstName, lastName, street, city, postalCode, phone) VALUES (?, ?, ?, ?, ?, ?, ?)");
            Address address = client.getAddress();
            String street = address.getStreet();
            String city = address.getCity();
            String postalCode = address.getPostalCode();
                pstm.setString(1, client.getLogin());
                pstm.setString(2, client.getFirstName());
                pstm.setString(3, client.getLastName());
                pstm.setString(4, street);
                pstm.setString(5, city);
                pstm.setString(6, postalCode);
                pstm.setInt(7, client.getPhone());
                pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    private void initTable(Connection con) {
        Statement stmt;
        try {//login, firstName, lastName, street, city, postalCode, phone
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE client ("
                    + "   ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "   login VARCHAR(63) NOT NULL,"
                    + "   firstName VARCHAR(63) NOT NULL,"
                    + "   lastName VARCHAR(63) NOT NULL,"
                    + "   street VARCHAR(63) NOT NULL,"
                    + "   city VARCHAR(63) NOT NULL,"
                    + "   postalCode VARCHAR(8),"
                    + "   phone INT NOT NULL,"
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
            return DriverManager.getConnection("jdbc:derby:client;create=true");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}