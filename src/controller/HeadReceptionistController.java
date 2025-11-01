/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.*;
import model.Receptionist;
import controller.DBController;

public class HeadReceptionistController extends ReceptionistController {

    private boolean isHeadReceptionist(String recCode) {
        String sql = "SELECT role FROM receptionist WHERE receptionist_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && "HEAD_RECEPTIONIST".equals(rs.getString("role"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    
    public Receptionist getRecByCode(String recCode) {
        String sql = "SELECT receptionist_code,username,password,fname,lname,role,"
                   + "phone_number,address,dob FROM receptionist WHERE receptionist_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Receptionist r = new Receptionist();
                    r.setReceptionistCode(rs.getString("receptionist_code"));
                    r.setUsername(rs.getString("username"));
                    r.setPassword(rs.getString("password"));
                    r.setFname(rs.getString("fname"));
                    r.setLname(rs.getString("lname"));
                    r.setRole(rs.getString("role"));
                    r.setPhoneNumber(rs.getString("phone_number"));
                    r.setAddress(rs.getString("address"));
                    r.setDob(rs.getDate("dob"));
                    return r;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean createReceptionist(String currentRecCode, Receptionist r) {
        if (!isHeadReceptionist(currentRecCode)) return false;
        String sql = "INSERT INTO receptionist "
                   + "(username,password,fname,lname,role,phone_number,address,dob) "
                   + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getUsername());
            ps.setString(2, r.getPassword());
            ps.setString(3, r.getFname());
            ps.setString(4, r.getLname());
            ps.setString(5, r.getRole());
            ps.setString(6, r.getPhoneNumber());
            ps.setString(7, r.getAddress());
            ps.setDate(8, new java.sql.Date(r.getDob().getTime()));
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateReceptionist(String currentRecCode, Receptionist r) {
        if (!isHeadReceptionist(currentRecCode)) return false;
        String sql = "UPDATE receptionist SET username=?,password=?,fname=?,lname=?,"
                   + "role=?,phone_number=?,address=?,dob=? WHERE receptionist_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getUsername());
            ps.setString(2, r.getPassword());
            ps.setString(3, r.getFname());
            ps.setString(4, r.getLname());
            ps.setString(5, r.getRole());
            ps.setString(6, r.getPhoneNumber());
            ps.setString(7, r.getAddress());
            ps.setDate(8, new java.sql.Date(r.getDob().getTime()));
            ps.setString(9, r.getReceptionistCode());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteReceptionist(String currentRecCode, String recCodeToDelete) {
        if (!isHeadReceptionist(currentRecCode)) return false;
        String sql = "DELETE FROM receptionist WHERE receptionist_code=?";
        try (Connection conn = DBController.createDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recCodeToDelete);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}