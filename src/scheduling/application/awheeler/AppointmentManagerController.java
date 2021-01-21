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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.Appointment;
import utils.AppointmentDao;
import utils.Customer;
import utils.DBConnection;
import utils.DBQuery;
import utils.DataProvider;

/**
 * FXML Controller class
 *
 * @author lexic
 */
public class AppointmentManagerController implements Initializable {

    @FXML
    private TableView<Appointment> ApptTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;

    @FXML
    private TableColumn<Appointment, String> customerNameCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;
    
    @FXML
    private TableColumn<Appointment, String> startCol;
    
    @FXML
    private TableColumn<Appointment, String> endCol;
    
    @FXML
    private RadioButton weekRB;
    
    @FXML
    private RadioButton monthRB;
    
    private static boolean Schedule;
    private static Appointment selectedAppointment;
    private static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    
    @FXML
    MenuBar menuBar;
    
    @FXML
    private ToggleGroup appointmentTG;
    
    private final DateTimeFormatter dateTimeF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId newZoneId = ZoneId.systemDefault();
    @FXML
    private TableColumn<?, ?> customerIdCol;
    
   @FXML 
    // Function that will activate upon clicking "Schedule"        
    void scheduleApt(ActionEvent event) {
        Schedule = true;
        appointmentList.clear();
        ApptTableView.getItems().clear();
        sceneChange();
    }
    
    @FXML
    // Function that will activate upon clicking "Edit"    
    void editApt(ActionEvent event) {
        Schedule = false;
        selectedAppointment = ApptTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText("Please select an appointment!");
            a.showAndWait();
            return;
        }
        ApptTableView.setItems(DataProvider.getAllAppointments());
        appointmentList.clear();
        ApptTableView.getItems().clear();
        sceneChange();
    }
        
    @FXML
    // Function that will activate upon clicking "Delete"  
    void deleteApt(ActionEvent event) {
        selectedAppointment = ApptTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText("Please select an appointment!");
            a.showAndWait();
            return;
        }
        else {
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this appointment?");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> {
            Statement statement = DBQuery.getPreparedStatement();
            String deleteStatement = "DELETE FROM appointment WHERE appointmentId =" + selectedAppointment.getAppointmentId();
                try {
                    statement.execute(deleteStatement);
                } catch (SQLException ex) {
                    Logger.getLogger(AppointmentManagerController.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            );
        appointmentList.clear();
        ApptTableView.getItems().clear();
        refresh();
        ApptTableView.setItems(DataProvider.getAllAppointments());
        }
    }
    
    // My second lambda expression. This makes it easier to sort data by month and by week 
    @FXML
    void aptWeekRB(ActionEvent event) {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus7 = now.plusDays(7);
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentList);
        filteredData.setPredicate(row -> {

            LocalDate rowDate = row.getSd();
            

            return rowDate.isAfter(now) && rowDate.isBefore(nowPlus7);
        });
        ApptTableView.setItems(filteredData);
    }
    
    @FXML
    void aptMonthRB(ActionEvent event) {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Month = now.plusMonths(1);
        
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentList);
        filteredData.setPredicate(row -> {

            LocalDate rowDate = row.getSd();
     

            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Month);
        });
        ApptTableView.setItems(filteredData);

    }

       private void appointmentFiltering() {
        try{     
        PreparedStatement statement = DBConnection.getConnection().prepareStatement(
        "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, "
                + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment, customer "
                + "WHERE appointment.customerId = customer.customerId "
                + "ORDER BY `start`");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String xAppointmentId = rs.getString("appointment.appointmentId");
                int xCustomerId = rs.getInt("customer.customerId");
                String xType = rs.getString("appointment.type");
                LocalDate xSd = rs.getDate("appointment.start").toLocalDate();
                LocalTime xSt = rs.getTime("appointment.start").toLocalTime();
                LocalDateTime xStart = LocalDateTime.of(xSd, xSt); 
                
                Timestamp tsStart = rs.getTimestamp("appointment.start");
                ZonedDateTime ZoneDateTimeStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime localStart = ZoneDateTimeStart.withZoneSameInstant(newZoneId);
                
                LocalDate xEd = rs.getDate("appointment.end").toLocalDate();
                LocalTime xEt = rs.getTime("appointment.end").toLocalTime();
                LocalDateTime xEnd = LocalDateTime.of(xEd, xEt);
                
                Timestamp tsEnd = rs.getTimestamp("appointment.end");
                ZonedDateTime ZoneDateTimeEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime localEnd = ZoneDateTimeEnd.withZoneSameInstant(newZoneId);
                
                String xTitle = rs.getString("appointment.title");
                String xCustomer = rs.getString("customer.customerName");
                String xUser = rs.getString("appointment.createdBy");
                appointmentList.add(new Appointment(xAppointmentId, xCustomerId, xCustomer, xType, xSd, xSt, xStart, localStart, xEd, xEt, xEnd, localEnd));
}
            
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
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
    
    public void updateApptTable() throws SQLException {
        ApptTableView.getItems().clear();
        AppointmentDao.selectAllAppointments();
        ApptTableView.setItems(DataProvider.getAllAppointments());
    }
    
    public void sceneChange() {
        Parent addEdit = null;
        try {
            addEdit = FXMLLoader.load(getClass().getResource("AddEditAppointment.fxml"));
            Scene scene = new Scene(addEdit);

            Stage stage = SchedulingApplicationAwheeler.getStage();

            stage.setScene(scene);

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void refresh() {
        Parent aptManager = null;
        try {
            aptManager = FXMLLoader.load(getClass().getResource("AppointmentManager.fxml"));
            Scene scene = new Scene(aptManager);

            Stage stage = SchedulingApplicationAwheeler.getStage();

            stage.setScene(scene);

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static boolean Schedule() {
        return Schedule;
    }

    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public static ObservableList<Appointment> getAppointmentList() {
       return appointmentList;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    appointmentTG = new ToggleGroup();
    this.weekRB.setToggleGroup(appointmentTG);
    this.monthRB.setToggleGroup(appointmentTG);
    
    ApptTableView.getItems().clear();
    ApptTableView.setItems(DataProvider.getAllAppointments());
    appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId")); // Get Instance id
    customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName")); // Get Instance id
    typeCol.setCellValueFactory(new PropertyValueFactory<>("type")); // Get Instance id
    startCol.setCellValueFactory(new PropertyValueFactory<>("zoneDTS")); // Get Instance id
    endCol.setCellValueFactory(new PropertyValueFactory<>("zoneDTE")); // Get Instance id
    appointmentFiltering();

    }    
    
   
}
