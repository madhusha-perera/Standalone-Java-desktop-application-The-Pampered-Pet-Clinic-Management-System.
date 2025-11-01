/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.*;
import java.util.*;
import model.Pet;
import model.Owner;
import model.MedicalRecord;
import controller.DBController;

public class OwnerController {
     public boolean createOwner(Owner o) {
        String sql = ""
          + "INSERT INTO owner "
          + "  (username, password, fname, lname, email, phone_number, address) "
          + "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getUsername());
            ps.setString(2, o.getPassword());
            ps.setString(3, o.getFname());
            ps.setString(4, o.getLname());
            ps.setString(5, o.getEmail());
            ps.setString(6, o.getPhoneNumber());
            ps.setString(7, o.getAddress());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
     }
     
     public boolean signupOwner(String username, String password,
                               String fname, String lname,
                               String email, String address,
                               String phoneNumber) {
        String sql = "INSERT INTO owner "
                   + "(username,password,fname,lname,email,address,phone_number) "
                   + "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fname);
            ps.setString(4, lname);
            ps.setString(5, email);
            ps.setString(6, address);
            ps.setString(7, phoneNumber);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
     }
    public boolean registerPet(String ownerCode,
                               String name, String species,
                               String breed, char gender,
                               java.sql.Date dob) {
        String sql = "INSERT INTO pet "
                   + "(owner_id,name,species,breed,gender,dob) VALUES ("
                   + "(SELECT owner_id FROM owner WHERE owner_code=?),"
                   + "?,?,?,?,?)";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerCode);
            ps.setString(2, name);
            ps.setString(3, species);
            ps.setString(4, breed);
            ps.setString(5, String.valueOf(gender));
            ps.setDate(6, dob);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean requestAppointment(String ownerCode,
                                      String petCode,
                                      Timestamp when,
                                      String reason) {
        String sql = "INSERT INTO appointment "
                   + "(owner_id,pet_id,datetime,reason) VALUES ("
                   + "(SELECT owner_id FROM owner WHERE owner_code=?),"
                   + "(SELECT pet_id   FROM pet   WHERE pet_code  =?),"
                   + "?,?)";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerCode);
            ps.setString(2, petCode);
            ps.setTimestamp(3, when);
            ps.setString(4, reason);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Pet> listPets(String ownerCode) {
        List<Pet> out = new ArrayList<>();
        String sql = "SELECT p.pet_code,p.name,p.species,p.breed,p.gender,p.dob "
                   + "FROM pet p JOIN owner o ON p.owner_id=o.owner_id "
                   + "WHERE o.owner_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pet p = new Pet();
                    p.setPetCode(rs.getString("pet_code"));
                    p.setName(rs.getString("name"));
                    p.setSpecies(rs.getString("species"));
                    p.setBreed(rs.getString("breed"));
                    p.setGender(rs.getString("gender").charAt(0));
                    p.setDob(rs.getDate("dob"));
                    out.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    public Pet getPetByCode(String petCode) {
        String sql = "SELECT p.pet_code,p.name,p.species,p.breed,p.gender,p.dob,"
                   + "o.owner_code "
                   + "FROM pet p JOIN owner o ON p.owner_id=o.owner_id "
                   + "WHERE p.pet_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, petCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pet p = new Pet();
                    p.setPetCode(rs.getString("pet_code"));
                    p.setName(rs.getString("name"));
                    p.setSpecies(rs.getString("species"));
                    p.setBreed(rs.getString("breed"));
                    p.setGender(rs.getString("gender").charAt(0));
                    p.setDob(rs.getDate("dob"));
                    p.setOwnerCode(rs.getString("owner_code"));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<MedicalRecord> viewMedicalHistory(String petCode) {
        List<MedicalRecord> out = new ArrayList<>();
        String sql =
            "SELECT r.record_code, r.visit_date, r.diagnosis, " +
            "       r.treatment, r.medication, v.vet_code " +
            "  FROM petmedicalrecord r " +
            "  JOIN pet p          ON r.pet_id = p.pet_id " +
            "  JOIN veterinarian v ON r.vet_id = v.vet_id " +
            " WHERE p.pet_code = ? " +
            " ORDER BY r.visit_date DESC";
        try (
            Connection conn = DBController.createDBConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, petCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicalRecord m = new MedicalRecord();
                    m.setRecordCode( rs.getString("record_code") );
                    m.setVisitDate(  rs.getDate  ("visit_date"  ) );
                    m.setDiagnosis(  rs.getString("diagnosis"  ) );
                    m.setTreatment(  rs.getString("treatment"  ) );
                    m.setMedication( rs.getString("medication" ) );
                    m.setVetCode(    rs.getString("vet_code"   ) );
                    out.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }
}