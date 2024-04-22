package tn.esprit.controllers;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tn.esprit.models.Actualite;
import tn.esprit.models.Commentaire;
import tn.esprit.services.ServiceActualite;
import tn.esprit.services.ServiceCommentaire;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
public class ActualiteController {
    @FXML
    private GridPane userContainer;
    @FXML
    public TextField titretf;
    @FXML
    private TextField cattf;
    @FXML
    private TextField prtf;
    @FXML
    private DatePicker datetf;
    @FXML
    private DatePicker datefintf;
    @FXML
    private TextField usersearch;
    @FXML
    public TextField desctf;
    @FXML
    private Label uinfolabel;
    private final ServiceActualite ActualiteS = new ServiceActualite();
    private Connection cnx;
    public void initialize(URL url, ResourceBundle rb) {
        load();
    }
    @FXML
    void AjouterCat(ActionEvent event) throws SQLException {
        String THEME = titretf.getText();
        LocalDate localDate = datetf.getValue();
        String PRIORITE = prtf.getText();
        String CATEGORIE = cattf.getText();

        // Validation pour le titre
        if (!THEME.matches("[a-zA-Z]+")) {
            showAlert("Le titre doit contenir uniquement des lettres.");
            return;
        }

        // Validation pour la priorité (niveau 1, niveau 2, niveau 3)
        if (!PRIORITE.matches("(niveau 1|niveau 2|niveau 3)")) {
            showAlert("La priorite doit être 'niveau 1', 'niveau 2' ou 'niveau 3'.");
            return;
        }

        // Validation pour la catégorie
        if (!CATEGORIE.matches("[a-zA-Z]+")) {
            showAlert("La catégorie doit contenir uniquement des lettres.");
            return;
        }

        // Validation pour la date
        if (localDate == null) {
            showAlert("Veuillez sélectionner une date.");
            return;
        }

        // Convertir LocalDate en java.sql.Date
        java.sql.Date DATE = java.sql.Date.valueOf(localDate);

        // Si tous les champs sont valides, procéder à l'ajout des données
        ActualiteS.Add(new Actualite(THEME, DATE, PRIORITE, CATEGORIE));
        uinfolabel.setText("Ajout Effectue");
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Deconnection(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Close the stage (which effectively closes the scene)
        stage.close();
    }
    @FXML
    void ModifierCat(ActionEvent event) {

        String THEME = titretf.getText();
        LocalDate localDate = datetf.getValue();
        // Convert LocalDate to java.sql.Date
        java.sql.Date DATE = java.sql.Date.valueOf(localDate);
        String PRIORITE = prtf.getText();
        String CATEGORIE = cattf.getText();




        ActualiteS.Update(new Actualite(THEME, DATE,PRIORITE,CATEGORIE));
        uinfolabel.setText("Modification Effectue");
    }
    public void load() {
        int column = 0;
        int row = 1;
        try {
            for (Actualite actualite : ActualiteS.afficher()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/CardUser1.fxml"));
                Pane userBox = fxmlLoader.load();
                CardUserrController cardC = fxmlLoader.getController();
                cardC.setData(actualite);
                if (column == 3) {
                    column = 0;
                    ++row;
                }
                userContainer.add(userBox, column++, row);
                GridPane.setMargin(userBox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void RechercheNom(ActionEvent event) {
        int column = 0;
        int row = 1;
        String recherche = usersearch.getText();
        try {
            userContainer.getChildren().clear();
            for (Actualite actualite : ActualiteS.Rechreche(recherche)){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/CardUser1.fxml"));
                Pane userBox = fxmlLoader.load();
                CardUserrController cardC = fxmlLoader.getController();
                cardC.setData(actualite);
                if (column == 3) {
                    column = 0;
                    ++row;
                }
                userContainer.add(userBox, column++, row);
                GridPane.setMargin(userBox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void refresh(ActionEvent event) {
        load();
    }
    @FXML
    void TriNom(ActionEvent event) {
        int column = 0;
        int row = 1;
        try {
            for (Actualite actualite : ActualiteS.TriparNom()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/CardUser1.fxml"));
                Pane userBox = fxmlLoader.load();
                CardUserrController cardC = fxmlLoader.getController();
                cardC.setData(actualite);
                if (column == 3) {
                    column = 0;
                    ++row;
                }
                userContainer.add(userBox, column++, row);
                GridPane.setMargin(userBox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void extract(ActionEvent event) {
        try {
            generatePDF();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void generatePDF() throws FileNotFoundException {
        // Get the path to the Downloads directory
        String downloadsDir = System.getProperty("user.home") + "/Downloads/";

        // Create a PDF file in the Downloads directory
        File file = new File(downloadsDir + "Actualites.pdf");
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);

        // Create a document
        Document document = new Document(pdf);

        // Add content to the document
        for (Actualite actualite : ActualiteS.afficher()) {
            document.add(new Paragraph("titre:       " + actualite.getTitre()));
            document.add(new Paragraph("date:    " + actualite.getDate()));
            document.add(new Paragraph("priorite:       " + actualite.getPriorite()));
            document.add(new Paragraph("categorie:    " + actualite.getCategorie()));


            document.add(new Paragraph("\n")); // Add a blank line between users
        }
        // Close the document
        document.close();

        System.out.println("PDF file generated successfully at: " + file.getAbsolutePath());
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

}
