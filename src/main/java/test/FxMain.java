package test;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FxMain extends Application {

    public static void main(String[] args) {

        boolean isConnected = checkDatabaseConnection();
        if (isConnected) {
            launch(args);
        } else {
            System.out.println("Database connection failed!");

        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/company.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        stage.setTitle("Add company ");
        stage.setScene(scene);

        stage.show();
    }


    private static boolean checkDatabaseConnection() {
        try {
            Connection connection = DBConnection.getInstance().getCnx();
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
