package models;

import javafx.collections.ObservableList;

public class Livreur {
    private int id;
    private String cin;
    private String nom;
    private String prenom;
    private int tel;
    private String email;
    private String statut_l;
    private String zone_liv;

    public Livreur() {}

    public Livreur(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }
    public Livreur(int id, String cin, String nom, String prenom, int tel, String email, String statut_l, String zone_liv) {
        this.id = id;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.email = email;
        this.statut_l = statut_l;
        this.zone_liv = zone_liv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatut_l() {
        return statut_l;
    }

    public void setStatut_l(String statut_l) {
        this.statut_l = statut_l;
    }

    public String getZone_liv() {
        return zone_liv;
    }

    public void setZone_liv(String zone_liv) {
        this.zone_liv = zone_liv;
    }

    @Override
    public String toString() {
        return nom;
    }
}
