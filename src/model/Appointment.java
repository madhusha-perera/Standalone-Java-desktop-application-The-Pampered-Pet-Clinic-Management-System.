/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import java.util.Date;

public class Appointment {
    private String appointmentCode, ownerCode, petCode, vetCode, status, reason;
    private Date   datetime;

    public String getAppointmentCode() { return appointmentCode; }
    public void setAppointmentCode(String c) { this.appointmentCode = c; }

    public String getOwnerCode() { return ownerCode; }
    public void setOwnerCode(String o) { this.ownerCode = o; }

    public String getPetCode() { return petCode; }
    public void setPetCode(String p) { this.petCode = p; }

    public String getVetCode() { return vetCode; }
    public void setVetCode(String v) { this.vetCode = v; }

    public Date getDatetime() { return datetime; }
    public void setDatetime(Date d) { this.datetime = d; }

    public String getReason() { return reason; }
    public void setReason(String r) { this.reason = r; }

    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
}