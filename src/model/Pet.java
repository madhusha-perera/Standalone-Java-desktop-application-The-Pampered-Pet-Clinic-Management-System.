/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class Pet {
    private int    petId;
    private String petCode, name, species, breed, ownerCode;
    private char   gender;
    private Date   dob;
    
    public Pet() {
}
    
    public Pet(int petId, String petCode, String name, String species, String breed,
           String ownerCode, char gender, Date dob) {
    this.petId = petId;
    this.petCode = petCode;
    this.name = name;
    this.species = species;
    this.breed = breed;
    this.ownerCode = ownerCode;
    this.gender = gender;
    this.dob = dob;
}

    
    
    public Pet(String petCode, String name, String species, String breed,
           String ownerCode, char gender, Date dob) {
    this.petCode = petCode;
    this.name = name;
    this.species = species;
    this.breed = breed;
    this.ownerCode = ownerCode;
    this.gender = gender;
    this.dob = dob;
}
    
    

    public String getPetCode() { return petCode; }
    public void setPetCode(String c) { this.petCode = c; }

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }

    public String getSpecies() { return species; }
    public void setSpecies(String s) { this.species = s; }

    public String getBreed() { return breed; }
    public void setBreed(String b) { this.breed = b; }

    public char getGender() { return gender; }
    public void setGender(char g) { this.gender = g; }

    public Date getDob() { return dob; }
    public void setDob(Date d) { this.dob = d; }

    public String getOwnerCode() { return ownerCode; }
    public void setOwnerCode(String o) { this.ownerCode = o; }
}
