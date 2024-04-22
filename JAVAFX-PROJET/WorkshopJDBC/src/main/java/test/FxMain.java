package test;

import controllers.GesMaterielFXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Materiel;

public class FxMain extends Application {
    private GesMaterielFXML mController =new GesMaterielFXML();

    public static void main(String[] args) {
        launch();
    }



    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/GesMaterielFXML.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        stage.setTitle("Gestion Materiel ");
        mController.setStage(stage);
        stage.setScene(scene);

        stage.show();

    }

   /* @Override
   / public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AjouterPersonneFXML.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        stage.setTitle("Ajouter une personne ");
        stage.setScene(scene);

        stage.show();

    }*/
}
