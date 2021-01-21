/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author lexic
 */
public abstract class DataProvider {
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<String> businessHours = FXCollections.observableArrayList();
    private static ObservableList<String> customers = FXCollections.observableArrayList();
    private static ObservableList<City> cities = FXCollections.observableArrayList();

    public static void setCustomers(ObservableList<String> customers) {
        DataProvider.customers = customers;
    }
    Locale locale = Locale.getDefault();
    
    public static void addUser(User user){
        allUsers.add(user);
    }
        public static void removeUser(User user){
        allUsers.remove(user);
    }
        public static ObservableList<User> getAllUsers(){
        return allUsers;
    }
            
    
    public static void addCustomer(Customer customer){
        allCustomers.add(customer);
    }
        public static void removeCustomer(Customer customer){
        allCustomers.remove(customer);
    }
        public static ObservableList<Customer> getAllCustomers(){
        return allCustomers;
    }
        
        
    public static void addAppointment(Appointment appointment){
        allAppointments.add(appointment);
    }
        public static void removeAppointment(Appointment appointment){
        allAppointments.remove(appointment);
    }
        public static ObservableList<Appointment> getAllAppointments(){
        return allAppointments;
    }
        
    // Populates "Start" end "End" combo boxes in the Schedule/modify appointment window   
    public static void loadBusinessHours() {
        LocalTime startTime = LocalTime.of(7, 0);
        DateTimeFormatter timeDateTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        for(int i = 0; i <= 9; i++) {
        startTime = startTime.plusHours(1);
        businessHours.add(startTime.format(timeDateTF));
        }
       
    }
    // Populates the customer combobox used in the Schedule/Modify appointment window
    public static void loadCustomers() {
        for (Customer customer : getAllCustomers()) {
            String customerName = customer.getCustomerName();
            customers.add(customerName);
        }

    }
    
    // Populates the city combobox in the Add/Modify customer window
    public static void cityList() {
    try(
    PreparedStatement statement = DBConnection.getConnection().prepareStatement("SELECT cityId, city, countryId FROM city;");
    ResultSet rs = statement.executeQuery();){
    
    while (rs.next()) {
        cities.add(new City(rs.getInt("city.cityId"),rs.getString("city.city"),rs.getInt("city.countryId")));
    }
    
    } catch (SQLException sqe) {
    sqe.printStackTrace();
    } catch (Exception e) {
    }
    }
    
    
    

    public static ObservableList<City> getCities() {
        return cities;
    }
    
     public static ObservableList<String> getCustomers() {
        return customers;
    }

    public static ObservableList<String> getBusinessHours() {
        return businessHours;
    }
   
    
}
