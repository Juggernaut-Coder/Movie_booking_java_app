package fxml;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import CreateMovDatabase.Movie;
import connect_mysql.Mysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import validityChecks.ValidityChecks;
import throwAlert.ThrowAlert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.lang.Exception;
import javafx.scene.input.MouseEvent;

public class ControlDash extends Mysql {
    private String username;
    private String email;
    private String phone;
    private String name;
    private String id;
    

    @FXML
    private Button buytickets_btn;
    @FXML
    private Button profile_btn;
    @FXML
    private AnchorPane profile_pane;
    @FXML
    private AnchorPane tickets_pane;
    @FXML
    private AnchorPane seat_pane;
    @FXML
    void bringup_profile_pane(ActionEvent event) {
        tickets_pane.setVisible(false);
        seat_pane.setVisible(false);
        profile_pane.setVisible(true);
    }
    @FXML
    void bringup_ticket_pane(ActionEvent event) {
        profile_pane.setVisible(false);
        seat_pane.setVisible(false);
        tickets_pane.setVisible(true);
    }

    @FXML
    private Label emailLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label usernameWelcomeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label usernameLabel;
    public void set_profile(String username, String email, String name, String phone, String id) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.id = id;
        usernameLabel.setText(this.username);
        usernameWelcomeLabel.setText(this.username);
        emailLabel.setText(this.email);
        nameLabel.setText(this.name);
        phoneLabel.setText(this.phone);
        idLabel.setText(this.id);
    }

    @FXML
    private Button updateEmailBtn;
    @FXML
    private Button updatePassBtn;
    @FXML
    private Button updatePhoneBtn;
    @FXML
    private TextField passField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    void updateEmailAction(ActionEvent event) throws Exception {
        ValidityChecks valid = new ValidityChecks();
        String mysql_update = "UPDATE booking_login SET email = ? WHERE customerID = ?";
        String email_str = emailField.getText();
        ThrowAlert throw_alert = new ThrowAlert();
        if(!valid.is_email_valid(email_str)) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Please ensure email is valid!");
        } else if(!valid.check_if_unique(email_str, "email")) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Email already exists in database!");
        } else { 
            try {
                connect = connect_db();
                preparedUpdate(mysql_update,email_str,this.id);
            } catch (Exception e) {
                throw e;
            } finally {
                close();
            }
            this.email = email_str;
            emailLabel.setText(this.email);        
        }
    }
    @FXML
    void updatePassAction(ActionEvent event) throws Exception {
        String mysql_update = "UPDATE booking_login SET password = ? WHERE customerID = ?";
        String pass = passField.getText();
        ThrowAlert throw_alert = new ThrowAlert();
        if(pass.length() <= 4) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Please ensure password length is greater than 4!");
        } else {
            try {
                connect = connect_db();
                preparedUpdate(mysql_update,pass,this.id);
            } catch (Exception e) {
                throw e;
            } finally {
                close();
            }
            throw_alert.throwAlert(AlertType.INFORMATION,"System Message","Password Changed!"); 
        }
    }
    @FXML
    void updatePhoneAction(ActionEvent event) throws Exception {
        ValidityChecks valid = new ValidityChecks();
        String mysql_update = "UPDATE booking_login SET phone = ? WHERE customerID = ?";
        String phone = phoneField.getText();
        ThrowAlert throw_alert = new ThrowAlert();
        if(!valid.check_if_string_is_int(phone)) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Please ensure the phone number is valid!");
        } else if(!valid.check_if_unique(phone, "phone")) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Phone number already exists in database!");
        } else {
            try {
                connect = connect_db();
                preparedUpdate(mysql_update,phone,this.id);
            } catch (Exception e) {
                throw e;
            } finally {
                close();
            }
            this.phone = phone;
            phoneLabel.setText(this.phone);    
        }
    }
    @FXML
    private ComboBox<String> date_combo;
    @FXML
    private ComboBox<String> theatre_combo;
    private static final int plannedDays = 7;
    @FXML
    private Button viewSeatBtn;
    public void setMovDatesAvail(ObservableList<String> dates) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(int i = 0; i < plannedDays; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            dates.add(date.format(format));
        }
    }
    public void init() {
        ObservableList<String> dates = FXCollections.observableArrayList();
        setMovDatesAvail(dates);
        date_combo.setItems(dates);
        theatre_combo.setItems(FXCollections.observableArrayList("Halifax","Bedford"));
    }
    @FXML
    private Button showList_btn;
    @FXML
    private TableColumn<Movie, String> timeMov;
    @FXML
    private TableColumn<Movie, String> movName;
    @FXML
    private TableView<Movie> movTab;
    private String date;
    private String tid;
    @FXML
    void showMovListAction(ActionEvent event) throws Exception {
        date = date_combo.getValue();
        tid = theatre_combo.getValue();
        ThrowAlert throw_alert = new ThrowAlert();
        movName.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        timeMov.setCellValueFactory(new PropertyValueFactory<>("time"));
        if( date == null || tid == null ) {
            throw_alert.throwAlert(AlertType.ERROR,"ERROR","Choose both date and theatre to continue");
        } else {
            String mysql = "SELECT movieName, TIME(screen) FROM movieList WHERE DATE(screen) = ? and theatreID = ?";
            ObservableList<Movie> list = FXCollections.observableArrayList();
            switch(tid) {
                case "Halifax":
                    tid = "0";
                    break;
                case "Bedford":
                    tid = "1";
                    break;
                default:
                throw new IllegalArgumentException("Invalid theatre ID");
            }
            try {
                connect = connect_db();
                preparedQuery(mysql,date,tid);
                while(result.next()) {
                    list.add(new Movie(result.getString(1),result.getString(2)));
                }
            } catch (Exception e) {
                throw e;
            } finally {
                close();
            }    
            movTab.getItems().clear();
            if(!list.isEmpty()) {
                movTab.setItems(list);
            }
        }
    }
     
    @FXML
    private ToggleButton tbtn1;
    @FXML
    private ToggleButton tbtn10;
    @FXML
    private ToggleButton tbtn11;
    @FXML
    private ToggleButton tbtn12;
    @FXML
    private ToggleButton tbtn13;
    @FXML
    private ToggleButton tbtn14;
    @FXML
    private ToggleButton tbtn15;
    @FXML
    private ToggleButton tbtn16;
    @FXML
    private ToggleButton tbtn17;
    @FXML
    private ToggleButton tbtn18;
    @FXML
    private ToggleButton tbtn19;
    @FXML
    private ToggleButton tbtn2;
    @FXML
    private ToggleButton tbtn20;
    @FXML
    private ToggleButton tbtn21;
    @FXML
    private ToggleButton tbtn22;
    @FXML
    private ToggleButton tbtn23;
    @FXML
    private ToggleButton tbtn24;
    @FXML
    private ToggleButton tbtn3;
    @FXML
    private ToggleButton tbtn4;
    @FXML
    private ToggleButton tbtn5;
    @FXML
    private ToggleButton tbtn6;
    @FXML
    private ToggleButton tbtn7;
    @FXML
    private ToggleButton tbtn8;
    @FXML
    private ToggleButton tbtn9;
    
    private String nameMovie;
    private String time;
    
    @FXML
    void viewSeatsAct(ActionEvent event) throws Exception {
        ToggleButton [] btnList = {tbtn1, tbtn2, tbtn3, tbtn4, tbtn5, tbtn6, tbtn7, tbtn8, tbtn9, tbtn10, tbtn11, tbtn12,
        tbtn13, tbtn14, tbtn15, tbtn16, tbtn17, tbtn18, tbtn19, tbtn20, tbtn21, tbtn22, tbtn23, tbtn24};
        String sql = "SELECT seatID FROM reservedSeat WHERE movieName = ? and date = ? and time = ? and theatreID = ?";
        ObservableList<Movie> mov = movTab.getSelectionModel().getSelectedItems();
        if(mov !=null && tid != null && date!=null) {
            nameMovie = mov.get(0).getMovieName();
            time = mov.get(0).getTime();
            try {
                connect = connect_db();
                preparedQuery(sql,nameMovie,date,time,tid);
                while(result.next()) {
                    btnList[result.getInt(1)-1].setDisable(true);        
                }

            } catch (Exception e) {
                throw e;
            } finally {
                close();
            }
            profile_pane.setVisible(false);
            tickets_pane.setVisible(false);
            seat_pane.setVisible(true);
        } else {
            new ThrowAlert().throwAlert(AlertType.ERROR,"ERROR","Please select a movie to view available seats");    
        }
        
    }
    @FXML
    void purchaseAct(ActionEvent event) {
        ToggleButton [] btnList = {tbtn1, tbtn2, tbtn3, tbtn4, tbtn5, tbtn6, tbtn7, tbtn8, tbtn9, tbtn10, tbtn11, tbtn12,
        tbtn13, tbtn14, tbtn15, tbtn16, tbtn17, tbtn18, tbtn19, tbtn20, tbtn21, tbtn22, tbtn23, tbtn24};
        String sql = "INSERT INTO reservedSeat (movieName, theatreID, date, time, seatID, cutomerID) VALUES (?, ?, ?, ?, ?, ?)";
        ThrowAlert throw_alert = new ThrowAlert();
        try {
            connect = connect_db();
            for(int i =0; i < btnList.length; i++) {
                if(btnList[i].isSelected()) {
                    int seat = i + 1;
                    preparedUpdate(sql,nameMovie,tid,date,time,String.valueOf(seat),id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        throw_alert.throwAlert(AlertType.INFORMATION,"System Message","Purchase Successful! Thank you");
        System.exit(0);
    }

    @FXML
    private Button bckBtn;

    @FXML
    void goBackAct(ActionEvent event) {
        date = null;
        tid = null;
        nameMovie = null;
        time = null;
        movTab.getItems().clear();
        date_combo.getSelectionModel().clearSelection();
        theatre_combo.getSelectionModel().clearSelection();
        profile_pane.setVisible(false);
        seat_pane.setVisible(false);
        tickets_pane.setVisible(true);
    }
    
}

