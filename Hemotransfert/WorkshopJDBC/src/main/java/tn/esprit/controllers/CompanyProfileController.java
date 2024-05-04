package tn.esprit.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.models.Company;
import tn.esprit.controllers.CompanyController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CompanyProfileController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField websiteField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField descriptionField;



    @FXML
    private Button btnOverview;



    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Button showcompaniesb;
    @FXML







    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert companyNameField != null : "fx:id=\"companyNameField\" was not injected: check your FXML file 'Company.fxml'.";
        assert countryField != null : "fx:id=\"countryField\" was not injected: check your FXML file 'Company.fxml'.";
        assert websiteField != null : "fx:id=\"websiteField\" was not injected: check your FXML file 'Company.fxml'.";
        assert emailField != null : "fx:id=\"emailField\" was not injected: check your FXML file 'Company.fxml'.";
        assert phoneField != null : "fx:id=\"phoneField\" was not injected: check your FXML file 'Company.fxml'.";
        assert addressField != null : "fx:id=\"addressField\" was not injected: check your FXML file 'Company.fxml'.";
        assert descriptionField != null : "fx:id=\"descriptionField\" was not injected: check your FXML file 'Company.fxml'.";
    }




    @FXML
    void addCompany(ActionEvent event) {
        String companyName = companyNameField.getText();
        String country = countryField.getText();
        String website = websiteField.getText();
        String email = emailField.getText();
        String phoneText = phoneField.getText();
        String address = addressField.getText();
        String description = descriptionField.getText();

        if (companyName.isEmpty() || country.isEmpty() || website.isEmpty() || email.isEmpty() || phoneText.isEmpty() || address.isEmpty() || description.isEmpty()) {
            showErrorAlert("Error", "Please fill in all fields.");
            return;
        }
        if (!isValidWebsite(website)) {
            showErrorAlert("Error", "Please enter a valid website address.");
            return;
        }

        if (!isValidEmail(email)) {
            showErrorAlert("Error", "Please enter a valid email address.");
            return;
        }

        if (description.length() < 10) {
            showErrorAlert("Error", "Description must be at least 10 characters long.");
            return;
        }

        if (country.length() < 3) {
            showErrorAlert("Error", "Country name must be at least 3 characters long.");
            return;
        }

        if (!isValidPhoneNumber(phoneText)) {
            showErrorAlert("Error", "Phone number must contain only numbers.");
            return;
        }


        CompanyController companyController = new CompanyController();
        boolean exists = false;
        try {
            exists = companyController.companyExistsByName(companyName);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (exists) {
            showErrorAlert("Error", "A company with this name already exists .");
            return;
        }

        Company company = new Company(
                0,
                companyName,
                country,
                "https://" + website,
                email,
                Integer.parseInt(phoneText),
                address,
                description
        );

        try {
            companyController.insertOne(company);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("Company added successfully!");
        alert.show();
    }

    @FXML
    void Showcompanies(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/companymanager.fxml"));
        Parent parent = loader.load();
        Stage stage = (Stage) showcompaniesb.getScene().getWindow();
        Scene scene = new Scene(parent);

        stage.setTitle("Show companies");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void gotocommentaire(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionAbon.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading gestionAbon.fxml: " + e.getMessage());
        }
    }

    @FXML
    void Showvolunteer(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Volunteer.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading gestionAbon.fxml: " + e.getMessage());
        }
    }

    @FXML
    void gotoactualite(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionCat.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading gestionAbon.fxml: " + e.getMessage());
        }
    }

    @FXML
    void ShowCompany(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Company.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading Company.fxml: " + e.getMessage());
        }
    }

    @FXML
    void Showcompany(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/company.fxml"));
        Parent parent = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);

        stage.setTitle("Add Company");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void Deconnection(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Close the stage (which effectively closes the scene)
        stage.close();
    }



    private boolean isValidEmail(String email) {

        return email.contains("@");
    }

    private boolean isValidWebsite(String website) {

        return website.matches("^\\w+(\\.\\w+)+$");

    }

    private boolean isValidPhoneNumber(String phone) {

        return Pattern.matches("\\d+", phone);
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    void gotoStock(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Stock.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading Stock.fxml: " + e.getMessage());
        }
    }
}

