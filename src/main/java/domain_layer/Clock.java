package domain_layer;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import presentation_layer.SceneController;

public class Clock implements Initializable {
    /*
    private final static Clock INSTANCE = new Clock();
    
    private Clock() {}
    
    public static Clock getInstance() {
        return INSTANCE;
    }
    
    private void timenow() {
        Thread thread = new Thread(() ->{
            boolean stop = false;
            SceneController controller = SceneController.getInstance();
            Client client;
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
                String timenow = sdf.format(new Date());
                String datenow = dmy.format(new Date());
                Platform.runLater(()->{
                    controller.setTime(timenow);
                    controller.setDate(datenow);
                    controller.setGreeting(hello);
                });
            }
        });
        thread.start();
    }
*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //timenow();
    }
}
