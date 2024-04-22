package tn.esprit.models;



import java.util.Date;


public class Actualite {

    private int id;
    private String titre;
    private Date date;
    private String priorite;
    private String categorie;


    public Actualite() {
    }

    public Actualite(int id, String titre, Date date, String priorite, String categorie) {
        this.id = id;
        this.titre = titre;
        this.date = date;
        this.priorite = priorite;
        this.categorie = categorie;

    }
    public Actualite( String titre, Date date, String priorite, String categorie) {

        this.titre = titre;
        this.date = date;
        this.priorite = priorite;
        this.categorie = categorie;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public java.sql.Date getDate() {
        return (java.sql.Date) date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }



    @Override
    public String toString() {
        return "Actualite{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", date=" + date +
                ", priorite='" + priorite + '\'' +
                ", categorie='" + categorie + '\'' +

                '}';
    }
}

