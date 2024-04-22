package com.hemotransfert.hemotransfert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    public List<Demandededon> generateDemandededonReport() {
        List<Demandededon> report = new ArrayList<>();
        String query = "SELECT * FROM demandededons";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Demandededon demandededon = new Demandededon();
                demandededon.setIdDemande(rs.getInt("idDemande"));
                demandededon.setDateDemande(rs.getDate("dateDemande").toLocalDate());
                demandededon.setTypeDon(rs.getString("typeDon"));
                demandededon.setQuantiteDemande(rs.getInt("quantiteDemande"));
                demandededon.setStatusDemande(rs.getString("statusDemande"));
                report.add(demandededon);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return report;
    }

    public List<Reservation> generateReservationReport() {
        List<Reservation> report = new ArrayList<>();
        String query = "SELECT * FROM Reservation";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setIdReservation(rs.getInt("idReservation"));
                reservation.setDateReservation(rs.getDate("dateReservation").toLocalDate());
                reservation.setHeureReservation(rs.getTime("heureReservation").toLocalTime());
                reservation.setQuantiteReservee(rs.getInt("quantiteReservee"));
                reservation.setCommentaire(rs.getString("commentaire"));
                reservation.setTypeCase(rs.getString("typeCase"));
                report.add(reservation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return report;
    }

    public List<DonationReport> generateDonationReport() {
        List<DonationReport> report = new ArrayList<>();
        String query = "SELECT dateDemande, COUNT(*) as donationCount FROM demandededons WHERE statusDemande = 'Confirmée' GROUP BY dateDemande";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                DonationReport donationReport = new DonationReport();
                donationReport.setDate(rs.getDate("dateDemande").toLocalDate());
                donationReport.setDonationCount(rs.getInt("donationCount"));
                report.add(donationReport);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return report;
    }

    public List<BloodTypeReport> generateBloodTypeReport() {
        List<BloodTypeReport> report = new ArrayList<>();
        String query = "SELECT typeDon, COUNT(*) as count FROM demandededons WHERE statusDemande = 'Confirmée' GROUP BY typeDon";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                BloodTypeReport bloodTypeReport = new BloodTypeReport();
                bloodTypeReport.setBloodType(rs.getString("typeDon"));
                bloodTypeReport.setCount(rs.getInt("count"));
                report.add(bloodTypeReport);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return report;
    }
}