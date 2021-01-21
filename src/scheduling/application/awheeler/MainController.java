/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduling.application.awheeler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author lexic
 */
public class MainController implements Initializable {

        public void aptManagerBtn(ActionEvent event) throws IOException 
    {
        // Changes scene to the Main Page 
        Parent mainParent = FXMLLoader.load(getClass().getResource("AppointmentManager.fxml"));
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
        public void custManagerBtn(ActionEvent event) throws IOException 
    {
        // Changes scene to the Main Page 
        Parent mainParent = FXMLLoader.load(getClass().getResource("CustomerManager.fxml"));
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
        
        public void reportsBtn(ActionEvent event) throws IOException 
    {
        // Changes scene to the Main Page 
        Parent mainParent = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
        
    public void logOutBtn(ActionEvent event) throws IOException, SQLException 
    {
        // Changes scene to the Main Page and closes the database connection
        DBConnection.closeConnection();
        Parent mainParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene mainScene = new Scene(mainParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }    
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
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
