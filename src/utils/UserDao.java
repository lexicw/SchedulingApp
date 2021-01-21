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

/**
 *
 * @author lexic
 */
public abstract class UserDao {
    
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static String sqlStatement;
    private static Connection conn;
    private static User user;
    
    public static void selectAllUsers() throws SQLException
    {
        sqlStatement = "select * from user";
        conn = DBConnection.getConnection();
        DBQuery.setPreparedStatement(conn, sqlStatement);
        ps = DBQuery.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();
        
        while(rs.next()){
        int userId = rs.getInt("userId");
        String userName = rs.getString("userName");
        String password = rs.getString("password");
        int active = rs.getInt("active");
        LocalDate cdd = rs.getDate("createDate").toLocalDate();
        LocalTime cdt = rs.getTime("createDate").toLocalTime();
        LocalDateTime createDate = LocalDateTime.of(cdd, cdt);
        String createdBy = rs.getString("createdBy");
        LocalDate lud = rs.getDate("lastUpdate").toLocalDate(); 
        LocalTime lut = rs.getTime("lastUpdate").toLocalTime();
        LocalDateTime lastUpdate = LocalDateTime.of(lud, lut);
        String lastUpdateBy = rs.getString("lastUpdateBy");
        
        user = new User(userId, userName, password, active, createDate, createdBy, lastUpdate, lastUpdateBy);
        DataProvider.addUser(user);
        }
    }
}
