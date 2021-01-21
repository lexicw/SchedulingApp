/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduling.application.awheeler;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author lexic
 */
public class ReportsController implements Initializable {

    @FXML
    private Button btnMonth;
    @FXML
    private Button btnCustomer;
    @FXML
    private Button btnSchedules;
    @FXML
    private TextArea txtArea;
    @FXML
    private Button backBtn;
    @FXML
    private Button exitBtn;
    
    @FXML
    MenuBar menuBar;

    
    public void generateMonth (ActionEvent event) {
        
            try {
            Statement statement = DBConnection.getConnection().createStatement();
            String query = "SELECT MONTHNAME(`start`) AS 'Month', description AS 'Type', COUNT(*) as 'Amount'\n" +
            "FROM appointment \n" +
            "GROUP BY MONTHNAME(`start`), description;";
            ResultSet results = statement.executeQuery(query);
            StringBuilder reportTxt = new StringBuilder();            
           reportTxt.append(String.format("%-50s %-50s %-1s \n", "Month", "Type", "Amount"));
                    
           reportTxt.append("\n");
            while(results.next()) {
                reportTxt.append(String.format("%-50s %-50s %-1s \n", 
                results.getString("Month"), results.getString("Type"), results.getInt("Amount")));
            }
            statement.close(); 
            txtArea.setText(reportTxt.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    public void generateCustomer (ActionEvent event) {
            try {
            Statement statement = DBConnection.getConnection().createStatement();
            String query = "SELECT customer.customerName, COUNT(*) as 'Amount'" +
            "FROM appointment, customer\n" + 
            "WHERE customer.customerId = appointment.customerId\n" +
            "GROUP BY customerName";
            ResultSet results = statement.executeQuery(query);
            StringBuilder reportTxt = new StringBuilder();
            reportTxt.append(String.format("%-50s %-50s \n", "Customer", "Amount"));
            reportTxt.append("\n");
            while(results.next()) {
                reportTxt.append(String.format("%-50s %-50s \n", 
                    results.getString("customerName"), results.getInt("Amount")));
            }
            statement.close();
            txtArea.setText(reportTxt.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    public void generateSchedules (ActionEvent event) {
            try {
            Statement statement = DBConnection.getConnection().createStatement();
            String query = "SELECT appointment.contact, appointment.description, customer.customerName, appointment.`start`, appointment.`end` \n" +
"                    FROM appointment, customer\n" +
"                    WHERE customer.customerId = appointment.customerId\n" +
"                    ORDER BY `start`;";
            ResultSet results = statement.executeQuery(query);
            StringBuilder reportTxt = new StringBuilder();
            reportTxt.append(String.format("%-20s %-20s %-20s %-40s %-1s \n", 
                    "Consultant", "Appointment", "Customer", "Start", "End"));
            reportTxt.append("\n");
            while(results.next()) {
                reportTxt.append(String.format("%-20s %-20s %-20s %-30s %-1s \n", 
                    results.getString("contact"), results.getString("description"), results.getString("customerName"),
                    results.getString("start"), results.getString("end")));
            }
            statement.close();
            txtArea.setText(reportTxt.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
            @FXML
        public void mainMenuBtn(ActionEvent event) throws IOException 
    {
        Parent mainParent = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)menuBar.getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
    
    @FXML
        public void reportMenuBtn(ActionEvent event) throws IOException 
    {
        Parent mainParent = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)menuBar.getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
        
    @FXML
        public void logOutMenuBtn(ActionEvent event) throws IOException, SQLException 
    {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log out");
        alert.setHeaderText("Are you sure you wish to log out?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            DBConnection.closeConnection();
            Parent mainParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene mainScene = new Scene(mainParent);
            Stage window = (Stage)menuBar.getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        } 
        else {
        }
    }
      
    @FXML
    public void exit(ActionEvent event) 
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
        
    @FXML
    public void custManagerMenuBtn(ActionEvent event) throws IOException 
    {
        Parent mainParent = FXMLLoader.load(getClass().getResource("CustomerManager.fxml"));
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)menuBar.getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
    @FXML
        public void apptManagerMenuBtn(ActionEvent event) throws IOException 
    {
        Parent mainParent = FXMLLoader.load(getClass().getResource("AppointmentManager.fxml"));
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)menuBar.getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
