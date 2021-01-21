/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lexic
 */
public class DBConnection {
    
    //  JDBC URL Parts
    private static final String protocol = "jdbc"; 
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U05oMr";
    
    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;
    
    // Driver and Connection Interface Reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null; 
    
    private static final String username = "U05oMr"; // Username
    private static String password = "53688563885"; // Password
    
    public static void startConnection()
    {
        try
        {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SQLException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
   public static Connection getConnection() 
   {
       return conn;
   }
    public static void closeConnection() throws SQLException
    {
        try 
        {
        conn.close();
        System.out.println("Connection Closed");
        }
        catch(SQLException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
