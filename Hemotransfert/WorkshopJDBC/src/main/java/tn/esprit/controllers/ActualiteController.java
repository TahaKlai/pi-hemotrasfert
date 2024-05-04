package tn.esprit.controllers;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tn.esprit.models.Actualite;
import tn.esprit.models.Commentaire;
import tn.esprit.controllers.ServiceActualite;
import tn.esprit.controllers.ServiceCommentaire;
import tn.esprit.utils.EmailSender;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

public class ActualiteController {
    @FXML
    private GridPane userContainer;
    @FXML
    private TextField searchField;
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
    @FXML
    private Button Showvolunteers;
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
        EmailSender.sendConfirmationEmail("khaledcheour555@gmail.com","","");
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
        String recherche = usersearch.getText().toLowerCase(); // Convertir en minuscules pour la recherche insensible à la casse
        try {
            userContainer.getChildren().clear();
            for (Actualite actualite : ActualiteS.Rechreche(recherche)) { // Suppose que ActualiteS.getAllActualites() retourne toutes les actualités
                // Vérifier si le titre de l'actualité contient la chaîne de recherche
                if (actualite.getTitre().toLowerCase().contains(recherche)) {
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

            generatePDF();


    }


    public void generatePDF() {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                int margin = 60;
                int startY = 700;
                int leading = 20;

                // Titre du document
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                contentStream.newLineAtOffset(margin, startY);
                contentStream.showText("Rapport d'Actualités");
                contentStream.endText();

                startY -= 2 * leading;

                // Informations de base
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(margin, startY);
                contentStream.showText("Informations Générales :");
                contentStream.endText();
                contentStream.beginText();
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setLeading(leading);
                contentStream.newLineAtOffset(margin, startY - leading);
                int i=1;

                for (Actualite actualite : ActualiteS.afficher()) {
                    contentStream.newLine();
                    contentStream.showText("actualite:"+ i++);
                    contentStream.newLine();

                    contentStream.showText("Actualite a la Date:        " + actualite.getDate());
                    contentStream.newLine();

                    contentStream.newLine();
                }
                contentStream.endText();

                startY -= 5 * leading;

                // Informations supplémentaires
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(margin, startY);
                contentStream.showText("");
                contentStream.endText();

                // Vous pouvez continuer à ajouter des informations ici si nécessaire

            }

            document.save("Actualités.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);

        stage.setTitle("Show volunteers");
        stage.setScene(scene);
        stage.show();
    }





    @FXML
    void Showvolunteer(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Volunteer.fxml"));
        Parent parent = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);

        stage.setTitle("Add Volunteer");
        stage.setScene(scene);
        stage.show();
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

