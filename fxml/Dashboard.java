package fxml;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class Dashboard {
   private double x;
   private double y;

   public Dashboard(String dashboard_username, String name, String email, String phone, String id) throws Exception {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("./DashboardGui.fxml"));
      Parent root = loader.load();
      ControlDash ctrl = loader.getController();
      ctrl.set_profile(dashboard_username,email,name,phone,id);
      ctrl.init();
      Scene scene = new Scene(root);
      Stage stage = new Stage();
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
      stage.setTitle("Dashboard");
      stage.setScene(scene);
      stage.show();
   }


}
