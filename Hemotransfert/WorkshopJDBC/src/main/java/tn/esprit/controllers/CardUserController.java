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
import tn.esprit.models.Commentaire;
import tn.esprit.services.ServiceCommentaire;

import java.io.IOException;

public class CardUserController {
    @FXML
    private Label carddesc;
    @FXML
    private Label cardname;
    @FXML
    private Pane Card;
    @FXML
    private Label cardspecialite;
    private final ServiceCommentaire CommentaireS = new ServiceCommentaire();

    int uid;
    String unom, udesc;

    private String[] colors = {"#CDB4DB", "#FFC8DD", "#FFAFCC", "#BDE0FE", "#A2D2FF",
            "#F4C2D7", "#FFD4E2", "#FFB7D0", "#A6D9FF", "#8BC8FF",
            "#E6A9CB", "#FFBFD3", "#FFA7C1", "#9AC2FF", "#74AFFA",
            "#D8B6D8", "#FFC9D7", "#FFB3C8", "#B0E1FF", "#8DCFFD",
            "#D3AADB", "#FFBEDF", "#FFA9CC", "#AFD5FF", "#93C5FF"};


    public void setData(Commentaire commentaire) {

        cardname.setText(commentaire.getTheme());
        carddesc.setText(commentaire.getDescription());



        Card.setBackground(Background.fill(Color.web(colors[(int)(Math.random()* colors.length)])));
        Card.setStyle("-fx-border-radius: 5px;-fx-border-color:#808080");


        uid = commentaire.getId();
        unom = commentaire.getTheme();
        udesc = commentaire.getDescription();

    }

    public void suppuser(ActionEvent actionEvent) throws IOException {
        CommentaireS.DeleteByID(uid);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAbon.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        CommentaireController AUC = loader.getController();
        stage.setScene(scene);
        stage.show();
    }

    public void modifuser(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAbon.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            CommentaireController AUC = loader.getController();

            AUC.themetf.setText(unom);
            AUC.desctf.setText(udesc);


            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
