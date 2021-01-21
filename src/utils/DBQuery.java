/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lexic
 */
public class DBQuery {
    
    private static PreparedStatement ps; // Statement reference
    
    // Create Statement Object
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException
    {
        ps = conn.prepareStatement(sqlStatement);
    }
    
    // Return Statement object
    public static PreparedStatement getPreparedStatement()
    {
        return ps;
    }
}
