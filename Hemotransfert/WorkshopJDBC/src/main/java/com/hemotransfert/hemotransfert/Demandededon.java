package com.hemotransfert.hemotransfert;

import java.time.LocalDate;

public class Demandededon {
    private int idDemande;
    private LocalDate dateDemande;
    private String typeDon;
    private int quantiteDemande;
    private String statusDemande;

    // Getters and Setters
    public int getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(int idDemande) {
        this.idDemande = idDemande;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getTypeDon() {
        return typeDon;
    }

    public void setTypeDon(String typeDon) {
        this.typeDon = typeDon;
    }

    public int getQuantiteDemande() {
        return quantiteDemande;
    }

    public void setQuantiteDemande(int quantiteDemande) {
        this.quantiteDemande = quantiteDemande;
    }

    public String getStatusDemande() {
        return statusDemande;
    }

    public void setStatusDemande(String statusDemande) {
        this.statusDemande = statusDemande;
    }
}