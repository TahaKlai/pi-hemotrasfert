package utils;

import com.mysql.cj.jdbc.ConnectionGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/projettest";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    private static DBConnection instance;

    private Connection cnx;

    private DBConnection() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected To DATABASE !");
        } catch (SQLException e) {
            System.err.println("Error: "+e.getMessage());
        }
    }


    public static DBConnection getInstance(){
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
