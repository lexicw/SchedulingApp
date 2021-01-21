/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 *
 * @author lexic
 */
public class Appointment {
        private String appointmentId;
        private int customerId;
        private String customerName;
        private Customer customer;
        private String user;
        private String userId;
        private String title;
        private String description;
        private String location;
        private String contact;
        private String type;
        private String url;
        private LocalDate sd;
        private LocalTime st;
        private LocalDateTime start;
        private ZonedDateTime zoneDTS;
        private LocalDate ed;
        private LocalTime et;
        private LocalDateTime end;
        private ZonedDateTime zoneDTE;
        private LocalDateTime createDate;
        private String createdBy;
        private LocalDateTime lastUpdate;
        private String lastUpdateBy;
        
    public Appointment(String appointmentId, int customerId, String customerName, String userId, String title, String description, String location, String contact, String type, String url, LocalDate sd, LocalTime st, LocalDateTime start, ZonedDateTime zoneDTS, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.zoneDTS = zoneDTS;
        this.end = end;
        this.zoneDTE = zoneDTE;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public Appointment(String appointmentId, int customerId, String customerName, String type, LocalDate sd, LocalTime st, LocalDateTime start, ZonedDateTime zoneDTS, LocalDate ed, LocalTime et, LocalDateTime end, ZonedDateTime zoneDTE) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.type = type;
        this.sd = sd;
        this.st = st;
        this.start = start;
        this.zoneDTS = zoneDTS;
        this.ed = ed;
        this.et = et;
        this.end = end;
        this.zoneDTE = zoneDTE;
    }
    

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getSd() {
        return sd;
    }

    public void setSd(LocalDate sd) {
        this.sd = sd;
    }

    public LocalTime getSt() {
        return st;
    }

    public void setSt(LocalTime st) {
        this.st = st;
    }


    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getZoneDTS() {
        return zoneDTS;
    }

    public void setZoneDTS(ZonedDateTime zoneDTS) {
        this.zoneDTS = zoneDTS;
    }

    public LocalDate getEd() {
        return ed;
    }

    public void setEd(LocalDate ed) {
        this.ed = ed;
    }

    public LocalTime getEt() {
        return et;
    }

    public void setEt(LocalTime et) {
        this.et = et;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public ZonedDateTime getZoneDTE() {
        return zoneDTE;
    }

    public void setZoneDTE(ZonedDateTime zoneDTE) {
        this.zoneDTE = zoneDTE;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
}
