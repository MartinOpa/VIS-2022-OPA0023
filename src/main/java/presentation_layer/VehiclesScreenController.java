package presentation_layer;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import domain_layer.ClientHolder;
import domain_layer.Vehicle;

public class VehiclesScreenController extends SceneController {
    @FXML
    protected TextField makeAdd;
    @FXML
    protected TextField modelAdd;
    @FXML
    protected TextField yearAdd;
    @FXML
    protected TextField vinAdd;
    @FXML
    protected TextField plateAdd;
    @FXML
    protected TextField searchVehField;
    
    @SuppressWarnings("unchecked")
    public void filterMyVehicles(ActionEvent event) throws IOException {
        String parameter = searchVehField.getText();
        
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/VehiclesScreen.fxml"));
        root = FXMLLoader.load();
        SceneController controller = FXMLLoader.getController();
        
        ClientHolder holder = ClientHolder.getInstance();
        client = holder.getClient();
        client.setVehicleList(client);
        holder.setClient(client);

        vehicleTable = controller.getVehicleTable();
        
        TableColumn<Vehicle, String> make = new TableColumn<>("Značka");
        make.setCellValueFactory(new PropertyValueFactory<>("make"));
        TableColumn<Vehicle, String> model = new TableColumn<>("Model");
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        TableColumn<Vehicle, String> year = new TableColumn<>("Ročník");
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        TableColumn<Vehicle, String> vin = new TableColumn<>("VIN");
        vin.setCellValueFactory(new PropertyValueFactory<>("vin"));
        TableColumn<Vehicle, String> plate = new TableColumn<>("SPZ");
        plate.setCellValueFactory(new PropertyValueFactory<>("plate"));
        
        vehicleTable.getColumns().addAll(make, model, year, vin, plate);
        
        ObservableList<Vehicle> data = FXCollections.observableArrayList(client.filter(client, parameter));
        vehicleTable.setItems(data);
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @SuppressWarnings("unchecked")
    public void addVehicle(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/VehiclesScreen.fxml"));
        root = FXMLLoader.load();
        SceneController controller = FXMLLoader.getController();
        
        ClientHolder holder = ClientHolder.getInstance();
        client = holder.getClient();
        
        Vehicle newVehicle = new Vehicle(client.getLogin(), makeAdd.getText(), modelAdd.getText(), yearAdd.getText(), vinAdd.getText(), plateAdd.getText());
        client.saveVeh(newVehicle);
        client.setVehicleList(client);
        holder.setClient(client);
        
        vehicleTable = controller.getVehicleTable();
        
        TableColumn<Vehicle, String> make = new TableColumn<>("Značka");
        make.setCellValueFactory(new PropertyValueFactory<>("make"));
        TableColumn<Vehicle, String> model = new TableColumn<>("Model");
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        TableColumn<Vehicle, String> year = new TableColumn<>("Ročník");
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        TableColumn<Vehicle, String> vin = new TableColumn<>("VIN");
        vin.setCellValueFactory(new PropertyValueFactory<>("vin"));
        TableColumn<Vehicle, String> plate = new TableColumn<>("SPZ");
        plate.setCellValueFactory(new PropertyValueFactory<>("plate"));
        
        vehicleTable.getColumns().addAll(make, model, year, vin, plate);
        
        ObservableList<Vehicle> data = FXCollections.observableArrayList(client.getVehicleList());
        
        vehicleTable.setItems(data);
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
