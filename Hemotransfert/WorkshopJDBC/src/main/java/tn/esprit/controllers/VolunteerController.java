package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.models.Volunteer;
import tn.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VolunteerController implements CRUD_Volunteer<Volunteer> {

    private Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void insertVolunteer(Volunteer volunteer) throws SQLException {
        String req = "INSERT INTO volunteer (name, address, phone, mail, dispo, profession, company_idc) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setString(1, volunteer.getName());
            preparedStatement.setString(2, volunteer.getAddress());
            preparedStatement.setInt(3, volunteer.getPhone());
            preparedStatement.setString(4, volunteer.getMail());
            preparedStatement.setString(5, volunteer.getDispo());
            preparedStatement.setString(6, volunteer.getProfession());
            preparedStatement.setInt(7, volunteer.getCompany_id());

            preparedStatement.executeUpdate();
            System.out.println("Volunteer added successfully!");
        }
    }

    @Override
    public List<Volunteer> affVolunteer() throws SQLException {
        List<Volunteer> volunteerList = new ArrayList<>();
        String req = "SELECT v.*, c.name AS company_name FROM volunteer v LEFT JOIN company c ON v.company_idc = c.id";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Volunteer volunteer = new Volunteer();
                volunteer.setId(rs.getInt("id"));
                volunteer.setName(rs.getString("name"));
                volunteer.setAddress(rs.getString("address"));
                volunteer.setPhone(rs.getInt("phone"));
                volunteer.setMail(rs.getString("mail"));
                volunteer.setDispo(rs.getString("dispo"));
                volunteer.setProfession(rs.getString("profession"));

                int companyId = rs.getInt("company_idc");
                volunteer.setCompany_id(companyId);

                volunteerList.add(volunteer);
            }
        }
        return volunteerList;
    }


    public String getCompanyName(int companyId) throws SQLException {
        String query = "SELECT name FROM company WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, companyId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                }
            }
        }
        return null;
    }

    @Override
    public void updateVolunteer(Volunteer volunteer) throws SQLException {
        String req = "UPDATE volunteer SET name=?, address=?, phone=?, mail=?, dispo=?, profession=?, company_idc=? WHERE id=?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setString(1, volunteer.getName());
            preparedStatement.setString(2, volunteer.getAddress());
            preparedStatement.setInt(3, volunteer.getPhone());
            preparedStatement.setString(4, volunteer.getMail());
            preparedStatement.setString(5, volunteer.getDispo());
            preparedStatement.setString(6, volunteer.getProfession());
            preparedStatement.setInt(7, volunteer.getCompany_id());
            preparedStatement.setInt(8, volunteer.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Volunteer updated successfully!");
            } else {
                System.out.println("No volunteer found with ID: " + volunteer.getId());
            }
        }
    }

    @Override
    public void deleteVolunteer(Volunteer volunteer) throws SQLException {
        String query = "DELETE FROM volunteer WHERE id=?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, volunteer.getId());
            preparedStatement.executeUpdate();
            System.out.println("Volunteer deleted successfully!");
        }
    }

    public ObservableList<Volunteer> sortVolunteerBy(String criteria) throws SQLException {

        String query = "SELECT * FROM volunteer ORDER BY " + criteria ;
        try (Statement st = cnx.createStatement(); ResultSet resultSet = st.executeQuery(query)) {
            ObservableList<Volunteer> volunteerList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Volunteer volunteer = new Volunteer();
                volunteer.setId(resultSet.getInt("id"));
                volunteer.setName(resultSet.getString("name"));
                volunteer.setAddress(resultSet.getString("address"));
                volunteer.setPhone(resultSet.getInt("phone"));
                volunteer.setMail(resultSet.getString("mail"));
                volunteer.setDispo(resultSet.getString("dispo"));
                volunteer.setProfession(resultSet.getString("profession"));
                int companyId = resultSet.getInt("company_idc");
                volunteer.setCompany_id(companyId);
                volunteerList.add(volunteer);
            }
            return volunteerList;
        }
    }

    public boolean volunteerExistsByPhone(int volunteerPhone) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM volunteer WHERE phone = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, volunteerPhone);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
}
