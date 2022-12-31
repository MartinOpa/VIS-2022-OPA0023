package presentation_layer;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import domain_layer.Address;
import domain_layer.ClientHolder;

public class SceneController implements Initializable {
    
    public SceneController() {}
    
    private Client client;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public volatile static boolean stop = false;
    private int loginAttempts = 0;
    
    @FXML
    private Label greetings;
    @FXML
    private Label time;
    @FXML
    private Label date;
    @FXML
    private TextField loginField, loginFieldReset, loginRegField, firstNameRegField,
    lastNameRegField, addressRegField1, addressRegField2, addressRegField3, phoneRegField,
    makeAdd, modelAdd, yearAdd, vinAdd, plateAdd,
    searchResField, searchVehField, describeProblem;
    @FXML
    private PasswordField passwordField, passwordFieldReset, passwordRegField, passwordCheckRegField;
    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private ChoiceBox<String> pickVehicle, pickTime;
    @FXML
    private DatePicker pickDate;
    
    public void logIn(ActionEvent event) throws IOException {
        boolean success = false;
        success = client.checkCredentials(loginField.getText(), passwordField.getText());
        
        if(success) {
            FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/MainMenuScreen.fxml"));
            root = FXMLLoader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            loginAttempts += 1;
            if (loginAttempts < 4) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Špatné jméno nebo heslo");
                alert.show();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Váš účet byl zablokován. Resetujte si, prosím, heslo v hlavním menu.");
                alert.show();
            }
        }
    }
    
    public void switchToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/MainMenuScreen.fxml"));
        root = FXMLLoader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
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
    
    public void createAccount(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/RegistrationScreen.fxml"));
        root = FXMLLoader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void forgotPassword(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/ForgotPassword.fxml"));
        root = FXMLLoader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void register(ActionEvent event) throws IOException {
        int ID = 0;
        String login = loginRegField.getText();
        String password = passwordRegField.getText();
        String passwordCheck = passwordCheckRegField.getText();
        String firstName = firstNameRegField.getText();
        String lastName = lastNameRegField.getText();
        String street = addressRegField1.getText();
        String city = addressRegField2.getText();
        String postalCode = addressRegField3.getText();
        Address address = new Address(street, city, postalCode);
        int phone = Integer.parseInt(phoneRegField.getText());
        
        ID = client.checkDuplicateLogin(login);
        
        if (password.equals(passwordCheck)) {
            if (ID > 0) {
                client.storeUser(ID, login, password);
                this.client = new Client(ID, login, firstName, lastName, address, phone);
                this.client.getClientDB().save(this.client);
                
                FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/LoginScreen.fxml"));
                root = FXMLLoader.load();
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Uživatel s tímto loginem již existuje");
                alert.show();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Zadaná hesla se neshodují");
            alert.show();
        }
        
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
    
    public void filterMyVehicles(ActionEvent event) throws IOException {
        String parameter = searchVehField.getText();
        
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
        
        ObservableList<Vehicle> data = FXCollections.observableArrayList(client.filter(client.getVehicleList(), parameter));
        vehicleTable.setItems(data);
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void AddVehicle(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/VehiclesScreen.fxml"));
        root = FXMLLoader.load();
        SceneController controller = FXMLLoader.getController();
        
        ClientHolder holder = ClientHolder.getInstance();
        client = holder.getClient();
        
        Vehicle newVehicle = new Vehicle(client.getLogin(), makeAdd.getText(), modelAdd.getText(), yearAdd.getText(), vinAdd.getText(), plateAdd.getText());
        client.getVehicleDB().save(newVehicle);
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
    
    public void resetPassword(ActionEvent event) throws IOException {
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/ForgotPassword.fxml"));
        root = FXMLLoader.load();
        
        String loginInput = loginFieldReset.getText();
        String newpassword = passwordFieldReset.getText();
        
        int saveID = 0;
        int ID = -1;
        String login = "";
        boolean success = false;
        
        Path p = Paths.get("./src/main/resources/users.txt");
        List<String> result = new ArrayList<String>();
        
        try (InputStream in = Files.newInputStream(p);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                int space = line.indexOf(" ");
                int divider = line.indexOf("|");
                ID = Integer.parseInt(line.substring(0, divider));
                login = line.substring(divider+1, space);                
                if (login.equals(loginInput)) {
                    saveID = ID;
                    success = true;
                } else result.add(line);
            }
            in.close();
        } catch (IOException x) {
            System.err.println(x);
        }
        
        if (success) {
            String write = "./src/main/resources/users.txt";
            
            FileWriter out = new FileWriter(write);
            out.write("");
            out.close();
            
            try {
                out = new FileWriter(write, true);
                for (String element : result) {
                    out.write(element + "\n");
                }
                out.write(Integer.toString(ID) + "|" + loginInput + " " + newpassword); 
                out.close();           
            } catch (IOException x) {
                System.err.println(x);
            }
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Úspěch, nyní se můžete přihlásit");
            alert.show();
            
            FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/LoginScreen.fxml"));
            root = FXMLLoader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Uživatel s tímto loginem neexistuje.");
            alert.show();
        }
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public Client getClient() {
        return this.client;
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
    
    public DatePicker getDatePicker() {
        return this.pickDate;
    }
    
    public TextField getLoginFieldReset() {
        return this.loginFieldReset;
    }
    
    public PasswordField getPasswordFieldReset() {
        return this.passwordFieldReset;
    }
}
