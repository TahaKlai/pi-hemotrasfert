package tn.esprit.models;



public class Commentaire {
    private int id;
    private String theme;
    private String description;

    public Commentaire() {
    }

    public Commentaire(int id, String theme, String description) {
        this.id = id;
        this.theme = theme;
        this.description = description;
    }
    public Commentaire( String theme, String description) {

        this.theme = theme;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", theme='" + theme + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}





