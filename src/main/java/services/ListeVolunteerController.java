package services;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Volunteer;
import utils.DBConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;




public class ListeVolunteerController implements Initializable {

    @FXML
    private Button afficherVolunteer;
    @FXML
    private Button supprimer;
    @FXML
    private Button Retour;
    @FXML
    private Button eChercher;
    @FXML
    private Button pdf;
    @FXML
    private Button modifier;
    @FXML
    private TableView<Volunteer> table;
    @FXML
    private TableColumn<Volunteer, String> volunteerNameColumn;
    @FXML
    private TableColumn<Volunteer, String> volunteerAddressColumn;
    @FXML
    private TableColumn<Volunteer, Integer> volunteerPhoneColumn;
    @FXML
    private TableColumn<Volunteer, String> volunteerEmailColumn;
    @FXML
    private TableColumn<Volunteer, String> volunteerAvailabilityColumn;
    @FXML
    private TableColumn<Volunteer, String> volunteerProfessionColumn;
    @FXML
    private TableColumn<Volunteer, String> CompanyColumn;
    @FXML
    private TextField volunteerName;
    @FXML
    private TextField volunteerAddress;
    @FXML
    private TextField volunteerPhone;
    @FXML
    private TextField volunteerEmail;

    @FXML
    private ChoiceBox<String> volunteerAvailability;
    @FXML
    private TextField volunteerProfession;
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> sortingCriteriaComboBox;

    @FXML
    private Button sortTable;
    @FXML
    private Button stat;
    @FXML
    private Button searchButton;
    private ObservableList<Volunteer> volunteersList;
    @FXML
    private PieChart statPieChart;

    private Connection cnx;



