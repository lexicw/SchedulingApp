/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduling.application.awheeler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Appointment;
import utils.DataProvider;
import utils.User;

/**
 *
 * @author lexic
 */
public class LoginController implements Initializable {
    
    @FXML private TextField usernameTxt;
    @FXML private PasswordField passwordTxt;
    @FXML private Label lblLang;
    @FXML private Button loginBtn;
    @FXML private Button exitBtn;
   
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblLoginForm;
    
    private String errorTitle;
    private String errorHeader;
    private String errorMsg;
   
    private final ZoneId zoneId = ZoneId.systemDefault();
    private final DateTimeFormatter timeDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    ResourceBundle rb;
    String user = null;
    
    @FXML
      // Method that is called when the "Exit" button is clicked
    public void exitBtn(ActionEvent event) 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit System");
        alert.setHeaderText("Are you sure you wish to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
           System.exit(0);
        } 
        else {
        }
    }
    
    // Function that activates upon clickin the login button.
    @FXML
        public void loginBtn(ActionEvent event) throws IOException 
    {
        boolean match = false;
        
        // verifies that the data enterred into the Username and Password fields is correct
        for (User user : DataProvider.getAllUsers()) {
            if (usernameTxt.getText().equals(user.getUserName() ) && passwordTxt.getText().equals(user.getPassword()))
            {
                match = true; 
                break;
            }
        }
        if (match)
        {

        // Prints username and current time to a text file
        FileWriter  writer = new FileWriter("users.txt", true);
        PrintWriter printWriter = new PrintWriter(writer);
        String user;
        user = usernameTxt.getText();
        printWriter.print(user + " ");
        java.util.Date date= new java.util.Date();
        printWriter.println(new Timestamp(date.getTime()));
        printWriter.close();
            
        aptReminder();
            
        // Changes scene to the Main Page 
        Parent mainParent = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
        }
        else 
        {
        // Alert that comes up when fields are incorrect
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(errorTitle);
        alert.setHeaderText(errorHeader);
        alert.setContentText(errorMsg);
        alert.showAndWait();
        }
        
    }
        
    // Function that is called to remind the user if there is an appointment within 15 minutes    
    public void aptReminder ()
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15 = now.plusMinutes(15);
        boolean reminderMatch = false;
        
        for (Appointment appointment : DataProvider.getAllAppointments()) {
            LocalDateTime aptDate = appointment.getStart();
            if (aptDate.isAfter(now.minusMinutes(1)) && aptDate.isBefore(nowPlus15))
            {
                reminderMatch = true; 
                break;
            }
        }
        if (reminderMatch)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Notification");
            alert.setHeaderText("Appointment within 15 minutes");
            alert.setContentText("There is an appointment that is scheduled within 15 minutes!");
            alert.showAndWait();
        }
        else
        {
            System.out.println("There are no appointments within 15 minutes.");
        }
    }
 

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("LanguageFiles/nat", locale);
        lblTitle.setText(rb.getString("title"));
        lblLoginForm.setText(rb.getString("login"));
        loginBtn.setText(rb.getString("login"));
        lblUsername.setText(rb.getString("username"));
        lblPassword.setText(rb.getString("password"));
        lblLang.setText(rb.getString("message"));
        errorHeader = rb.getString("errorheader");
        errorTitle = rb.getString("errortitle");
        errorMsg = rb.getString("errormsg");
    }    
    
}
