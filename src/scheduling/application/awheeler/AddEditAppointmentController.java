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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import static java.time.temporal.TemporalQueries.localTime;
import java.util.Locale;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Appointment;
import utils.AppointmentDao;
import utils.City;
import utils.Customer;
import utils.DBConnection;
import utils.DataProvider;
import static utils.DataProvider.getAllCustomers;

/**
 * FXML Controller class
 *
 * @author lexic
 */
public class AddEditAppointmentController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField txtAptId;
    @FXML
    private ComboBox<String> customerCB;
    @FXML
    private TextField txtCustId;
    @FXML
    private TextField txtCustName;
    @FXML
    private TextField txtType;
    @FXML
    private DatePicker txtDate;
    @FXML
    private ComboBox<String> cbStart;
    @FXML
    private ComboBox<String> cbEnd;
    
    Locale locale = Locale.getDefault();
    
    @FXML
    private static boolean Schedule;
    @FXML
    private static Appointment selectedAppointment;
    private final DateTimeFormatter timeDateTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateDateTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId newZoneId = ZoneId.systemDefault();
    private static Appointment appointment;
    boolean match = false;
    
    @FXML
    // Method that takes you back to the previous screen when "Cancel" is clicked
    public void cancel(ActionEvent event) {
        sceneChange();
    }
    
    // Method that is called upon clicking the save button
    @FXML
    public void save(ActionEvent event) throws SQLException {
        if (aptValidation()){
            LocalDate date = txtDate.getValue();

            // Gets combobox selections and converts from String to Localtime format
            LocalTime start = LocalTime.parse(cbStart.getSelectionModel().getSelectedItem(), timeDateTF);
            LocalTime end = LocalTime.parse(cbEnd.getSelectionModel().getSelectedItem(), timeDateTF);

            // Takes LocalDate 'date' and combines with LocalTime 'start' and 'end'
            LocalDateTime startLocal = LocalDateTime.of(date, start);
            LocalDateTime endLocal = LocalDateTime.of(date, end);
            
            // Converts LocalDateTime to Timestamp
            Timestamp startLocalTS = Timestamp.valueOf(startLocal); 
            Timestamp endLocalTS = Timestamp.valueOf(endLocal); 

        if (Schedule) { // Method that is called when you save a new appointment
            try {
                PreparedStatement ps = DBConnection.getConnection().prepareStatement("INSERT INTO appointment "
                + "(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");
                            
                ps.setString(1, txtCustId.getText());
                ps.setInt(2, 1);
                ps.setString(3, "not needed");
                ps.setString(4, "not needed");
                ps.setString(5, "not needed");
                ps.setString(6, "not needed");
                ps.setString(7, txtType.getText());
                ps.setString(8, "not needed");
                ps.setTimestamp(9, startLocalTS);
                ps.setTimestamp(10, endLocalTS);
                ps.setString(11, "test");
                ps.setString(12, "test");
                int result = ps.executeUpdate();
                
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New appointment added");
            alert.setHeaderText("New appointment added!");
            alert.setContentText("New appointment has been added to the system!");
            alert.showAndWait();       
            sceneChange();
                
               
            } catch (SQLException ex) {
            ex.printStackTrace();
            }

        } else {
            try { // Method that is called when updating an existing appointment
                PreparedStatement ps1 = DBConnection.getConnection().prepareStatement("UPDATE appointment "
                        + "SET customerId = ?, type = ?, start = ?, end = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? "
                        + "WHERE appointmentId = ?");
            
                ps1.setString(1, txtCustId.getText());
                ps1.setString(2, txtType.getText());
                ps1.setTimestamp(3, startLocalTS);
                ps1.setTimestamp(4, endLocalTS);
                ps1.setString(5, "test");
                ps1.setString(6, selectedAppointment.getAppointmentId());
                int result = ps1.executeUpdate();
                
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Modified");
            alert.setHeaderText("Appointment Modified");
            alert.setContentText("Your appointment has been modified successfully!");
            alert.showAndWait();       
            sceneChange();
                
            } catch (SQLException ex) {
            ex.printStackTrace();
            }
        

            }
        }
    }
        // Method that is called to make sure appointment times do not overlap
        public boolean aptOverlap() {
        LocalDate date = txtDate.getValue();
        LocalTime start = LocalTime.parse(cbStart.getSelectionModel().getSelectedItem(), timeDateTF);
        LocalDateTime startLocal = LocalDateTime.of(date, start);
        ZonedDateTime startUTC = startLocal.atZone(newZoneId).withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp startTS = Timestamp.valueOf(startUTC.toLocalDateTime()); 
        LocalTime end = LocalTime.parse(cbEnd.getSelectionModel().getSelectedItem(), timeDateTF);
        LocalDateTime endLocal = LocalDateTime.of(date, end);
        Timestamp endTS = Timestamp.valueOf(endLocal); 
        try {
            Statement statement = DBConnection.getConnection().createStatement();            
            String query = "SELECT * FROM appointment WHERE ('" + startLocal + "' BETWEEN start AND end OR '" + endLocal + "' BETWEEN start AND end OR '" + startLocal + "' < start AND '" + endLocal + "' > end)";
            ResultSet results = statement.executeQuery(query);
            System.out.println("startLocal = " + startLocal);
            System.out.println("endLocal = " + endLocal);
            System.out.println(results);
            if(results.next()) {
                if(results.getString("appointmentId").equals(txtAptId.getText())) {
                    statement.close();
                    return false;
                }
                statement.close();
                return true;
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return true;
        }
        
        
        
        
    }
        
        
    // Sets customer Id based on what is selected in the "customer name" combobox
    public void setCustId() {
        String customerName = customerCB.getSelectionModel().getSelectedItem();
        {
            for (Customer customer : getAllCustomers()) {
                if (customerName.equals(customer.getCustomerName())) {
                    String customerId = String.valueOf(customer.getCustomerId());
                    txtCustId.setText(customerId);
                }
            }
            
        }
    }
        
        // Method that checks appointment fields to make sure the information is valid
        private boolean aptValidation() {
        String customerName = customerCB.getValue();
        String type = txtType.getText();
        LocalDate date = txtDate.getValue();
        LocalTime start = LocalTime.parse(cbStart.getSelectionModel().getSelectedItem(), timeDateTF);
        LocalTime end = LocalTime.parse(cbEnd.getSelectionModel().getSelectedItem(), timeDateTF);
        
        String errorMsg = "";
        if (customerName == null) {
         errorMsg += "A customer name has not been selected.\n"; 
        }
        if (type.trim().isEmpty()) {
            errorMsg += "The type field is blank.\n";  
        } 
        if (date == null) {
            errorMsg += "A date has not been selected.\n"; 
        } 
        if (start == null) {
            errorMsg += "A start time has not been selected.\n"; 
        } 
        if (end == null) {
            errorMsg += "An end time has not been selected.\n"; 
        } 
        if (aptOverlap()) {
            errorMsg += "There is an appointment that conflicts with the time of this appointment in the system.\n"; 
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
        Parent appointmentManager = null;
        try {
            AppointmentDao.selectAllAppointments();
            appointmentManager = FXMLLoader.load(getClass().getResource("AppointmentManager.fxml"));
            Scene scene = new Scene(appointmentManager);

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

        Schedule = AppointmentManagerController.Schedule();
        customerCB.setItems(DataProvider.getCustomers());
        cbStart.setItems(DataProvider.getBusinessHours());
        cbEnd.setItems(DataProvider.getBusinessHours());
        txtDate.setValue(LocalDate.now());

        if (!Schedule) {
        lblTitle.setText("Update Appointment");
        selectedAppointment = AppointmentManagerController.getSelectedAppointment();
        txtAptId.setText(selectedAppointment.getAppointmentId());
        customerCB.getSelectionModel().select(selectedAppointment.getCustomerName());
        String customerId = String.valueOf(selectedAppointment.getCustomerId());
        txtCustId.setText(customerId);
        txtType.setText(selectedAppointment.getType());
        txtDate.setValue((selectedAppointment.getSd()));
        cbStart.getSelectionModel().select(selectedAppointment.getStart().format(timeDateTF));
        cbEnd.getSelectionModel().select(selectedAppointment.getEnd().format(timeDateTF));
        
       
        }
        else{
        lblTitle.setText("Schedule Appointment");
        txtAptId.setText("Auto-Generated");
        }
    }  
    
}
