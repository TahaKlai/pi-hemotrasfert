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
import tn.esprit.models.Commentaire;
import tn.esprit.services.ServiceCommentaire;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CommentaireController {
    @FXML
    private GridPane userContainer;
    @FXML
    public TextField themetf;
    @FXML
    private TextField prixtf;
    @FXML
    private TextField numabonntf;
    @FXML
    private DatePicker datedebtf;
    @FXML
    private DatePicker datefintf;
    @FXML
    private TextField usersearch;
    @FXML
    public TextField desctf;
    @FXML
    private Label uinfolabel;
    private final ServiceCommentaire CommentaireS = new ServiceCommentaire();
    private Connection cnx;

    public void initialize(URL url, ResourceBundle rb) {
        load();
    }

    @FXML
    void AjouterAbb(ActionEvent event) throws SQLException {
        String THEME = themetf.getText();
        String DESCRIPTION = desctf.getText();

        // Validate THEME
        if (THEME.isEmpty()) {
            showAlert("Theme field cannot be empty.");
            return;
        }

        // Validate DESCRIPTION
        if (DESCRIPTION.isEmpty()) {
            showAlert("Description field cannot be empty.");
            return;
        }

        // If all fields are valid, proceed with adding the data
        CommentaireS.Add(new Commentaire(THEME, DESCRIPTION));
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
    void ModifierAbb(ActionEvent event) {

        String THEME = themetf.getText();
        String DESCRIPTION = desctf.getText();




        CommentaireS.Update(new Commentaire(THEME,DESCRIPTION));
        uinfolabel.setText("Modification Effectue");
    }
    public void load() {
        int column = 0;
        int row = 1;
        try {
            for (Commentaire commentaire : CommentaireS.afficher()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/CardUser.fxml"));
                Pane userBox = fxmlLoader.load();
                CardUserController cardC = fxmlLoader.getController();
                cardC.setData(commentaire);
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
            for (Commentaire commentaire : CommentaireS.Rechreche(recherche)){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/CardUser.fxml"));
                Pane userBox = fxmlLoader.load();
                CardUserController cardC = fxmlLoader.getController();
                cardC.setData(commentaire);
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
            for (Commentaire commentaire : CommentaireS.TriparNom()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/CardUser.fxml"));
                Pane userBox = fxmlLoader.load();
                CardUserController cardC = fxmlLoader.getController();
                cardC.setData(commentaire);
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
        File file = new File(downloadsDir + "Commentaires.pdf");
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);

        // Create a document
        Document document = new Document(pdf);

        // Add content to the document
        for (Commentaire commentaire : CommentaireS.afficher()) {
            document.add(new Paragraph("Theme:       " + commentaire.getTheme()));
            document.add(new Paragraph("description:    " + commentaire.getDescription()));

            document.add(new Paragraph("\n")); // Add a blank line between users
        }
        // Close the document
        document.close();

        System.out.println("PDF file generated successfully at: " + file.getAbsolutePath());
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

}
