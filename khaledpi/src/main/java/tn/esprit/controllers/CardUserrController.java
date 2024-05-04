package tn.esprit.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.esprit.models.Actualite;
import tn.esprit.services.ServiceActualite;

import java.io.IOException;
import java.time.LocalDate;
public class CardUserrController {
    @FXML
    private Label cardname;
    @FXML
    private Label cardpr;
    @FXML
    private Label carddate;
    @FXML
    private Label cardcat;
    @FXML
    private Pane Card;
    @FXML
    private Label carddatefin;
    private final ServiceActualite ActualiteS = new ServiceActualite();
    int uid;

    String utitre, upr,ucat;

    LocalDate udate;

    private String[] colors = {"#CDB4DB", "#FFC8DD", "#FFAFCC", "#BDE0FE", "#A2D2FF",
            "#F4C2D7", "#FFD4E2", "#FFB7D0", "#A6D9FF", "#8BC8FF",
            "#E6A9CB", "#FFBFD3", "#FFA7C1", "#9AC2FF", "#74AFFA",
            "#D8B6D8", "#FFC9D7", "#FFB3C8", "#B0E1FF", "#8DCFFD",
            "#D3AADB", "#FFBEDF", "#FFA9CC", "#AFD5FF", "#93C5FF"};


    public void setData(Actualite actualite) {

        cardname.setText(actualite.getTitre());
        carddate.setText(actualite.getDate().toString());

        cardpr.setText(actualite.getPriorite());
        cardcat.setText(actualite.getCategorie());



        Card.setBackground(Background.fill(Color.web(colors[(int)(Math.random()* colors.length)])));
        Card.setStyle("-fx-border-radius: 5px;-fx-border-color:#808080");


        uid = actualite.getId();
        utitre = actualite.getTitre();

        upr = actualite.getPriorite();
        ucat = actualite.getCategorie(); // Assuming getDate_debut_a() returns a java.util.Date
       // Assuming getDate_fin_a() returns a java.util.Date
    }
    public void modifuser(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionCat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            ActualiteController AUC = loader.getController();





            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void suppuser(ActionEvent actionEvent) throws IOException {
        ActualiteS.DeleteByID(uid);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionCat.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        ActualiteController AUC = loader.getController();
        stage.setScene(scene);
        stage.show();
    }
}
