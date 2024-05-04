package tn.esprit.controllers;


import tn.esprit.models.Company;
import java.sql.SQLException;
import java.util.List;
public interface CRUD_Company<T> {

    void insertOne(T t) throws SQLException;
    void updateOne(T t) throws SQLException;
    void deleteOne(T t) throws SQLException;
    List<T> afficher() throws SQLException;


}
