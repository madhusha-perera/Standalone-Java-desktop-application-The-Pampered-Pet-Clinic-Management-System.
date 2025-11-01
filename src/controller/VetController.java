/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.*;
import java.util.*;
import model.Appointment;
import model.MedicalRecord;
import model.Pet;
import controller.DBController;

public class VetController {

    public List<Appointment> listAppointments(String vetCode) {
        List<Appointment> out = new ArrayList<>();
        String sql = "SELECT a.appointment_code,a.datetime,a.reason,a.status,"
                   + "o.owner_code,p.pet_code "
                   + "FROM appointment a "
                   + "JOIN veterinarian v ON a.vet_id=v.vet_id "
                   + "JOIN owner o         ON a.owner_id=o.owner_id "
                   + "JOIN pet p           ON a.pet_id=p.pet_id "
                   + "WHERE v.vet_code=? AND a.status='SCHEDULED' "
                   + "ORDER BY a.datetime";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vetCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment a = new Appointment();
                    a.setAppointmentCode(rs.getString("appointment_code"));
                    a.setDatetime(rs.getTimestamp("datetime"));
                    a.setReason(rs.getString("reason"));
                    a.setStatus(rs.getString("status"));
                    a.setOwnerCode(rs.getString("owner_code"));
                    a.setPetCode(rs.getString("pet_code"));
                    out.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    public Pet searchPet(String petCode) {
        return new OwnerController().getPetByCode(petCode);
    }

    public List<MedicalRecord> listMedicalRecords(String petCode) {
        List<MedicalRecord> out = new ArrayList<>();
        String sql = 
            "SELECT r.record_code, r.visit_date, r.diagnosis, " +
            "       r.treatment, r.medication, v.vet_code " +
            "  FROM petmedicalrecord r " +
            "  JOIN pet p          ON r.pet_id = p.pet_id " +
            "  JOIN veterinarian v ON r.vet_id = v.vet_id " +
            " WHERE p.pet_code = ? " +
            " ORDER BY r.visit_date DESC";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public boolean addMedicalRecord(String vetCode, String petCode,
                                    java.util.Date visitDate,
                                    String diagnosis,
                                    String treatment,
                                    String medication) {
        String sql = "INSERT INTO petmedicalrecord "
                   + "(vet_id,pet_id,visit_date,diagnosis,treatment,medication) VALUES ("
                   + "(SELECT vet_id FROM veterinarian WHERE vet_code=?),"
                   + "(SELECT pet_id FROM pet WHERE pet_code=?),"
                   + "?,?,?,?)";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vetCode);
            ps.setString(2, petCode);
            ps.setDate(3, new java.sql.Date(visitDate.getTime()));
            ps.setString(4, diagnosis);
            ps.setString(5, treatment);
            ps.setString(6, medication);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}