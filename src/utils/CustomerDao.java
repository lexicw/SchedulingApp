/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author lexic
 */
public abstract class CustomerDao {
    
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static String sqlStatement;
    private static Connection conn;
    private static Customer customer;
    
        public static void selectAllCustomers() throws SQLException
    {
        sqlStatement = "SELECT customer.customerId, customer.customerName, address.address, address.postalCode, city.cityId, city.city, city.countryId, country.countryId, country.country, address.phone\n" +
        "FROM customer, address, city, country\n" +
        "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId ";
        conn = DBConnection.getConnection();
        DBQuery.setPreparedStatement(conn, sqlStatement);
        ps = DBQuery.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();
        
        while(rs.next()){
        int customerId = rs.getInt("customerId");
        String customerName = rs.getString("customerName");
        String address = rs.getString("address");
        City city = new City(rs.getInt("city.cityId"), rs.getString("city.city"), rs.getInt("city.countryId"));
        String country = rs.getString("country");
        String postalCode = rs.getString("postalCode");
        String phone = rs.getString("phone");

        
        customer = new Customer(customerId, customerName, address, city, country, postalCode, phone);
        DataProvider.addCustomer(customer);
        
        }
        
    }

}
