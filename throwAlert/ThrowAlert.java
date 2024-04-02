package throwAlert;

import javafx.beans.NamedArg;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ThrowAlert {

    public void throwAlert(@NamedArg("alertType") AlertType var, String title, String msg) {
        Alert alert = new Alert(var);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
        alert.close();
    }

}
