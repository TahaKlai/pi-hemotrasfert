package com.hemotransfert.hemotransfert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Hyperlink;
import javafx.scene.web.WebView;
import org.json.JSONArray;
import org.json.JSONObject;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DemandededonController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

        WordAPI wordAPI = new WordAPI();

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private DatePicker tDate;
    @FXML
    private TextField tDatedemande;

    @FXML
    private TextField tIDDemande;


    @FXML
    private TextField tQuantitedemande;

    @FXML
    private TextField tStatusdemande;

    @FXML
    private TextField tTypedon;

    @FXML
    private TableColumn<Demandededon, LocalDate> colDate;

    @FXML
    private TableColumn<Demandededon, Integer> colIDDE;

    @FXML
    private TableColumn<Demandededon, Integer> colIDDO;

    @FXML
    private TableColumn<Demandededon, Integer> colQuantite;

    @FXML
    private TableColumn<Demandededon, String> colStatut;

    @FXML
    private TableColumn<Demandededon, String> colType;

    @FXML
    private TableView<Demandededon> table;

    int iddemande=0;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colDate.setCellValueFactory(new PropertyValueFactory<Demandededon, LocalDate>("dateDemande"));
        colType.setCellValueFactory(new PropertyValueFactory<Demandededon, String>("typeDon"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<Demandededon, Integer>("quantiteDemande"));
        colStatut.setCellValueFactory(new PropertyValueFactory<Demandededon, String>("statusDemande"));
        showDemandededons();
    }

    void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);

        VBox vbox = new VBox();
        vbox.setPrefWidth(400); // Set the preferred width
        vbox.setPrefHeight(400); // Set the preferred height

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(vbox);

        if (message.startsWith("[")) {
            JSONArray jsonArray = new JSONArray(message);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
            JSONObject meaningsObject = meaningsArray.getJSONObject(0);
            JSONArray sourceUrlsArray = meaningsObject.getJSONArray("sourceUrls");
            String url = sourceUrlsArray.getString(0);

            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setText(url);
            hyperlink.setOnAction(event -> {
                try {
                    if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                        java.awt.Desktop.getDesktop().browse(new java.net.URI(hyperlink.getText()));
                    } else {
                        System.out.println("Can't open URL, desktop or browse action not supported!");
                    }
                } catch (Exception e) {
                    System.out.println("Error opening URL:");
                    e.printStackTrace();
                }
            });

            vbox.getChildren().addAll(new Text(message), hyperlink);
        } else {
            // Handle the case where the message is not a JSON array
            // This could be a JSON object, a simple string, or something else
            vbox.getChildren().add(new Text(message));
        }

        alert.getDialogPane().setContent(scrollPane);
        alert.showAndWait();
    }
    void showDemandededons() {
        ObservableList<Demandededon> demandededonList = FXCollections.observableArrayList();
        String select = "SELECT * FROM demandededons";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(select);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Demandededon demandededon = new Demandededon();
                // Assuming Demandededon has these setter methods
                demandededon.setIdDemande(rs.getInt("idDemande"));
                demandededon.setDateDemande(rs.getDate("dateDemande").toLocalDate());
                demandededon.setTypeDon(rs.getString("typeDon"));
                demandededon.setQuantiteDemande(rs.getInt("quantiteDemande"));
                demandededon.setStatusDemande(rs.getString("statusDemande"));
                demandededonList.add(demandededon);
            }
            table.setItems(demandededonList);
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    void clearField(ActionEvent event) {
        clear();

    }
    void clear() {
        tDate.setValue(null);
        tTypedon.clear();
        tQuantitedemande.clear();
        tStatusdemande.clear();
        btnSave.setDisable(false);
    }

    @FXML
    void creatDemandededon(ActionEvent event) {
        if (tDate.getValue() == null || tTypedon.getText().isEmpty() || tQuantitedemande.getText().isEmpty() || tStatusdemande.getText().isEmpty()) {
            showAlert("Please fill in all fields");
            return;
        }

        // Validate typeDon string
        if (!tTypedon.getText().matches("^[a-zA-Z ]*$")) {
            showAlert("TypeDon must only contain alphabetic characters");
            return;
        }

        // Validate statusDemande string
        if (!tStatusdemande.getText().matches("^[a-zA-Z ]*$")) {
            showAlert("StatusDemande must only contain alphabetic characters");
            return;
        }

        // Validate quantiteDemande integer
        int quantiteDemande;
        try {
            quantiteDemande = Integer.parseInt(tQuantitedemande.getText());
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid Input for quantiteDemande");
            return;
        }

        String insert = "INSERT INTO demandededons (dateDemande, typeDon, quantiteDemande, statusDemande) VALUES (?,?,?,?)";
        con=DBConnection.getCon();
        try {
            st = con.prepareStatement(insert);
            st.setDate(1, java.sql.Date.valueOf(tDate.getValue()));
            st.setString(2, tTypedon.getText());
            st.setInt(3, quantiteDemande);
            st.setString(4, tStatusdemande.getText());
            st.executeUpdate();
            clearField(null);
            showDemandededons();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    void deleteDemandededon(ActionEvent event) {
        String delete = "DELETE FROM demandededons WHERE idDemande=?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(delete);
            st.setInt(1, iddemande);
            st.executeUpdate();
            clear();
            showDemandededons();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void updateDemandededon(ActionEvent event) {
        if (tDate.getValue() == null || tTypedon.getText().isEmpty() || tQuantitedemande.getText().isEmpty() || tStatusdemande.getText().isEmpty()) {
            showAlert("Please fill in all fields");
            return;
        }

        // Validate typeDon string
        if (!tTypedon.getText().matches("^[a-zA-Z ]*$")) {
            showAlert("TypeDon must only contain alphabetic characters");
            return;
        }

        // Validate statusDemande string
        if (!tStatusdemande.getText().matches("^[a-zA-Z ]*$")) {
            showAlert("StatusDemande must only contain alphabetic characters");
            return;
        }

        // Validate quantiteDemande integer
        int quantiteDemande;
        try {
            quantiteDemande = Integer.parseInt(tQuantitedemande.getText());
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid Input for quantiteDemande");
            return;
        }

        String update = "UPDATE demandededons SET dateDemande=?, typeDon=?, quantiteDemande=?, statusDemande=? WHERE idDemande=?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(update);
            st.setDate(1, java.sql.Date.valueOf(tDate.getValue()));
            st.setString(2, tTypedon.getText());
            st.setInt(3, quantiteDemande);
            st.setString(4, tStatusdemande.getText());
            st.setInt(5, iddemande);
            st.executeUpdate();
            clearField(null);
            showDemandededons();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String getWordExplanation(String word) {
        try {
            return wordAPI.getWordDefinition(word);
        } catch (Exception e) {
            System.out.println("Error fetching word definition: " + e.getMessage());
            return null;
        }
    }

    @FXML
    void getData(MouseEvent event) {
        Demandededon demandededon = table.getSelectionModel().getSelectedItem();
        if (demandededon == null) {
            showAlert("No Demandededon selected");
            return;
        }
        iddemande = demandededon.getIdDemande();
        tDate.setValue(demandededon.getDateDemande());
        String typeDon = demandededon.getTypeDon();
        tTypedon.setText(typeDon);
        String explanation = getWordExplanation(typeDon);
        if (explanation != null) {
            showAlert("Explanation for " + typeDon + ": " + explanation);
        }
        tQuantitedemande.setText(String.valueOf(demandededon.getQuantiteDemande()));
        tStatusdemande.setText(demandededon.getStatusDemande());
        btnSave.setDisable(true);
    }

}
