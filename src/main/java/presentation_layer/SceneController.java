package presentation_layer;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import domain_layer.Client;
import domain_layer.Vehicle;
import domain_layer.Reservation;
import domain_layer.ClientHolder;

public class SceneController implements Initializable {
    
    public SceneController() {}
    
    protected Client client;
    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    public volatile static boolean stop = false;
    protected int loginAttempts = 0;
    
    @FXML
    private Label greetings;
    @FXML
    private Label time;
    @FXML
    private Label date;
    @FXML
    protected TextField loginField;
    @FXML
    protected TextField loginFieldReset;
    @FXML
    protected TextField loginRegField;
    @FXML
    protected TextField firstNameRegField;
    @FXML
    protected TextField lastNameRegField;
    @FXML
    protected TextField addressRegField1;
    @FXML
    protected TextField addressRegField2;
    @FXML
    protected TextField addressRegField3;
    @FXML
    protected TextField phoneRegField;
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
    protected TextField searchResField;
    @FXML
    protected TextField searchVehField;
    @FXML
    protected TextField describeProblem;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected PasswordField passwordFieldReset;
    @FXML
    protected PasswordField passwordRegField;
    @FXML
    protected PasswordField passwordCheckRegField;
    @FXML
    protected TableView<Vehicle> vehicleTable;
    @FXML
    protected TableView<Reservation> reservationTable;
    @FXML
    protected ChoiceBox<String> pickVehicle;
    @FXML
    protected ChoiceBox<String> pickTime;
    @FXML
    protected DatePicker pickDate;
    
    public void logOut(ActionEvent event) throws IOException {
        ClientHolder holder = ClientHolder.getInstance();
        holder.setClient(new Client(0, null, null, null, null, 0));
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/LoginScreen.fxml"));
        root = FXMLLoader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void myReservations(ActionEvent event) throws IOException {  
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
        
        ObservableList<Reservation> data = FXCollections.observableArrayList(client.getReservationList());
        reservationTable.setItems(data);
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void newReservation(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/NewReservationScreen.fxml"));
        root = FXMLLoader.load();
        SceneController controller = FXMLLoader.getController();
        
        ChoiceBox<String> pickVehicle = controller.getVehiclePicker();
        ChoiceBox<String> pickTime = controller.getTimePicker();
        
        ClientHolder holder = ClientHolder.getInstance();
        client = holder.getClient();
        client.setVehicleList(client.getVehicleDB().load(client));
        
        pickTime.getItems().addAll("8:00", "9:00", "10:00", "11:00", "13:00", "14:00", "15:00");
        
        for (Vehicle vehicle : client.getVehicleList()) {
            String clientVehicle = vehicle.getMake() + " " + vehicle.getModel() + ", " + vehicle.getYear()
            + ", VIN-" + vehicle.getVin();
            pickVehicle.getItems().add(clientVehicle);
        }
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void myVehicles(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/VehiclesScreen.fxml"));
        root = FXMLLoader.load();
        SceneController controller = FXMLLoader.getController();
        
        ClientHolder holder = ClientHolder.getInstance();
        client = holder.getClient();
        client.setVehicleList(client.getVehicleDB().load(client));
        holder.setClient(client.getClientDB().load(holder.getClient()));

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
    
    public void aboutUs(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/AboutUs.fxml"));
        root = FXMLLoader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private void timenow() {
        Thread thread = new Thread(() ->{
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat dmy = new SimpleDateFormat("d.M.y");
            ClientHolder holder = ClientHolder.getInstance();
            client = holder.getClient();
            String hello = "Vítejte, " + client.getFirstName() + "!";
            while(!stop) {
                try {
                    Thread.sleep(500);
                } catch(Exception e) {
                    System.out.println(e);
                }
                time = getTime();
                String timenow = sdf.format(new Date());
                String datenow = dmy.format(new Date());
                Platform.runLater(()->{
                    time.setText(timenow);
                    date.setText(datenow);
                    greetings.setText(hello);
                });
            }
        });
        thread.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timenow();  
    }
    
    public static void stopThread() {
        stop = true;
    }
    
    public Label getTime() {
        return this.time;
    }
    
    public TableView<Vehicle> getVehicleTable() {
        return this.vehicleTable;
    }
    
    public TableView<Reservation> getReservationTable() {
        return this.reservationTable;
    }
    
    public ChoiceBox<String> getVehiclePicker() {
        return this.pickVehicle;
    }
    
    public ChoiceBox<String> getTimePicker() {
        return this.pickTime;
    }
}
