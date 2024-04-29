package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {


        Parent parent = FXMLLoader.load(getClass().getResource("/Fxml/Stock.fxml"));


        Scene scene= new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setTitle("CRUD");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){

launch();

    }
}
