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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * @author lexic
 */
public class AppointmentDao {
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static String sqlStatement;
    private static Connection conn;
    private static Appointment appointment;
    private static ZoneId newZoneId = ZoneId.systemDefault();
    
        public static void selectAllAppointments() throws SQLException
    {
        sqlStatement = "select appointment.appointmentId, appointment.type, appointment.start, appointment.end, customer.customerId, customer.customerName\n" +
        "FROM appointment, customer\n" + 
        "WHERE appointment.customerId = customer.customerId";
        
        conn = DBConnection.getConnection();
        DBQuery.setPreparedStatement(conn, sqlStatement);
        ps = DBQuery.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();
        
        while(rs.next()){
        String appointmentId = rs.getString("appointmentId");
        int customerId = rs.getInt("customerId");
        String customerName = rs.getString("customerName");
        String type = rs.getString("type");
        
        LocalDate sd = rs.getDate("start").toLocalDate();
        LocalTime st = rs.getTime("start").toLocalTime();
        LocalDateTime start = LocalDateTime.of(sd, st);
        Timestamp tsStart = rs.getTimestamp("start");
        ZonedDateTime zoneDateTimeStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZonedDateTime zoneDTS = zoneDateTimeStart.withZoneSameInstant(newZoneId);
        
        LocalDate ed = rs.getDate("end").toLocalDate();
        LocalTime et = rs.getTime("end").toLocalTime();
        LocalDateTime end = LocalDateTime.of(ed, et);
        Timestamp tsEnd = rs.getTimestamp("end");
        ZonedDateTime zoneDateTimeEnd = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZonedDateTime zoneDTE = zoneDateTimeEnd.withZoneSameInstant(newZoneId);

        
        appointment = new Appointment(appointmentId, customerId, customerName, type, sd, st, start, zoneDTS, ed, et, end, zoneDTE);
        DataProvider.addAppointment(appointment);
        
        }
    }
    
}
