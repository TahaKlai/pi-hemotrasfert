package com.hemotransfert.hemotransfert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.time.ZoneId;

public class ReservationController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private DatePicker tDateReservation;

    @FXML
    private TextField tHeureReservation;

    @FXML
    private TextField tQuantiteReservee;

    @FXML
    private TextField tCommentaire;

    @FXML
    private TextField tTypeCase;

    @FXML
    private TableColumn<Reservation, LocalTime> colHeureReservation;

    @FXML
    private TableColumn<Reservation, LocalDate> colDateReservation;

    @FXML
    private TableColumn<Reservation, String> colQuantiteReservee;

    @FXML
    private TableColumn<Reservation, String> colCommentaire;

    @FXML
    private TableColumn<Reservation, String> colTypeCase;

    @FXML
    private TableView<Reservation> table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showReservations();
    }
    void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public ObservableList<Reservation> getReservations() {
        ObservableList<Reservation> reservations = FXCollections.observableArrayList();

        String query = "SELECT * FROM Reservation";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setIdReservation(rs.getInt("idReservation")); // Set idReservation
                reservation.setDateReservation(rs.getDate("dateReservation").toLocalDate());
                reservation.setHeureReservation(rs.getTime("heureReservation").toLocalTime());
                reservation.setQuantiteReservee(rs.getInt("quantiteReservee"));
                reservation.setCommentaire(rs.getString("commentaire"));
                reservation.setTypeCase(rs.getString("typeCase"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }
    public void showReservations() {
        ObservableList<Reservation> reservations = getReservations();
        table.setItems(reservations);
        colDateReservation.setCellValueFactory(new PropertyValueFactory<Reservation, LocalDate>("dateReservation"));
        colHeureReservation.setCellValueFactory(new PropertyValueFactory<Reservation, LocalTime>("heureReservation"));
        colQuantiteReservee.setCellValueFactory(new PropertyValueFactory<Reservation, String>("quantiteReservee"));
        colCommentaire.setCellValueFactory(new PropertyValueFactory<Reservation, String>("commentaire"));
        colTypeCase.setCellValueFactory(new PropertyValueFactory<Reservation, String>("typeCase"));
    }

    @FXML
    void clearField(ActionEvent event) {
        clear();
    }

    void clear() {
        tDateReservation.setValue(null);
        tHeureReservation.clear();
        tQuantiteReservee.clear();
        tCommentaire.clear();
        tTypeCase.clear();
        btnSave.setDisable(false);
    }

    @FXML
    void creatReservation(ActionEvent event) {
        if (tDateReservation.getValue() == null || tHeureReservation.getText().isEmpty() || tQuantiteReservee.getText().isEmpty() || tCommentaire.getText().isEmpty() || tTypeCase.getText().isEmpty()) {
            showAlert("Please fill in all fields");
            return;
        }

        // Validate commentaire string
        if (!tCommentaire.getText().matches("^[a-zA-Z]*$")) {
            showAlert("Commentaire must only contain alphabetic characters");
            return;
        }

        // Validate typeCase string
        if (!tTypeCase.getText().matches("^[a-zA-Z]*$")) {
            showAlert("TypeCase must only contain alphabetic characters");
            return;
        }

        // Validate quantiteReservee integer
        int quantiteReservee;
        try {
            quantiteReservee = Integer.parseInt(tQuantiteReservee.getText());
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid Input for quantiteReservee");
            return;
        }

        String insert = "INSERT INTO Reservation (dateReservation, heureReservation, quantiteReservee, commentaire, typeCase) VALUES (?,?,?,?,?)";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(insert);
            st.setDate(1, java.sql.Date.valueOf(tDateReservation.getValue()));
            st.setTime(2, java.sql.Time.valueOf(tHeureReservation.getText()));
            st.setInt(3, quantiteReservee);
            st.setString(4, tCommentaire.getText());
            st.setString(5, tTypeCase.getText());
            st.executeUpdate();
            clearField(null);
            showReservations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void deleteReservation(ActionEvent event) {
        Reservation reservation = table.getSelectionModel().getSelectedItem();
        if (reservation == null) {
            showAlert("No Reservation selected");
            return;
        }
        String delete = "DELETE FROM Reservation WHERE idReservation=?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(delete);
            st.setInt(1, reservation.getIdReservation());
            st.executeUpdate();
            clear();
            showReservations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void updateReservation(ActionEvent event) {
        Reservation reservation = table.getSelectionModel().getSelectedItem();
        if (reservation == null) {
            showAlert("No Reservation selected");
            return;
        }
        if (tDateReservation.getValue() == null || tHeureReservation.getText().isEmpty() || tQuantiteReservee.getText().isEmpty() || tCommentaire.getText().isEmpty() || tTypeCase.getText().isEmpty()) {
            showAlert("Please fill in all fields");
            return;
        }

        // Validate commentaire string
        if (!tCommentaire.getText().matches("^[a-zA-Z ]*$")) {
            showAlert("Commentaire must only contain alphabetic characters");
            return;
        }

        // Validate typeCase string
        if (!tTypeCase.getText().matches("^[a-zA-Z ]*$")) {
            showAlert("TypeCase must only contain alphabetic characters");
            return;
        }

        // Validate quantiteReservee integer
        int quantiteReservee;
        try {
            quantiteReservee = Integer.parseInt(tQuantiteReservee.getText());
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid Input for quantiteReservee");
            return;
        }

        // Validate heureReservation time
        LocalTime heureReservation;
        try {
            heureReservation = LocalTime.parse(tHeureReservation.getText());
        } catch (DateTimeParseException e) {
            showAlert("Invalid time format. Please enter time in the format hh:mm:ss");
            return;
        }

        String update = "UPDATE Reservation SET dateReservation=?, heureReservation=?, quantiteReservee=?, commentaire=?, typeCase=? WHERE idReservation=?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(update);
            st.setDate(1, java.sql.Date.valueOf(tDateReservation.getValue()));
            st.setTime(2, java.sql.Time.valueOf(heureReservation)); // Changed this line
            st.setInt(3, quantiteReservee);
            st.setString(4, tCommentaire.getText());
            st.setString(5, tTypeCase.getText());
            st.setInt(6, reservation.getIdReservation());

            st.executeUpdate();
            clearField(null);
            showReservations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void getData(MouseEvent event) {
        Reservation reservation = table.getSelectionModel().getSelectedItem();
        if (reservation == null) {
            showAlert("No Reservation selected");
            return;
        }
        tDateReservation.setValue(reservation.getDateReservation());
        tHeureReservation.setText(reservation.getHeureReservation().toString());
        tQuantiteReservee.setText(String.valueOf(reservation.getQuantiteReservee()));
        tCommentaire.setText(reservation.getCommentaire());
        tTypeCase.setText(reservation.getTypeCase());
        btnSave.setDisable(true);
    }
}