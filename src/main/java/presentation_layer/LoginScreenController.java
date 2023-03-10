package presentation_layer;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import domain_layer.Client;

public class LoginScreenController extends SceneController {
    @FXML
    protected TextField loginField;
    @FXML
    protected TextField loginFieldReset;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected PasswordField passwordFieldReset;
    
    public void logIn(ActionEvent event) throws IOException {
        boolean success = false;
        client = new Client();
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
    
    public void resetPassword(ActionEvent event) throws IOException {
        boolean success = false;
        FXMLLoader FXMLLoader = new FXMLLoader(App.class.getResource("/fxml/ForgotPassword.fxml"));
        root = FXMLLoader.load();
        
        String loginInput = loginFieldReset.getText();
        String newpassword = passwordFieldReset.getText();
        
        Client client = new Client();
        
        success = client.tryPasswordReset(loginInput, newpassword);
        
        if (success) {
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
}
