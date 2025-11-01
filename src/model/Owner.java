/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Owner {
    private int    ownerId;
    private String ownerCode, username, password;
    private String fname, lname, email, address, phoneNumber;

    
    public Owner() {
}
    
    public Owner(int ownerId, String ownerCode, String username, String password,
             String fname, String lname, String email, String address, String phoneNumber) {
    this.ownerId = ownerId;
    this.ownerCode = ownerCode;
    this.username = username;
    this.password = password;
    this.fname = fname;
    this.lname = lname;
    this.email = email;
    this.address = address;
    this.phoneNumber = phoneNumber;
}
    
    
    
    public Owner(String username, String password,
             String fname, String lname, String email, String address, String phoneNumber) {
    this.username = username;
    this.password = password;
    this.fname = fname;
    this.lname = lname;
    this.email = email;
    this.address = address;
    this.phoneNumber = phoneNumber;
}
    
    
    public String getOwnerCode() { return ownerCode; }
    public void setOwnerCode(String ownerCode) { this.ownerCode = ownerCode; }

    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; } 

    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }

    public String getFname() { return fname; }
    public void setFname(String f) { this.fname = f; }

    public String getLname() { return lname; }
    public void setLname(String l) { this.lname = l; }

    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }

    public String getAddress() { return address; }
    public void setAddress(String a) { this.address = a; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String p) { this.phoneNumber = p; }
}