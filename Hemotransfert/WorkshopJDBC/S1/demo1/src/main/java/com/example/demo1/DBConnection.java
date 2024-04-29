package com.example.demo1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static  String user="root";
    static  String password="";
    static  String url="jdbc:mysql://localhost:3306/projettest";
    static  String driver="com.mysql.cj.jdbc.Driver";
    private Connection con;
    private static DBConnection instance;
    private DBConnection() {

            try {
                con = DriverManager.getConnection(url,user,password);
                System.out.println("Connected To DATABASE !");
            } catch (SQLException e) {
                System.err.println("Error: "+e.getMessage());
            }
        }
    public static DBConnection getInstance(){
        if (instance == null) instance = new DBConnection();
        return instance;
    }



    public  Connection getCnx() {
        return con;
    }
}
