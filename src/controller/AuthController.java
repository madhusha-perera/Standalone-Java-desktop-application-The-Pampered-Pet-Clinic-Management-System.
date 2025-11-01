package controller;

import java.sql.*;
import model.Owner;
import model.Veterinarian;
import model.Receptionist;

public class AuthController {
    
    public static class LoginResult {
        
        private final String userCode, role;
        
        public LoginResult(String userCode, String role) {
            this.userCode = userCode;
            this.role     = role;
        }
        
        public String getUserCode() { return userCode; }
        public String getRole()     { return role;     }
    }

    /** 
     * Returns LoginResult with code+role, or null if bad credentials.
     */
    public LoginResult login(String userOrEmail, String password) {
        try (Connection conn = DBController.createDBConnection()) {
            // --- Owner ---
            String sqlO = "SELECT owner_code,password FROM owner WHERE username=? OR email=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlO)) {
                ps.setString(1, userOrEmail);
                ps.setString(2, userOrEmail);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getString("password").equals(password)) {
                        return new LoginResult(rs.getString("owner_code"), "OWNER");
                    }
                }
            }
            // --- Veterinarian ---
            String sqlV = "SELECT vet_code,password FROM veterinarian WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlV)) {
                ps.setString(1, userOrEmail);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getString("password").equals(password)) {
                        return new LoginResult(rs.getString("vet_code"), "VETERINARIAN");
                    }
                }
            }
            // --- Receptionist (incl. head) ---
            String sqlR = "SELECT receptionist_code,password,role FROM receptionist WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlR)) {
                ps.setString(1, userOrEmail);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getString("password").equals(password)) {
                        return new LoginResult(
                            rs.getString("receptionist_code"),
                            rs.getString("role")  // "RECEPTIONIST" or "HEAD_RECEPTIONIST"
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
