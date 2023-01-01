package presentation_layer;

import java.io.IOException;

import domain_layer.ClientHolder;
import domain_layer.Reservation;
import domain_layer.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ReservationsScreenController extends SceneController {
    public void filterMyReservations(ActionEvent event) throws IOException { 
        String parameter = searchResField.getText();
        
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/ReservationsScreen.fxml"));
        root = FXMLLoader.load();
        SceneController controller = FXMLLoader.getController();
        
        ClientHolder holder = ClientHolder.getInstance();
        client = holder.getClient();
        client.setReservationList(client.getReservationDB().load(client));

        reservationTable = controller.getReservationTable();
        
        TableColumn<Reservation, String> dateTime = new TableColumn<>("Datum a čas rezervace");
        dateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        TableColumn<Reservation, String> vin = new TableColumn<>("VIN kód vozidla");
        vin.setCellValueFactory(new PropertyValueFactory<>("vin"));
        TableColumn<Reservation, String> issue = new TableColumn<>("Popsaný problém");
        issue.setCellValueFactory(new PropertyValueFactory<>("issue"));

        reservationTable.getColumns().addAll(dateTime, vin, issue);
        
        ObservableList<Reservation> data = FXCollections.observableArrayList(client.filter(parameter, client.getReservationList()));
        reservationTable.setItems(data);
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void confirmNewReservation(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/NewReservationScreen.fxml"));
        root = FXMLLoader.load();
        SceneController controller = FXMLLoader.getController();
        boolean success = false;
        
        ClientHolder holder = ClientHolder.getInstance();
        client = holder.getClient();
        String dateTime = pickDate.getValue() + " " + pickTime.getSelectionModel().getSelectedItem();
        Vehicle vehicle = new Vehicle(null, null, null, null, null, null);
        
        String vehicleInput = pickVehicle.getSelectionModel().getSelectedItem();
        
        int vinPos = vehicleInput.indexOf("-");
        String vinInput = vehicleInput.substring(vinPos+1, vehicleInput.length());
        
        for (Vehicle veh : client.getVehicleList()) {
            if (veh.getVin().equals(vinInput)) {
                vehicle = veh; 
                success = true;
            }
        }
        
        if (success) {
            client.getReservationDB().save(new Reservation(client.getLogin(), dateTime, vehicle, describeProblem.getText()));
            holder.setClient(client);
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Rezervace byla úspěšně vytvořena");
            alert.show(); 
        }
    }
}
