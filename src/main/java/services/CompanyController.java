package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Company;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyController implements CRUD_Company<Company> {

    private Connection cnx = DBConnection.getInstance().getCnx();
    @Override
    public List<Company> afficher() throws SQLException {
        List<Company> companyList = new ArrayList<>();
        String req = "SELECT * FROM company";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Company company = new Company();
                company.setId(rs.getInt("id"));
                company.setName(rs.getString("name"));
                company.setCountry(rs.getString("country"));
                company.setWebsite(rs.getString("website"));
                company.setMail(rs.getString("mail"));
                company.setPhone(rs.getInt("phone"));
                company.setAddress(rs.getString("address"));
                company.setDescription(rs.getString("description"));

                companyList.add(company);
            }
        }
        return companyList;
    }
    @Override
    public void insertOne(Company company) throws SQLException {
        String req = "INSERT INTO company (name, country, website, mail, phone, address, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getCountry());
            preparedStatement.setString(3, company.getWebsite());
            preparedStatement.setString(4, company.getMail());
            preparedStatement.setInt(5, company.getPhone());
            preparedStatement.setString(6, company.getAddress());
            preparedStatement.setString(7, company.getDescription());

            preparedStatement.executeUpdate();
            System.out.println("Company added successfully!");
        }
    }

    @Override
    public void updateOne(Company company) throws SQLException {
        String req = "UPDATE company SET name=?, country=?, website=?, mail=?, phone=?, address=?, description=? WHERE id=?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getCountry());
            preparedStatement.setString(3, company.getWebsite());
            preparedStatement.setString(4, company.getMail());
            preparedStatement.setInt(5, company.getPhone());
            preparedStatement.setString(6, company.getAddress());
            preparedStatement.setString(7, company.getDescription());
            preparedStatement.setInt(8, company.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Company updated successfully!");
            } else {
                System.out.println("No company found with ID: " + company.getId());
            }
        }
    }





    @Override
    public void deleteOne(Company company) throws SQLException {
        String query = "DELETE FROM company WHERE id=?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, company.getId());
            preparedStatement.executeUpdate();
            System.out.println("Company deleted successfully!");
        }
    }

    public ObservableList<Company> tri(String criteria) throws SQLException {

        String query = "SELECT * FROM company ORDER BY " + criteria ;
        try (Statement st = cnx.createStatement(); ResultSet resultSet = st.executeQuery(query)) {
            ObservableList<Company> companiesList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Company company = new Company();
                company.setName(resultSet.getString("name"));
                company.setCountry(resultSet.getString("country"));
                company.setWebsite(resultSet.getString("website"));
                company.setMail(resultSet.getString("mail"));
                company.setPhone(resultSet.getInt("phone"));
                company.setAddress(resultSet.getString("address"));
                company.setDescription(resultSet.getString("description"));
                companiesList.add(company);
            }
            return companiesList;
        }
    }

    public boolean companyExistsByName(String companyName) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM company WHERE name = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, companyName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }

    public List<String> getAllCompanyNames() {
        List<String> companyNames = new ArrayList<>();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            String query = "SELECT name FROM company";


            statement = cnx.prepareStatement(query);
            resultSet = statement.executeQuery();


            while (resultSet.next()) {
                String companyName = resultSet.getString("name");
                companyNames.add(companyName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return companyNames;
    }


    public int getCompanyIdByName(String companyName) throws SQLException {
        int companyId = -1;
        String query = "SELECT id FROM company WHERE name = ?";

        try (PreparedStatement preparedStatement = DBConnection.getInstance().getCnx().prepareStatement(query)) {
            preparedStatement.setString(1, companyName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    companyId = resultSet.getInt("id");
                }
            }
        }

        return companyId;
    }


}
