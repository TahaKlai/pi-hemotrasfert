package com.hemotransfert.hemotransfert;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private int idReservation;
    private LocalDate dateReservation;
    private LocalTime heureReservation;
    private int quantiteReservee;
    private String commentaire;
    private String typeCase;

    // Getters and Setters
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public LocalTime getHeureReservation() {
        return heureReservation;
    }

    public void setHeureReservation(LocalTime heureReservation) {
        this.heureReservation = heureReservation;
    }

    public int getQuantiteReservee() {
        return quantiteReservee;
    }

    public void setQuantiteReservee(int quantiteReservee) {
        this.quantiteReservee = quantiteReservee;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getTypeCase() {
        return typeCase;
    }

    public void setTypeCase(String typeCase) {
        this.typeCase = typeCase;
    }
}