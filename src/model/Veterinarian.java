/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import java.util.Date;

public class Veterinarian {
    private String vetCode, username, password, fname, lname, specialization,
                   phoneNumber, address;
    private Date   dob;

    public String getVetCode() { return vetCode; }
    public void setVetCode(String c) { this.vetCode = c; }

    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }

    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }

    public String getFname() { return fname; }
    public void setFname(String f) { this.fname = f; }

    public String getLname() { return lname; }
    public void setLname(String l) { this.lname = l; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String s) { this.specialization = s; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String p) { this.phoneNumber = p; }

    public Date getDob() { return dob; }
    public void setDob(Date d) { this.dob = d; }

    public String getAddress() { return address; }
    public void setAddress(String a) { this.address = a; }
}
