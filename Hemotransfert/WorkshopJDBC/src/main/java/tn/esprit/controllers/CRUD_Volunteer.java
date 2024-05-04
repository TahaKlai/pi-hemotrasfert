package tn.esprit.controllers;

import tn.esprit.models.Volunteer;

import java.sql.SQLException;
import java.util.List;
public interface CRUD_Volunteer<T> {



    void insertVolunteer(Volunteer volunteer) throws SQLException;

    void updateVolunteer(Volunteer volunteer) throws SQLException;

    List<Volunteer> affVolunteer() throws SQLException;

    void deleteVolunteer(Volunteer volunteer) throws SQLException;
}
