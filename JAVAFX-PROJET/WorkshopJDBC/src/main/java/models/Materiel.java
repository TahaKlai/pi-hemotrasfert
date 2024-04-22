package models;
import java.sql.Date;

public class Materiel {
    private int id;
    private String type_m,description_m, statut_m;
    private int quantite_m;
    private Date dateexp_m;
    private int livreur_id;


    public Materiel() {}
    public Materiel(String type_m, String description_m,  int quantite_m,Date dateexp_m,String statut_m) {

        this.type_m = type_m;
        this.description_m = description_m;
        this.quantite_m = quantite_m;
        this.dateexp_m = dateexp_m;
        this.statut_m = statut_m;

    }

    public Materiel(int id,String type_m, String description_m,  int quantite_m,Date dateexp_m,String statut_m) {
        this.id = id;
        this.type_m = type_m;
        this.description_m = description_m;
        this.quantite_m = quantite_m;
        this.dateexp_m = dateexp_m;
        this.statut_m = statut_m;
        }

    public Materiel(String type_m, String description_m, int quantite_m, Date dateexp_m, String statut_m, int livreur_id) {
        this.type_m = type_m;
        this.description_m = description_m;
        this.quantite_m = quantite_m;
        this.dateexp_m = dateexp_m;
        this.statut_m = statut_m;
        this.livreur_id = livreur_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_m() {
        return type_m;
    }

    public void setType_m(String type_m) {
        this.type_m = type_m;
    }

    public String getDescription_m() {
        return description_m;
    }

    public void setDescription_m(String description_m) {
        this.description_m = description_m;
    }

    public String getStatut_m() {
        return statut_m;
    }

    public void setStatut_m(String statut_m) {
        this.statut_m = statut_m;
    }

    public int getQuantite_m() {
        return quantite_m;
    }

    public void setQuantite_m(int quantite_m) {
        this.quantite_m = quantite_m;
    }

    public Date getDateexp_m() {
        return dateexp_m;
    }

    public void setDateexp_m(Date dateexp_m) {
        this.dateexp_m = dateexp_m;
    }

    public int getLivreur_id() {
        return livreur_id;
    }

    public void setLivreur_id(int livreur_id) {
        this.livreur_id = livreur_id;
    }

    @Override
    public String toString() {
        return "Materiel{" +
                "id=" + id +
                ", type_m='" + type_m + '\'' +
                ", description_m='" + description_m + '\'' +
                ", statut_m='" + statut_m + '\'' +
                ", quantite_m=" + quantite_m +
                ", dateexp_m=" + dateexp_m +
                ", livreur=" + livreur_id +
                '}';
    }
}
