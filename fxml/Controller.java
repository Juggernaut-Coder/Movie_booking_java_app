package fxml;

import connect_mysql.*;
import validityChecks.ValidityChecks;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import throwAlert.ThrowAlert;

public class Controller extends Mysql {
    @FXML
    private Button close;
    @FXML
    private Button close1;
    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private Button minimize;
    @FXML
    private Button minimize1;
    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage)close.getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    private Hyperlink new_user_hyperlink;
    @FXML
    private Hyperlink old_user_hyperlink;
    @FXML
    private AnchorPane login_anchr;
    @FXML
    private AnchorPane signup_anchr;
    @FXML
    void switch_page(ActionEvent event) {
        if (event.getSource() == new_user_hyperlink) {
            login_anchr.setVisible(false);
            signup_anchr.setVisible(true);
        } else if (event.getSource() == old_user_hyperlink) {
            signup_anchr.setVisible(false);
            login_anchr.setVisible(true);
        }

    }

    @FXML
    private Button login;
    @FXML
    private TextField input_password;
    @FXML
    private TextField input_username;
    @FXML
    void login(ActionEvent event) throws Exception {
        
        ThrowAlert throw_alert = new ThrowAlert();
        String mysql_login = "SELECT * FROM booking_login WHERE username = ? and password = ?";
        String username = input_username.getText();
        String pass = input_password.getText();
        
        if(username.isEmpty() || pass.isEmpty()) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Please fill both username and password to login!");
        } else {
            try{
                connect = connect_db();
                preparedQuery(mysql_login,username,pass);
                if(result.next()) {
                    throw_alert.throwAlert(AlertType.INFORMATION,"System Message","Login Successful!");
                    Stage stage = (Stage)close.getScene().getWindow();                    
                    stage.close();                    
                    new Dashboard(username, result.getString("name"),result.getString("email"),result.getString("phone"),result.getString("customerID"));
                } else {
                    throw_alert.throwAlert(AlertType.ERROR,"ERROR","Wrong username or password!");
                }   
            } catch (Exception e) {
                throw e;
            } finally {
                close();
            }
        }   
    }


    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField phone;
    @FXML
    private TextField signup_username;
    @FXML
    private TextField signup_password;
    @FXML
    private Button signup_btn;
    @FXML
    void signup_action(ActionEvent event) throws Exception {
        ValidityChecks valid = new ValidityChecks();
        String Name = name.getText();
        String Email = email.getText();
        String Phone = phone.getText();
        String username = signup_username.getText();
        String pass = signup_password.getText();
        ThrowAlert throw_alert = new ThrowAlert();
        String mysql_signup = "INSERT INTO booking_login (name, email, phone, username, password) VALUES (?, ?, ?, ?, ?)";
        if(Name.isEmpty() || Email.isEmpty() || Phone.isEmpty() || username.isEmpty() || pass.isEmpty()) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Please fill all the fields to signup!");
        } else if(pass.length() <= 4) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Password length must be greater than 4!");
        } else if (!valid.check_if_string_is_int(Phone)){
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Please ensure phone number is valid!");
        } else if(!valid.is_email_valid(Email)) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Please ensure the email is valid!");
        } else if(valid.check_if_unique(Email,Phone,username)) {
                try {
                    connect = connect_db();
                    preparedUpdate(mysql_signup,Name,Email,Phone,username,pass);
                } catch (Exception e) {
                    throw e;
                } finally {
                    close();
                }
            throw_alert.throwAlert(AlertType.INFORMATION,"System Message","Signup Successful! Please log in!");
            signup_anchr.setVisible(false);
            login_anchr.setVisible(true);
        } else {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Duplicate entry of email, phone or username! Please login or try again!");
        }     
    }

}

