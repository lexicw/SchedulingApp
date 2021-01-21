/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduling.application.awheeler;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Customer;
import utils.CustomerDao;
import utils.DBConnection;
import utils.DBQuery;
import utils.DataProvider;

/**
 * FXML Controller class
 *
 * @author lexic
 */
public class CustomerManagerController implements Initializable {
    
    @FXML
     TableView<Customer> CustomerTableView;

    @FXML
    private TableColumn<Customer, Integer> CustIdCol;

    @FXML
    private TableColumn<Customer, String> CustNameCol;

    @FXML
    private TableColumn<Customer, String> CustAddressCol;
    @FXML
    private TableColumn<Customer, String> CustCityCol;
    @FXML
    private TableColumn<Customer, String> CustCountryCol;
    
    @FXML
    private TableColumn<Customer, String> CustZipCol;

    @FXML
    private TableColumn<Customer, String> CustPhoneCol;
    
    
    @FXML
    MenuBar menuBar;
    
    private static boolean Add;
    private static Customer selectedCustomer;
    private static ObservableList<Customer> customerList;

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
    
    @FXML
    void add(ActionEvent event) throws SQLException {
        Add = true;
        CustomerTableView.getItems().clear();
        sceneChange();
    }
    
    // Method that is called when "Edit" button is clicked
    @FXML
    void edit(ActionEvent event) {
        Add = false;
        selectedCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText("Please select a customer!");
            a.showAndWait();
            return;
        }
        CustomerTableView.getItems().clear();
        sceneChange();
    }
    
    
    
        // Method that is called when "Delete" button is clicked
        public void deleteCustomer(ActionEvent event) throws IOException {
        Customer selectedCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            // When there is no product selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Error");
            alert.setHeaderText("You must select a customer!");
            alert.showAndWait();
        } 
        else {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Customer");
        alert.setHeaderText("Are you sure you want to delete '" + selectedCustomer.getCustomerName() + "'?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Deleted");
            alert.setHeaderText("Customer '" + selectedCustomer.getCustomerName() + "' has been removed from the system." );
            alert.showAndWait();
            
            try{           
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("DELETE customer.*, address.* from customer, address WHERE customer.customerId = ? AND customer.addressId = address.addressId");
            pst.setInt(1, selectedCustomer.getCustomerId()); 
            pst.executeUpdate();   
            updateCustomerTable();
                
        } catch(SQLException e){
            e.printStackTrace();
        }       
        } else {
        }
        }
    }

    
    public void updateCustomerTable() throws SQLException {
        CustomerTableView.getItems().clear();
        CustomerDao.selectAllCustomers();
        CustomerTableView.setItems(DataProvider.getAllCustomers());
    }
    
    
     public void sceneChange() {
        Parent addEdit = null;
        try {
            addEdit = FXMLLoader.load(getClass().getResource("AddEditCustomer.fxml"));
            Scene scene = new Scene(addEdit);

            Stage stage = SchedulingApplicationAwheeler.getStage();

            stage.setScene(scene);

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // My first lambda expression. Makes it easier to populate the data into the tableview.
        CustIdCol.setCellValueFactory(cellData -> {
          return new ReadOnlyObjectWrapper<>(cellData.getValue().getCustomerId());
        });
        CustNameCol.setCellValueFactory(cellData -> {
          return new ReadOnlyObjectWrapper<>(cellData.getValue().getCustomerName());
        });
        CustAddressCol.setCellValueFactory(cellData -> {
          return new ReadOnlyObjectWrapper<>(cellData.getValue().getAddress());
        });
        CustCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        CustCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        CustZipCol.setCellValueFactory(cellData -> {
          return new ReadOnlyObjectWrapper<>(cellData.getValue().getPostalCode());
        });
        CustPhoneCol.setCellValueFactory(cellData -> {
          return new ReadOnlyObjectWrapper<>(cellData.getValue().getPhone());
        });
            CustomerTableView.setItems(DataProvider.getAllCustomers());
    }    
    public static boolean Add() {
        return Add;
    }

    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public static ObservableList<Customer> getCustomerList() {
        return customerList;
    }
    
}
