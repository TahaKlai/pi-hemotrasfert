package com.hemotransfert.hemotransfert;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load Demandededon
       Parent demandededonParent = FXMLLoader.load(getClass().getResource("/Fxml/Demandededons.fxml"));
        Scene demandededonScene = new Scene(demandededonParent);
        Stage demandededonStage = new Stage();
        demandededonStage.setTitle("Hemotransfert - Demandededon");
        demandededonStage.setScene(demandededonScene);
        demandededonStage.show();

        // Load Reservation
       /* Parent reservationParent = FXMLLoader.load(getClass().getResource("/Fxml/Reservation.fxml"));
        Scene reservationScene = new Scene(reservationParent);
        Stage reservationStage = new Stage();
        reservationStage.setTitle("Hemotransfert - Reservation");
        reservationStage.setScene(reservationScene);
        reservationStage.show();*/

        // Load ReportService
       /* Parent reportServiceParent = FXMLLoader.load(getClass().getResource("/Fxml/ReportService.fxml"));
        Scene reportServiceScene = new Scene(reportServiceParent);
        Stage reportServiceStage = new Stage();
        reportServiceStage.setTitle("Hemotransfert - ReportService");
        reportServiceStage.setScene(reportServiceScene);
        reportServiceStage.show();*/


        testWordAPI();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public void testWordAPI() {
        WordAPI wordAPI = new WordAPI();
        try {
            String definition = wordAPI.getWordDefinition("example");
            System.out.println(definition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}