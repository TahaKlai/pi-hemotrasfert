package com.hemotransfert.hemotransfert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class ReportServiceController {

    @FXML
    private TableView<Demandededon> demandedonTable;

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableView<DonationReport> donationReportTable;

    @FXML
    private TableView<BloodTypeReport> bloodTypeReportTable;

    private ReportService reportService;

    @FXML
    public void initialize() {
        reportService = new ReportService();
    }

    @FXML
    void generateDemandededonReport(ActionEvent event) {
        ObservableList<Demandededon> demandedonData = FXCollections.observableArrayList(reportService.generateDemandededonReport());
        demandedonTable.setItems(demandedonData);
        // Assuming Demandededon has fields idDemande and idDonateur
        TableColumn<Demandededon, Integer> idDemandeColumn = new TableColumn<>("ID Demande");
        idDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("idDemande"));
        TableColumn<Demandededon, Integer> idDonateurColumn = new TableColumn<>("ID Donateur");
        idDonateurColumn.setCellValueFactory(new PropertyValueFactory<>("idDonateur"));
        demandedonTable.getColumns().setAll(idDemandeColumn, idDonateurColumn);
    }

    @FXML
    void generateReservationReport(ActionEvent event) {
        ObservableList<Reservation> reservationData = FXCollections.observableArrayList(reportService.generateReservationReport());
        reservationTable.setItems(reservationData);
        // Assuming Reservation has fields idReservation and idDemande
        TableColumn<Reservation, Integer> idReservationColumn = new TableColumn<>("ID Reservation");
        idReservationColumn.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
        TableColumn<Reservation, Integer> idDemandeColumn = new TableColumn<>("ID Demande");
        idDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("idDemande"));
        reservationTable.getColumns().setAll(idReservationColumn, idDemandeColumn);
    }

    @FXML
    void generateDonationReport(ActionEvent event) {
        ObservableList<DonationReport> donationReportData = FXCollections.observableArrayList(reportService.generateDonationReport());
        donationReportTable.setItems(donationReportData);
        // Assuming DonationReport has fields date and donationCount
        TableColumn<DonationReport, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<DonationReport, Integer> donationCountColumn = new TableColumn<>("Donation Count");
        donationCountColumn.setCellValueFactory(new PropertyValueFactory<>("donationCount"));
        donationReportTable.getColumns().setAll(dateColumn, donationCountColumn);
    }

    @FXML
    void generateBloodTypeReport(ActionEvent event) {
        ObservableList<BloodTypeReport> bloodTypeReportData = FXCollections.observableArrayList(reportService.generateBloodTypeReport());
        bloodTypeReportTable.setItems(bloodTypeReportData);
        // Assuming BloodTypeReport has fields bloodType and count
        TableColumn<BloodTypeReport, String> bloodTypeColumn = new TableColumn<>("Blood Type");
        bloodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        TableColumn<BloodTypeReport, Integer> countColumn = new TableColumn<>("Count");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        bloodTypeReportTable.getColumns().setAll(bloodTypeColumn, countColumn);
    }
}