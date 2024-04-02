import fxml.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;



public class Main {
    public static void main(String[] args) {
        Application.launch(TicketManager.class, args);
    }

    public static class TicketManager extends Application {

        private double x;
        private double y;

        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./fxml/Login_Screen.fxml"));
            Parent root = loader.load();
            Controller ctrl = loader.getController();
            Scene scene = new Scene(root);
            
            // Mouse Dragging of the window
            root.setOnMousePressed((MouseEvent event) ->{
            x = event.getSceneX();
            y = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) ->{
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
            });
            // End of Mouse dragging
        
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("FXML Welcome");
            stage.setScene(scene);
            stage.show();
        }
    }
}
