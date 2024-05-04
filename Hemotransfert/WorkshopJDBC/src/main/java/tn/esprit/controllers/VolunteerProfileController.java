package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.models.Volunteer;


import java.sql.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.regex.Pattern;
import tn.esprit.utils.DataSource;
public class VolunteerProfileController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField volunteerNameTextField;

    @FXML
    private TextField volunteerAddressTextField;

    @FXML
    private TextField volunteerPhoneTextField;

    @FXML
    private TextField volunteerEmailTextField;

    @FXML
    private ChoiceBox<String> volunteerAvailabilityComboBox;

    @FXML
    private TextField volunteerProfessionTextField;



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
    private Button showVolunteersButton;
    @FXML
    private AnchorPane chartContainer;
    @FXML
    private Button statComboBox;
    @FXML
    private Button Showvolunteers;
    @FXML
    private ChoiceBox<String> companies;


    @FXML
    private void CompanyNames() {
        CompanyController companyService = new CompanyController();
        List<String> companyNames = companyService.getAllCompanyNames();
        companies.getItems().clear();
        companies.getItems().addAll(companyNames);
    }
    @FXML
    void addVolunteer(ActionEvent event) throws SQLException {
        String volunteerName = volunteerNameTextField.getText();
        String volunteerAddress = volunteerAddressTextField.getText();
        String volunteerPhone = volunteerPhoneTextField.getText();
        String volunteerMail = volunteerEmailTextField.getText();
        String volunteerAvailability = volunteerAvailabilityComboBox.getValue();
        String volunteerProfession = volunteerProfessionTextField.getText();

        String selectedCompanyName = companies.getValue();

        // Check if all fields are filled
        if (volunteerName.isEmpty() || volunteerAddress.isEmpty() || volunteerPhone.isEmpty() || volunteerMail.isEmpty() || volunteerAvailability == null || volunteerProfession.isEmpty() || selectedCompanyName == null) {
            showErrorAlert("Error", "Please fill in all fields.");
            return;
        }

        // Validate phone number format
        if (!isValidPhoneNumber(volunteerPhone)) {
            showErrorAlert("Error", "Phone number must contain only numbers.");
            return;
        }

        // Validate email format
        if (!isValidEmail(volunteerMail)) {
            showErrorAlert("Error", "Please enter a valid email address.");
            return;
        }

        VolunteerController volunteerController = new VolunteerController();
        boolean exists = volunteerController.volunteerExistsByPhone(Integer.parseInt(volunteerPhone));
        if (exists) {
            showErrorAlert("Error", "The Volunteer already exists.");
            return;
        }


        CompanyController companyController = new CompanyController();
        int company_idc = companyController.getCompanyIdByName(selectedCompanyName);




        Volunteer volunteer = new Volunteer(
                0,
                volunteerName,
                volunteerAddress,
                Integer.parseInt(volunteerPhone),
                volunteerMail,
                volunteerAvailability,
                volunteerProfession,
                company_idc
        );


        try {
            volunteerController.insertVolunteer(volunteer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Send email notification
        String username = "mahjoubisamir060@gmail.com";
        String password = "cjiu tjod kihl ebuu";
        String recipient = volunteerMail;
        MailerAPI.sendVolunteerInformation(username, password, recipient, volunteerName, volunteerAddress, volunteerPhone, volunteerMail, volunteerAvailability, volunteerProfession);

        // Display success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("Volunteer added successfully!");
        alert.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert volunteerNameTextField != null : "fx:id=\"volunteerNameTextField\" was not injected: check your FXML file 'Volunteer.fxml'.";
        assert volunteerAddressTextField != null : "fx:id=\"volunteerAddressTextField\" was not injected: check your FXML file 'Volunteer.fxml'.";
        assert volunteerPhoneTextField != null : "fx:id=\"volunteerPhoneTextField\" was not injected: check your FXML file 'Volunteer.fxml'.";
        assert volunteerEmailTextField != null : "fx:id=\"volunteerMailTextField\" was not injected: check your FXML file 'Volunteer.fxml'.";
        assert volunteerAvailabilityComboBox != null : "fx:id=\"volunteerAvailabilityChoiceBox\" was not injected: check your FXML file 'Volunteer.fxml'.";
        assert volunteerProfessionTextField != null : "fx:id=\"volunteerProfessionTextField\" was not injected: check your FXML file 'Volunteer.fxml'.";
    }
    @FXML
    void gotocommentaire(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionAbon.fxml"));
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) Showvolunteers.getScene().getWindow();
        Scene scene = new Scene(parent);

        stage.setTitle("Show volunteers");
        stage.setScene(scene);
        stage.show();
    }




    @FXML
    void Showvolunteers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/volunteermanager.fxml"));
        Parent parent = loader.load();
        Stage stage = (Stage) Showvolunteers.getScene().getWindow();
        Scene scene = new Scene(parent);

        stage.setTitle("Show volunteers");
        stage.setScene(scene);
        stage.show();
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
    void Showcompany(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/company.fxml"));
        Parent parent = loader.load();
        Stage stage = (Stage) Showvolunteers.getScene().getWindow();
        Scene scene = new Scene(parent);

        stage.setTitle("Add Company");
        stage.setScene(scene);
        stage.show();
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
    void Deconnection(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Close the stage (which effectively closes the scene)
        stage.close();
    }


    private boolean isValidEmail(String email) {
        return email.contains("@");
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
