package presentation_layer;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import domain_layer.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginScreenController extends SceneController {
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
}
