/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBController {
    private static final String URL =
        "jdbc:mysql://localhost:3306/pamperedpetclinicdb"
      + "?useSSL=false&serverTimezone=UTC";
    private static final String USER = "";
    private static final String PWD  = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL Driver not found: " + e);
        }
    }

    public static Connection createDBConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }
}