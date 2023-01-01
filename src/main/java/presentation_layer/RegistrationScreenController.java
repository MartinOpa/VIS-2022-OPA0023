package presentation_layer;

import java.io.IOException;

import domain_layer.Address;
import domain_layer.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RegistrationScreenController extends LoginScreenController {
    public void register(ActionEvent event) throws IOException {
        Client client = new Client();
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
                client = new Client(ID, login, firstName, lastName, address, phone);
                client.save(client);
                
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
}
