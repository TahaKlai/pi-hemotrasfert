package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Person;
import services.ServicePerson;

public class AjouterPersonneFXML {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tfAge;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfPrenom;

    @FXML
    void ajouterPersonne(ActionEvent event) {
        try {
            Person p = new Person(tfNom.getText(), tfPrenom.getText(), Integer.parseInt(tfAge.getText()));
            ServicePerson sp = new ServicePerson();
            sp.insertOne(p);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        }
    }

    @FXML
    void initialize() {
        assert tfAge != null : "fx:id=\"tfAge\" was not injected: check your FXML file 'AjouterPersonneFXML.fxml'.";
        assert tfNom != null : "fx:id=\"tfNom\" was not injected: check your FXML file 'AjouterPersonneFXML.fxml'.";
        assert tfPrenom != null : "fx:id=\"tfPrenom\" was not injected: check your FXML file 'AjouterPersonneFXML.fxml'.";

    }

}
