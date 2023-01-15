package presentation_layer;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {
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
        
        Scene loginScreen = new Scene(login);  
        primaryStage.setScene(loginScreen);
        primaryStage.show();
    }
    
    @SuppressWarnings("unused")
    private void exitProgram(WindowEvent evt) {
        SceneController.stopThread();
        
        System.exit(0);  
    }
    
}
