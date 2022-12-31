package presentation_layer;

import java.io.IOException;

import domain_layer.Client;
import domain_layer.Reservation;
import domain_layer.Vehicle;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {
    @FXML
    private Label greetings, time, date;    
    @FXML
    private TextField loginField, loginRegField, firstNameRegField, lastNameRegField, addressRegField1,
    addressRegField2, addressRegField3, phoneRegField, makeAdd, modelAdd, yearAdd, vinAdd, plateAdd;
    @FXML
    private PasswordField passwordField, passwordRegField, passwordCheckRegField;
    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private ChoiceBox<String> pickVehicle, pickTime;
    @FXML
    private DatePicker pickDate;

    private Client client = new Client(0, null, null, null, null, 0);
    
    @FXML
    private AnchorPane login;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) { 
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/LoginScreen.fxml"));
        try {
            login = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        SceneController controller = new SceneController();
        controller.setClient(client);
        
        Scene loginScreen = new Scene(login);  
        primaryStage.setScene(loginScreen);
        primaryStage.show();
    }
    
    private void exitProgram(WindowEvent evt) {
        SceneController.stopThread();
        
        System.exit(0);  
    }
    
}