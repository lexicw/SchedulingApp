/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduling.application.awheeler;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import utils.City;
import utils.Country;
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
public class AddEditCustomerController implements Initializable {

    
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtCustId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddress;
    @FXML
    private ComboBox<City> cbCity;
    @FXML
    private TextField txtCountry;
    @FXML
    private TextField txtZip;
    @FXML
    private TextField txtPhone;
    
    private int customerId;
    private static boolean Add;
    private static Customer selectedCustomer;

    /**
     *
     * @param event
     */
    @FXML
    public void cancel(ActionEvent event) {
        sceneChange();
    }

    /**
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void save(ActionEvent event) throws SQLException {
        if (customerValidation()){
        if (Add) { // Called when adding a new customer
                PreparedStatement ps1 = DBConnection.getConnection().prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)",Statement.RETURN_GENERATED_KEYS);

                ps1.setString(1, txtAddress.getText());
                ps1.setString(2, " ");
                ps1.setInt(3, cbCity.getValue().getCityId());
                ps1.setString(4, txtZip.getText());
                ps1.setString(5, txtPhone.getText());
                ps1.setString(6, "test");
                ps1.setString(7, "test");
                boolean result1 = ps1.execute();
                int addressId = -1;
                ResultSet rs = ps1.getGeneratedKeys();
                
                if(rs.next()){
                    addressId = rs.getInt(1);
                }
            
            try {
                PreparedStatement ps2 = DBConnection.getConnection().prepareStatement("INSERT INTO customer "
                + "(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");
            
                ps2.setString(1, txtName.getText());
                ps2.setInt(2, addressId);
                ps2.setInt(3, 1);
                ps2.setString(4, "test");
                ps2.setString(5, "test");
                int result2 = ps2.executeUpdate();
               
            } catch (SQLException ex) {
            ex.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New customer added");
            alert.setHeaderText("New customer added!");
            alert.setContentText("Customer '" + txtName.getText() + "' has been added to the system!");
            alert.showAndWait();       
            sceneChange();
            

        } else {
        try {  // Called when updating an existing customer
                PreparedStatement ps1 = DBConnection.getConnection().prepareStatement("UPDATE address, customer "
                        + "SET address = ?, address2 = ?, address.cityId = ?, postalCode = ?, phone = ?, address.lastUpdate = CURRENT_TIMESTAMP, address.lastUpdateBy = ? "
                        + "WHERE customer.customerId = ? AND customer.addressId = address.addressId");

                ps1.setString(1, txtAddress.getText());
                ps1.setString(2, " ");
                ps1.setInt(3, cbCity.getValue().getCityId());
                ps1.setString(4, txtZip.getText());
                ps1.setString(5, txtPhone.getText());
                ps1.setString(6, "test");
                ps1.setString(7, "test");
                
                int result = ps1.executeUpdate();
                
                PreparedStatement ps2 = DBConnection.getConnection().prepareStatement("UPDATE customer, address, city "
                + "SET customerName = ?, address = ?, address2 = ?, address.cityId = ?, postalCode = ?, phone = ?, customer.lastUpdate = CURRENT_TIMESTAMP, customer.lastUpdateBy = ? "
                + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId");
            
                ps2.setString(1, txtName.getText());
                ps2.setString(2, txtAddress.getText());
                ps2.setString(3, " ");
                ps2.setInt(4, cbCity.getValue().getCityId());
                ps2.setString(5, txtZip.getText());
                ps2.setString(6, txtPhone.getText());
                ps2.setString(7, "test");
                ps2.setString(8, txtCustId.getText());
                int results = ps2.executeUpdate();
                
                
                
            } catch (SQLException ex) {
            ex.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer update");
            alert.setHeaderText("Customer updated");
            alert.setContentText("Customer '" + txtName.getText() + "' has been updated!");
            alert.showAndWait();       
            sceneChange();
        }
        }

    }
    
    // Sets country based on what is selected in the "city" combobox
    public void setCountry() {
        String result;
        switch (cbCity.getSelectionModel().getSelectedItem().toString()) {
        case "New York":
            result = "US"; 
            break;
        case "Los Angeles":
            result = "US";
            break;
        case "Toronto":
            result = "Canada";
            break;
        case "Vancouver":
            result = "Canada";
            break;
        case "Oslo":
            result = "Norway";
            break;
        default:
            result = " ";
            break;
    }
        txtCountry.setText(result);
    }
    
    @FXML
    public void cityCBChange(ActionEvent event) {
        setCountry();
    }
        
    private boolean customerValidation() {
        String customerName = txtName.getText();
        String customerAddress = txtAddress.getText();
        City customerCity = cbCity.getValue();
        String customerCountry = txtCountry.getText();
        String customerZip = txtZip.getText();
        String customerPhone = txtPhone.getText();
        
        String errorMsg = "";
        if (customerName.trim().isEmpty()) {
            errorMsg += "The name field is blank.\n"; 
        }
        if (customerAddress.trim().isEmpty()) {
            errorMsg += "The address field is blank.\n";  
        } 
        if (customerCity == null) {
            errorMsg += "A city has not been selected.\n"; 
        } 
        if (customerCountry.trim().isEmpty()) {
            errorMsg += "The country field is blank. \n"; 
        }         
        if (customerZip.trim().isEmpty()) {
            errorMsg += "The zip code field is blank.\n"; 
        } 
        if (customerZip.length() != 5) {
            errorMsg += "The zip code must contain 5 characters.\n"; 
        } 
        if (customerPhone.trim().isEmpty()) {
            errorMsg += "The phone number field is blank."; 
        }
        if (customerPhone.length() != 10) {
            errorMsg += "The phone number must contain 10 characters.\n"; 
        } 
        if (errorMsg.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect Information");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMsg);

            alert.showAndWait();

            return false;
        }
    }

    public void sceneChange() {
        Parent customerManager = null;
        try {
            CustomerDao.selectAllCustomers();
            customerManager = FXMLLoader.load(getClass().getResource("CustomerManager.fxml"));
            Scene scene = new Scene(customerManager);

            Stage stage = SchedulingApplicationAwheeler.getStage();

            stage.setScene(scene);

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(AddEditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        /**
     * Initializes the controller class.
     */
     @Override
     public void initialize(URL url, ResourceBundle rb) {
        cbCity.setItems(DataProvider.getCities());
        Add = CustomerManagerController.Add();
        if (!Add) {
        lblTitle.setText("Modify Customer");
        selectedCustomer = CustomerManagerController.getSelectedCustomer();
        customerId = selectedCustomer.getCustomerId();
        txtCustId.setText(customerId  + "");
        txtName.setText(selectedCustomer.getCustomerName());
        txtAddress.setText(selectedCustomer.getAddress());
        cbCity.setValue(selectedCustomer.getCity());
        setCountry();
        txtZip.setText(selectedCustomer.getPostalCode());
        txtPhone.setText(selectedCustomer.getPhone());
        }
        else{
            lblTitle.setText("Add Customer");
            txtCustId.setText("Auto-Generated");
        }
    }
    
}