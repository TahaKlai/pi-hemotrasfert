package services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Company;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.scene.paint.Color;

import javafx.scene.control.Alert;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import utils.DBConnection;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;




public class ListeCompanyController implements Initializable {

    @FXML
    private Button supprimer;
    @FXML
    private Button Retour;

    @FXML
    private Button pdf;

    @FXML
    private TableView<Company> table;
    @FXML
    private TableColumn<Company, String> Name;
    @FXML
    private TableColumn<Company, String> Country;
    @FXML
    private TableColumn<Company, String> Website;
    @FXML
    private TableColumn<Company, String> Email;
    @FXML
    private TableColumn<Company, Integer> Phone;
    @FXML
    private TableColumn<Company, String> Address;
    @FXML
    private TableColumn<Company, String> Description;
    @FXML
    private TextField CompanyName;
    @FXML
    private TextField Countryi;
    @FXML
    private TextField emaill;
    @FXML
    private TextField site;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField eChercher;

    @FXML
    private ChoiceBox<String> sortingCriteriaComboBox;
    private ObservableList<Company> companiesList;

    @FXML
    private Button QRcode;
    @FXML
    private Button stat;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadTableData();
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedCompany();
        });

    }

    private Connection cnx;




    @FXML
    private void statC(ActionEvent event) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        try {
            cnx = DBConnection.getInstance().getCnx();

            String query = "SELECT COUNT(*) AS count, country FROM company GROUP BY country";
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            List<Color> usedColors = new ArrayList<>();

            while (rs.next()) {
                Color randomColor;
                do {
                    randomColor = generateRandomColor();
                } while (usedColors.contains(randomColor));

                usedColors.add(randomColor);
                Rectangle rectangle = new Rectangle(10, 10, randomColor);


                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(rectangle);

                series.getData().add(new XYChart.Data<>(rs.getString("country"), rs.getInt("count"), stackPane));
            }

            barChart.getData().add(series);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "An error occurred while fetching statistics: " + ex.getMessage());
            return;
        }


        barChart.setTitle("**Statistiques country **");
        xAxis.setLabel("Country");
        yAxis.setLabel("Count");


        Scene newScene = new Scene(new Group(barChart));

        Stage stage = new Stage();
        stage.setScene(newScene);


        stage.show();
    }


    private Color generateRandomColor() {
        return Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    @FXML
    private void selectedCompany() {

        Company selectedCompany = table.getSelectionModel().getSelectedItem();

        if (selectedCompany == null) {
            return;
        }
        CompanyName.setText(selectedCompany.getName());
        Countryi.setText(selectedCompany.getCountry());
        site.setText(selectedCompany.getWebsite().replace("https://", ""));
        emaill.setText(selectedCompany.getMail());
        phoneTextField.setText(String.valueOf(selectedCompany.getPhone()));
        addressTextField.setText(selectedCompany.getAddress());
        descriptionTextField.setText(selectedCompany.getDescription());


    }



    @FXML
    private void generateQRCode() {
        Company selectedCompany = table.getSelectionModel().getSelectedItem();


        String companyInfo = "**********Company Information********\n"
                + " Company Name  :  " + selectedCompany.getName() + "\n"
                + "country  :  " + selectedCompany.getCountry() + "\n"
                + " website  :  " + selectedCompany.getWebsite() + "\n"
                + " mail  :  " + selectedCompany.getMail() + "\n"
                + " phone  :  " + selectedCompany.getPhone() + "\n"
                + " address :  " + selectedCompany.getAddress() + "\n"
                + " description :  " + selectedCompany.getDescription() + "\n";

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(companyInfo, BarcodeFormat.QR_CODE, 500, 500);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            displayImageInWindow(image);
        } catch (WriterException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to generate QR code: " + e.getMessage());
        }
    }

    private void displayImageInWindow(Image image) {
        ImageView imageView = new ImageView(image);
        VBox vbox = new VBox(imageView);
        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }





    private void loadTableData() {
        Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Country.setCellValueFactory(new PropertyValueFactory<>("country"));
        Website.setCellValueFactory(new PropertyValueFactory<>("website"));
        Email.setCellValueFactory(new PropertyValueFactory<>("mail"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        CompanyController companyController = new CompanyController();
        try {
            companiesList = FXCollections.observableList(companyController.afficher());
            table.setItems(companiesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void sortTable() {

        String sortingCriteria = sortingCriteriaComboBox.getValue();


        CompanyController companyController = new CompanyController();
        try {
            companiesList = FXCollections.observableList(companyController.tri(sortingCriteria));
            table.setItems(companiesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private void RetourMenu(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Company.fxml"));
        Scene newScene = new Scene(root);
        Stage stage = (Stage) Retour.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }

    @FXML
    private void Chercherevent(ActionEvent event) {
        CompanyController sr = new CompanyController();
        ObservableList<Company> list;
        try {
            list = FXCollections.observableList(sr.afficher());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Country.setCellValueFactory(new PropertyValueFactory<>("country"));
        Website.setCellValueFactory(new PropertyValueFactory<>("website"));
        Email.setCellValueFactory(new PropertyValueFactory<>("mail"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.setItems(list);

        FilteredList<Company> filteredData = new FilteredList<>(list, b -> true);
        eChercher.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(company -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return company.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Company> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }
    @FXML
    private void imprimer(ActionEvent event) {
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
       Company r = table.getSelectionModel().getSelectedItem();
                if (r != null) {
    document.add(new Paragraph("************************Company Information************************\n\n\n\n\n\n\n"));
          document.add(new Paragraph(" ___________________________________________________________________________\n"));
   document.add(new Paragraph(" Company Name  :  " + r.getName() + "  \n"));
   document.add(new Paragraph(" country  :  " + r.getCountry() + "  \n"));
   document.add(new Paragraph(" website  :  " + r.getWebsite() + "  \n"));
  document.add(new Paragraph(" mail  :  " + r.getMail() + "  \n"));
            document.add(new Paragraph(" phone  :  " + r.getPhone() + "  \n"));
              document.add(new Paragraph(" address :  " + r.getAddress() + "  \n"));
  document.add(new Paragraph(" description :  " + r.getDescription() + "  \n"));
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
    private void supprimerCompany(ActionEvent event) {
        CompanyController sr = new CompanyController();
        Company r = table.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer la Company ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                sr.deleteOne(r);
                showAlert(Alert.AlertType.INFORMATION, "Succès", null, "Company supprimée avec succès !");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Annulation", null, "Suppression annulée.");
        }

        loadTableData();
    }

    @FXML
    private void modifierCompany(ActionEvent event) {
        CompanyController sr = new CompanyController();
        Company selectedCompany = table.getSelectionModel().getSelectedItem();



        String name = CompanyName.getText();
        String country = Countryi.getText();
        String website = site.getText();
        String mail = emaill.getText();
        String phone = phoneTextField.getText();
        String address = addressTextField.getText();
        String description = descriptionTextField.getText();

        if (name.isEmpty() || country.isEmpty() || website.isEmpty() || mail.isEmpty() || phone.isEmpty() || address.isEmpty() || description.isEmpty()) {
            showErrorAlert("Error", "Please fill in all fields.");
            return;
        }

        if (!isValidWebsite(website)) {
            showErrorAlert("Error", "Please enter a valid website address.");
            return;
        }

        if (!isValidEmail(mail)) {
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

        if (!isValidPhoneNumber(phone)) {
            showErrorAlert("Error", "Phone number must contain only numbers.");
            return;
        }

        selectedCompany.setName(name);
        selectedCompany.setCountry(country);
        selectedCompany.setWebsite("https://" + website);
        selectedCompany.setMail(mail);
        selectedCompany.setPhone(Integer.parseInt(phone));
        selectedCompany.setAddress(address);
        selectedCompany.setDescription(description);


        try {
            sr.updateOne(selectedCompany);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setContentText("Company updated successfully!");
        successAlert.show();
        loadTableData();
    }



    @FXML
    private void afficherCompany(ActionEvent event)  {
        Company r = table.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Company Info");
        alert.setContentText("**********Company Information********"+"  \n" +" Company Name  :  " + r.getName() + "\n"
        + "country  :  " + r.getCountry()+ "  \n" +" website  :  " + r.getWebsite()+ "  \n"
         +" mail  :  "  +  r.getMail()+ "  \n" +      " phone  :  " + r.getPhone()+ "  \n"
         +       " address :  " + r.getAddress()+ "  \n" +
                        " description :  " + r.getDescription()+ "  \n"
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
