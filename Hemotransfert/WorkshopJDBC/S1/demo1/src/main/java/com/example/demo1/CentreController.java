package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
public class CentreController implements Initializable {
    @FXML
    private BarChart<String, Integer> barChart;
    Connection con =null;
    PreparedStatement st =null;
    ResultSet rs=null;
    // Déclarez la pagination
    @FXML
    private Pagination pagination;

    // Ajoutez une variable pour stocker le nombre total d'éléments
    private int totalItems;

    @FXML
    private Button btnClear1;

    @FXML
    private Button btnDelete1;

    @FXML
    private Button btnSave1;

    @FXML
    private Button btnUpdate1;


    @FXML
    private TableColumn<Centre, Integer> colid1;
    @FXML
    private TableColumn<Centre, String> colnom;
    @FXML
    private TableColumn<Centre, String> coladresse;
    @FXML
    private TableColumn<Centre, String> colcoordonne;
    @FXML
    private TableColumn<Centre, String> colcontact;





    @FXML
    private TableView<Centre> table1;
    int id=0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePagination();

            afficherStatistiquesParAdresse();

    }

    private void afficherStatistiquesParAdresse() {
        // Appeler la méthode pour récupérer les statistiques depuis la base de données
        Map<String, Integer> statistiquesParAdresse = getStatistiquesParAdresseDepuisBaseDeDonnees();

        // Code pour remplir le BarChart avec les statistiques
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : statistiquesParAdresse.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(series);
    }
    private Map<String, Integer> getStatistiquesParAdresseDepuisBaseDeDonnees() {
        // Initialiser la map pour stocker les statistiques
        Map<String, Integer> statistiquesParAdresse = new HashMap<>();
        String query = "SELECT adresse, COUNT(*) AS count FROM centre_de_don GROUP BY adresse";
        con = DBConnection.getInstance().getCnx();

        // Établir la connexion à la base de données
        try
        {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            // Parcourir les résultats et remplir la map avec les statistiques
            while (rs.next()) {
                String adresse = rs.getString("adresse");
                int count = rs.getInt("count");
                statistiquesParAdresse.put(adresse, count);
            }
        } catch (SQLException e) {
            // Gérer les exceptions liées à la connexion à la base de données ou à l'exécution de la requête
            e.printStackTrace();
            throw new RuntimeException(e);

        }

        // Retourner la map contenant les statistiques par adresse
        return statistiquesParAdresse;
    }



    private void initializePagination() {
        totalItems = getTotalItemsFromDatabase();
        pagination.setPageCount(calculatePageCount());
        pagination.setPageFactory(pageIndex -> {
            loadPageData(pageIndex);
            return table1;
        });
    }
    private int calculatePageCount() {
        // Supposez que vous souhaitez afficher 4 éléments par page
        int itemsPerPage = 4;
        return (totalItems + itemsPerPage - 1) / itemsPerPage;
    }
    private void loadPageData(int pageIndex) {
        int itemsPerPage = 4;
        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        ObservableList<Centre> pageData = getCentres(startIndex, endIndex);
        table1.setItems(pageData);
    }

    private int getTotalItemsFromDatabase() {
        String query = "SELECT COUNT(*) AS total FROM centre_de_don";
        con = DBConnection.getInstance().getCnx();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0; // Retourne 0 en cas d'échec ou si la table est vide
    }

    public ObservableList<Centre> getCentres(int startIndex, int count) {
        ObservableList<Centre> centres = FXCollections.observableArrayList();
        String query = "SELECT * FROM centre_de_don LIMIT ?, ?";
        con = DBConnection.getInstance().getCnx();
        try {
            st = con.prepareStatement(query);
            st.setInt(1, startIndex);
            st.setInt(2, count);
            rs = st.executeQuery();
            while (rs.next()) {
                Centre st = new Centre();
                st.setId(rs.getInt("id"));
                st.setNom(rs.getString("nom"));
                st.setAdresse(rs.getString("adresse"));
                st.setCoordonnegeo(rs.getString("coordonnegeo"));
                st.setContact(rs.getString("contact"));
                centres.add(st);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return centres;
    }




    @FXML
    private TextField tnom;

    @FXML
    private TextField tadresse;

    @FXML
    private TextField tcoordonne;

    @FXML
    private TextField tcontact;

    @FXML
    void clearField(ActionEvent event) {
        clear();
    }

    @FXML
    void createCentre(ActionEvent event) {
        String insert = "insert into centre_de_don(NOM,ADRESSE,COORDONNEGEO,CONTACT) values (?,?,?,?)";
        con = DBConnection.getInstance().getCnx();

        if (validerChampsCentre()) {
            try {
                st = con.prepareStatement(insert);
                st.setString(1, tnom.getText());
                st.setString(2, tadresse.getText());
                st.setString(3, tcoordonne.getText());
                st.setString(4, tcontact.getText());
                st.executeUpdate();
                clear();
                // Mise à jour de la pagination après la mise à jour d'un centre
                pagination.setPageCount(calculatePageCount());
                // Afficher les données de la page actuelle après la mise à jour
                loadPageData(pagination.getCurrentPageIndex());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Afficher un message d'erreur si la validation échoue
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs correctement.");
            alert.showAndWait();
        }
    }

    @FXML
    void getData(MouseEvent event) {
        Centre centre = table1.getSelectionModel().getSelectedItem();
        if (centre != null) { // Vérifiez si centre est null
            id = centre.getId();
            tnom.setText(centre.getNom());
            tadresse.setText(centre.getAdresse());
            tcoordonne.setText(centre.getCoordonnegeo());
            tcontact.setText(centre.getContact());
            btnSave1.setDisable(true);
        }
    }

    void clear(){
        tnom.setText(null);
        tadresse.setText(null);
        tcontact.setText(null);
        tcoordonne.setText(null);
        btnSave1.setDisable(false);
    }


    @FXML
    void deleteCentre(ActionEvent event) {
        String delete ="delete from centre_de_don where id =?";
        con = DBConnection.getInstance().getCnx();
        try {
            st =  con.prepareStatement(delete);
            st.setInt(1,id);
            st.executeUpdate();
            // Mise à jour de la pagination après la mise à jour d'un centre
            pagination.setPageCount(calculatePageCount());
            // Afficher les données de la page actuelle après la mise à jour
            loadPageData(pagination.getCurrentPageIndex());
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void updateCentre(ActionEvent event) {
        String update="update centre_de_don set nom=?,adresse=?,coordonnegeo=?,contact=? where id=?" ;
        con = DBConnection.getInstance().getCnx();
        if (validerChampsCentre()) {try {
            st =  con.prepareStatement(update);
            st.setString(1,tnom.getText());
            st.setString(2,tadresse.getText());
            st.setString(3,tcoordonne.getText());
            st.setString(4,tcontact.getText());
            st.setInt(5,id);
            st.executeUpdate();
            // Mise à jour de la pagination après la mise à jour d'un centre
            pagination.setPageCount(calculatePageCount());
            // Afficher les données de la page actuelle après la mise à jour
            loadPageData(pagination.getCurrentPageIndex());
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }}
        else {
            // Afficher un message d'erreur si la validation échoue
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs correctement.");
            alert.showAndWait();
        }
    }



    boolean validerChampsCentre() {
        String nom = tnom.getText();
        String adresse = tadresse.getText();
        String coordonnee = tcoordonne.getText();
        String contact = tcontact.getText();

        // Vérifier que le nom a une longueur supérieure à 2
        if (nom.length() <= 2) {
            return false;
        }

        // Vérifier que l'adresse et les coordonnées sont des chaînes non vides
        if (adresse.isEmpty() || coordonnee.isEmpty()) {
            return false;
        }

        // Vérifier que le contact contient le caractère '@'
        if (!contact.contains("@")) {
            return false;
        }

        // Si toutes les conditions sont remplies, retourner true
        return true;
    }




}
