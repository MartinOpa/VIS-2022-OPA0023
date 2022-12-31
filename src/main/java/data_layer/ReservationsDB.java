package data_layer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import domain_layer.Client;
import domain_layer.Reservation;

public class ReservationsDB {
    public ReservationsDB() {
        
        try(Connection con = getConnection()) {
            initTable(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public List<Reservation> load(Client client) {
        List<Reservation> reservations = new LinkedList<>();
        List<Reservation> cleanReservations = new LinkedList<>();
        String loginInput = client.getLogin();
        try(Connection con = getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT login, dateTimeRes, vin, issue FROM reservation");
            while(rs.next()) {
                String login = rs.getString(1);
                String dateTime = rs.getString(2);
                String vin = rs.getString(3);
                String issue = rs.getString(4);
                reservations.add(new Reservation(login, dateTime, vin, issue));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (loginInput.equals("admin")) {
            return reservations;
        }
        
        for (Reservation element : reservations) {
            if (element.getLogin().equals(loginInput)) {
                cleanReservations.add(element);
            }
        }
        
        return cleanReservations;
    }

    public void save(Reservation reservation) {
        try( Connection con = getConnection()) {
            PreparedStatement pstm = con.prepareStatement("INSERT INTO reservation (login, dateTimeRes, vin, issue) VALUES (?, ?, ?, ?)");
                pstm.setString(1, reservation.getLogin());
                pstm.setString(2, reservation.getDateTime());
                pstm.setString(3, reservation.getVin());
                pstm.setString(4, reservation.getIssue());
                pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    private void initTable(Connection con) {
        Statement stmt;
        try {
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE reservation ("
                    + "   ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "   login VARCHAR(255) NOT NULL,"
                    + "   dateTimeRes VARCHAR(255) NOT NULL,"
                    + "   vin VARCHAR(255) NOT NULL,"
                    + "   issue VARCHAR(255) NOT NULL,"
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