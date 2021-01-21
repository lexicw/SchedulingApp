/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduling.application.awheeler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.AppointmentDao;
import utils.CustomerDao;
import utils.DBConnection;
import utils.DBQuery;
import utils.DataProvider;
import utils.User;
import utils.UserDao;

/**
 *
 * @author lexic
 */
public class SchedulingApplicationAwheeler extends Application {
    
    private static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

     public void showCustomerScreen(User currentUser) {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApplicationAwheeler.class.getResource("CustomerManager.fxml"));
            AnchorPane customerScreen = (AnchorPane) loader.load();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, IOException {
        
    DBConnection.startConnection();
    UserDao.selectAllUsers();
    CustomerDao.selectAllCustomers();
    AppointmentDao.selectAllAppointments();
    DataProvider.loadBusinessHours();
    DataProvider.loadCustomers();
    DataProvider.cityList();
    

        launch(args);
        DBConnection.closeConnection();
    }
        public static Stage getStage() {
        return stage;
    }
    
}