    @FXML
    private void statB(ActionEvent event) {
        PieChart pieChart = new PieChart();
        try {
            cnx = DBConnection.getInstance().getCnx();
            String query = "SELECT COUNT(*) AS count, dispo FROM volunteer GROUP BY dispo";
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                pieChart.getData().add(new PieChart.Data(rs.getString("dispo"), rs.getInt("count")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "An error occurred while fetching statistics: " + ex.getMessage());
            return;
        }

        pieChart.setTitle("**Statistiques Availability **");
        pieChart.setLegendSide(Side.LEFT);
        Scene newScene = new Scene(new Group(pieChart));
        Stage stage = new Stage();
        stage.setScene(newScene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {


       loadTableDataV();
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedVolunteer();
        });
    }



    @FXML
    private void selectedVolunteer() {

        Volunteer selectedVolunteer = table.getSelectionModel().getSelectedItem();
        if (selectedVolunteer == null) {
            return;
        }
        volunteerName.setText(selectedVolunteer.getName());
        volunteerAddress.setText(selectedVolunteer.getAddress());

        volunteerPhone.setText(String.valueOf(selectedVolunteer.getPhone()));
        volunteerEmail.setText(selectedVolunteer.getMail());
        volunteerProfession.setText(selectedVolunteer.getProfession());
        volunteerAvailability.setValue(selectedVolunteer.getDispo());




}
    @FXML
    private void modifierVolunteer(ActionEvent event) {
        VolunteerController volunteerController = new VolunteerController();

        Volunteer selectedVolunteer = table.getSelectionModel().getSelectedItem();
        if (selectedVolunteer != null) {
            String name = volunteerName.getText();
            String address = volunteerAddress.getText();
            String phone = volunteerPhone.getText();
            String email = volunteerEmail.getText();
            String availability = volunteerAvailability.getValue();
            String profession = volunteerProfession.getText();





            if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() ||  profession.isEmpty() || availability == null) {
                showErrorAlert("Error", "Please fill in all fields.");
                return;
            }

            if (!isValidPhoneNumber(phone)) {
                showErrorAlert("Error", "Phone number must contain only numbers.");
                return;
            }

            if (!isValidEmail(email)) {
                showErrorAlert("Error", "Please enter a valid email address.");
                return;
            }
            selectedVolunteer.setName(name);

            selectedVolunteer.setAddress(address);

            selectedVolunteer.setPhone(parseInt(phone));
            selectedVolunteer.setMail(email);
            selectedVolunteer.setDispo(availability);
            selectedVolunteer.setProfession(profession);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            try {
                volunteerController.updateVolunteer(selectedVolunteer);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            alert.setTitle("Success");
            alert.setContentText("Volunteer added successfully!");
            alert.show();
            loadTableDataV();
        }
        else {   showAlert(Alert.AlertType.ERROR, "Error", null, "select Company first !");
        }

    }



    private void loadTableDataV() {
        VolunteerController volunteerController = new VolunteerController();

        volunteerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        volunteerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        volunteerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        volunteerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));
        volunteerAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("dispo"));
        volunteerProfessionColumn.setCellValueFactory(new PropertyValueFactory<>("profession"));
        CompanyColumn.setCellValueFactory(cellData -> {
            Volunteer volunteer = cellData.getValue();
            String companyName = null;
            try {
                companyName = volunteerController.getCompanyName(volunteer.getCompany_id());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty(companyName);
        });

        try {
            volunteersList = FXCollections.observableList(volunteerController.affVolunteer());
            table.setItems(volunteersList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void sortTable() {

       String sortingCriteria = sortingCriteriaComboBox.getValue();

        VolunteerController volunteerController = new VolunteerController();
        try {
            volunteersList = FXCollections.observableList(volunteerController.sortVolunteerBy(sortingCriteria));
            table.setItems(volunteersList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private void supprimerVolunteer(ActionEvent event) {
        VolunteerController volunteerController = new VolunteerController();
        Volunteer selectedVolunteer = table.getSelectionModel().getSelectedItem();

        if (selectedVolunteer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment supprimer le volunteer ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    volunteerController.deleteVolunteer(selectedVolunteer);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", null, "Volunteer supprimé avec succès !");
                    loadTableDataV();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", null, "Une erreur est survenue lors de la suppression du volunteer.");
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Annulation", null, "Suppression annulée.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur de sélection", null, "Veuillez sélectionner un volunteer à supprimer.");
        }
        afficherVolunteer(event);
    loadTableDataV();
    }


    @FXML
    private void Retour(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Volunteer.fxml"));
        Scene newScene = new Scene(root);
        Stage stage = (Stage) Retour.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }
    @FXML
    private void chercherVolunteer(ActionEvent event) {
        VolunteerController volunteerController = new VolunteerController();
        ObservableList<Volunteer> list ;
        try {
            list = FXCollections.observableList(volunteerController.affVolunteer());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        table.setItems(list);

        try {
            volunteerController.affVolunteer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        table.setItems(list);


        FilteredList<Volunteer> filteredData = new FilteredList<>(list, b -> true);


            searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Evenement -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (Evenement.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;

                    }  else {
                        return false;
                    }
                });

            }));
        SortedList<Volunteer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

@FXML
private void generatePDF(ActionEvent event) {

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save PDF File");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

    File file = fileChooser.showSaveDialog(null);

    if (file != null) {
        try {
            OutputStream outputStream = new FileOutputStream(file);
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Volunteer r = table.getSelectionModel().getSelectedItem();
            if (r != null) {
 document.add(new Paragraph("************************Volunteer Information************************\n\n\n\n\n\n\n"));
     document.add(new Paragraph(" ___________________________________________________________________________\n"));
  document.add(new Paragraph("Volunteer Name: " + r.getName()+ "  \n"));
   document.add(new Paragraph("Address: " + r.getAddress()+ "  \n"));
     document.add(new Paragraph("Phone: " + r.getPhone()+ "  \n"));
     document.add(new Paragraph("Email: " + r.getMail()+ "  \n"));
       document.add(new Paragraph("Availability: " + r.getDispo()+ "  \n"));
      document.add(new Paragraph("Profession: " + r.getProfession()+ "  \n"));
     document.add(new Paragraph(" _______________________________________________________________________"));
            }

            document.close();
            outputStream.close();
            showAlert(Alert.AlertType.INFORMATION, "Success", null, "PDF file created successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "An error occurred while creating PDF.");
        }
    }
}


    @FXML
    private void afficherVolunteer(ActionEvent event) {
        Volunteer r = table.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Volunteer Info");
        alert.setContentText("**********Volunteer Information********"+"  \n" +" Volunteer Name  :  " + r.getName() + "\n"
                + "Address  :  " + r.getAddress()+ "  \n" +" Availability  :  " + r.getDispo()+ "  \n"
                +" mail  :  "  +  r.getMail()+ "  \n" +      " phone  :  " + r.getPhone()+ "  \n"
                +       " address :  " + r.getAddress()+ "  \n" +
                " Profession :  " + r.getProfession()+ "  \n"
        );



        alert.show();
    }


    @FXML
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
