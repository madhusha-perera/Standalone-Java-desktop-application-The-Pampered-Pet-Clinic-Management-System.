/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.*;
import java.util.*;
import model.Appointment;
import model.Owner;
import model.Pet;
import model.Veterinarian;
import controller.DBController;

public class ReceptionistController {

    public Appointment getAppointmentByCode(String code) {
        String sql = "SELECT a.appointment_code,a.datetime,a.reason,a.status,"
                   + "o.owner_code,p.pet_code,v.vet_code "
                   + "FROM appointment a "
                   + "JOIN owner o         ON a.owner_id=o.owner_id "
                   + "JOIN pet p           ON a.pet_id=p.pet_id "
                   + "JOIN veterinarian v  ON a.vet_id=v.vet_id "
                   + "WHERE a.appointment_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Appointment a = new Appointment();
                    a.setAppointmentCode(rs.getString("appointment_code"));
                    a.setDatetime(rs.getTimestamp("datetime"));
                    a.setReason(rs.getString("reason"));
                    a.setStatus(rs.getString("status"));
                    a.setOwnerCode(rs.getString("owner_code"));
                    a.setPetCode(rs.getString("pet_code"));
                    a.setVetCode(rs.getString("vet_code"));
                    return a;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Appointment> getAppointmentsByOwnerCode(String ownerCode) {
        List<Appointment> out = new ArrayList<>();
        String sql = "SELECT appointment_code,datetime,reason,status "
                   + "FROM appointment a "
                   + "JOIN owner o ON a.owner_id=o.owner_id "
                   + "WHERE o.owner_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment a = new Appointment();
                    a.setAppointmentCode(rs.getString("appointment_code"));
                    a.setDatetime(rs.getTimestamp("datetime"));
                    a.setReason(rs.getString("reason"));
                    a.setStatus(rs.getString("status"));
                    a.setOwnerCode(ownerCode);
                    out.add(a);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }

    public boolean createAppointment(String recCode,
                                     String ownerCode,
                                     String petCode,
                                     String vetCode,
                                     Timestamp when,
                                     String reason) {
        String sql = "INSERT INTO appointment "
                   + "(owner_id,pet_id,vet_id,receptionist_id,datetime,reason,status) VALUES ("
                   + "(SELECT owner_id FROM owner WHERE owner_code=?),"
                   + "(SELECT pet_id   FROM pet   WHERE pet_code  =?),"
                   + "(SELECT vet_id   FROM veterinarian WHERE vet_code=?),"
                   + "(SELECT receptionist_id FROM receptionist WHERE receptionist_code=?),"
                   + "?,?,'SCHEDULED')";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerCode);
            ps.setString(2, petCode);
            ps.setString(3, vetCode);
            ps.setString(4, recCode);
            ps.setTimestamp(5, when);
            ps.setString(6, reason);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateAppointment(String apptCode,
                                     String ownerCode,
                                     String petCode,
                                     String vetCode,
                                     Timestamp when,
                                     String reason,
                                     String status) {
        String sql = "UPDATE appointment SET "
                   + "owner_id=(SELECT owner_id FROM owner WHERE owner_code=?),"
                   + "pet_id=(SELECT pet_id FROM pet WHERE pet_code=?),"
                   + "vet_id=(SELECT vet_id FROM veterinarian WHERE vet_code=?),"
                   + "datetime=?,reason=?,status=? "
                   + "WHERE appointment_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerCode);
            ps.setString(2, petCode);
            ps.setString(3, vetCode);
            ps.setTimestamp(4, when);
            ps.setString(5, reason);
            ps.setString(6, status);
            ps.setString(7, apptCode);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean cancelAppointment(String apptCode) {
        String sql = "DELETE FROM appointment WHERE appointment_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, apptCode);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** List *all* pets. */
    public List<Pet> listAllPets() {
        List<Pet> pets = new ArrayList<>();
        String sql =
          "SELECT p.pet_code, p.name, p.species, p.breed, p.gender, p.dob, o.owner_code " +
          "FROM pet p " +
          "JOIN owner o ON p.owner_id = o.owner_id";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pet p = new Pet();
                p.setPetCode(   rs.getString("pet_code"));
                p.setName(      rs.getString("name"));
                p.setSpecies(   rs.getString("species"));
                p.setBreed(     rs.getString("breed"));
                p.setGender(    rs.getString("gender").charAt(0));
                p.setDob(       rs.getDate("dob"));
                p.setOwnerCode( rs.getString("owner_code"));
                pets.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    /** List pets for a given owner code. */
    public List<Pet> listPets(String ownerCode) {
        List<Pet> pets = new ArrayList<>();
        String sql =
          "SELECT p.pet_code, p.name, p.species, p.breed, p.gender, p.dob, o.owner_code " +
          "FROM pet p " +
          "JOIN owner o ON p.owner_id = o.owner_id " +
          "WHERE o.owner_code = ?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pet p = new Pet();
                    p.setPetCode(   rs.getString("pet_code"));
                    p.setName(      rs.getString("name"));
                    p.setSpecies(   rs.getString("species"));
                    p.setBreed(     rs.getString("breed"));
                    p.setGender(    rs.getString("gender").charAt(0));
                    p.setDob(       rs.getDate("dob"));
                    p.setOwnerCode( rs.getString("owner_code"));
                    pets.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    /** Find a single pet by its pet code. */
    public Pet getPetByCode(String petCode) {
        String sql =
          "SELECT p.pet_code, p.name, p.species, p.breed, p.gender, p.dob, o.owner_code " +
          "FROM pet p " +
          "JOIN owner o ON p.owner_id = o.owner_id " +
          "WHERE p.pet_code = ?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, petCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pet p = new Pet();
                    p.setPetCode(   rs.getString("pet_code"));
                    p.setName(      rs.getString("name"));
                    p.setSpecies(   rs.getString("species"));
                    p.setBreed(     rs.getString("breed"));
                    p.setGender(    rs.getString("gender").charAt(0));
                    p.setDob(       rs.getDate("dob"));
                    p.setOwnerCode( rs.getString("owner_code"));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean createPet(String ownerCode,
                         String name,
                         String species,
                         String breed,
                         char gender,
                         java.util.Date dob) {
    // First look up the owner_id from owner_code
    String findOwnerSql = "SELECT owner_id FROM owner WHERE owner_code = ?";
    String insertPetSql = 
      "INSERT INTO pet (pet_code, name, species, breed, gender, dob, owner_id) " +
      "VALUES (NULL, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DBController.createDBConnection();
         PreparedStatement psOwner = conn.prepareStatement(findOwnerSql)) {
        psOwner.setString(1, ownerCode);
        try (ResultSet rs = psOwner.executeQuery()) {
            if (!rs.next()) {
                // no such owner
                return false;
            }
            int ownerId = rs.getInt("owner_id");
            // now insert into pet
            try (PreparedStatement ps = conn.prepareStatement(insertPetSql)) {
                ps.setString(1, name);
                ps.setString(2, species);
                ps.setString(3, breed);
                ps.setString(4, String.valueOf(gender));
                // convert util.Date to sql.Date
                ps.setDate(5, new java.sql.Date(dob.getTime()));
                ps.setInt(6, ownerId);
                return ps.executeUpdate() == 1;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    public boolean updatePet(Pet p) {
        String sql = "UPDATE pet SET name=?,species=?,breed=?,gender=?,dob=?,"
                   + "owner_id=(SELECT owner_id FROM owner WHERE owner_code=?) "
                   + "WHERE pet_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getSpecies());
            ps.setString(3, p.getBreed());
            ps.setString(4, String.valueOf(p.getGender()));
            ps.setDate(5, new java.sql.Date(p.getDob().getTime()));
            ps.setString(6, p.getOwnerCode());
            ps.setString(7, p.getPetCode());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deletePet(String petCode) {
        String sql = "DELETE FROM pet WHERE pet_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, petCode);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public Owner getOwnerByCode(String ownerCode) {
        String sql = "SELECT owner_code,username,password,fname,lname,email,address,phone_number "
                   + "FROM owner WHERE owner_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Owner o = new Owner();
                    o.setOwnerCode(rs.getString("owner_code"));
                    o.setUsername(rs.getString("username"));
                    o.setPassword(rs.getString("password"));
                    o.setFname(rs.getString("fname"));
                    o.setLname(rs.getString("lname"));
                    o.setEmail(rs.getString("email"));
                    o.setAddress(rs.getString("address"));
                    o.setPhoneNumber(rs.getString("phone_number"));
                    return o;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean createOwner(Owner o) {
        String sql = "INSERT INTO owner "
                   + "(username,password,fname,lname,email,address,phone_number) "
                   + "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getUsername());
            ps.setString(2, o.getPassword());
            ps.setString(3, o.getFname());
            ps.setString(4, o.getLname());
            ps.setString(5, o.getEmail());
            ps.setString(6, o.getAddress());
            ps.setString(7, o.getPhoneNumber()); 
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
        
        
     }    private boolean signupOwner(Owner o) {
        return new OwnerController().signupOwner(
            o.getUsername(), o.getPassword(),
            o.getFname(), o.getLname(),
            o.getEmail(), o.getAddress(),
            o.getPhoneNumber()
        );
    }

    public boolean updateOwner(Owner o) {
        String sql = "UPDATE owner SET username=?,password=?,fname=?,lname=?,"
                   + "email=?,address=?,phone_number=? WHERE owner_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getUsername());
            ps.setString(2, o.getPassword());
            ps.setString(3, o.getFname());
            ps.setString(4, o.getLname());
            ps.setString(5, o.getEmail());
            ps.setString(6, o.getAddress());
            ps.setString(7, o.getPhoneNumber());
            ps.setString(8, o.getOwnerCode());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteOwner(String ownerCode) {
        String sql = "DELETE FROM owner WHERE owner_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerCode);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public Veterinarian getVetByCode(String vetCode) {
        String sql = "SELECT vet_code,username,password,fname,lname,"
                   + "specialization,phone_number,dob,address "
                   + "FROM veterinarian WHERE vet_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vetCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Veterinarian v = new Veterinarian();
                    v.setVetCode(rs.getString("vet_code"));
                    v.setUsername(rs.getString("username"));
                    v.setPassword(rs.getString("password"));
                    v.setFname(rs.getString("fname"));
                    v.setLname(rs.getString("lname"));
                    v.setSpecialization(rs.getString("specialization"));
                    v.setPhoneNumber(rs.getString("phone_number"));
                    v.setDob(rs.getDate("dob"));
                    v.setAddress(rs.getString("address"));
                    return v;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean createVet(Veterinarian v) {
        String sql = "INSERT INTO veterinarian "
                   + "(username,password,fname,lname,specialization,phone_number,dob,address) "
                   + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getUsername());
            ps.setString(2, v.getPassword());
            ps.setString(3, v.getFname());
            ps.setString(4, v.getLname());
            ps.setString(5, v.getSpecialization());
            ps.setString(6, v.getPhoneNumber());
            ps.setDate(7, new java.sql.Date(v.getDob().getTime()));
            ps.setString(8, v.getAddress());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateVet(Veterinarian v) {
        String sql = "UPDATE veterinarian SET username=?,password=?,fname=?,lname=?,"
                   + "specialization=?,phone_number=?,dob=?,address=? "
                   + "WHERE vet_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getUsername());
            ps.setString(2, v.getPassword());
            ps.setString(3, v.getFname());
            ps.setString(4, v.getLname());
            ps.setString(5, v.getSpecialization());
            ps.setString(6, v.getPhoneNumber());
            ps.setDate(7, new java.sql.Date(v.getDob().getTime()));
            ps.setString(8, v.getAddress());
            ps.setString(9, v.getVetCode());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteVet(String vetCode) {
        String sql = "DELETE FROM veterinarian WHERE vet_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vetCode);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}