package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.*;
import services.ServiceLivreur;
import services.ServiceMateriel;
import utils.DBConnection;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class GesMaterielFXML implements Initializable {

    private static Stage stage;

    @FXML
    private TextField Descriptiontxt;

    @FXML
    private TextField Quantitetxt;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField searchtypetxt;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private DatePicker dateexptxt;

    @FXML
    private ComboBox<Livreur> livreurtxt;

    @FXML
    private ComboBox<String> statuttxt;

    @FXML
    private TableView<Materiel> table;

    @FXML
    private TextField typetxt;

    private Materiel materiel;

    private Livreur livreur;

    @FXML
    private TableColumn<Materiel, Date> col_dateexp;

    @FXML
    private TableColumn<Materiel, String> col_descriprion;

    @FXML
    private TableColumn<Materiel, Integer> col_id;

    @FXML
    private TableColumn<Materiel, String> col_livreur;

    @FXML
    private TableColumn<Materiel, Integer> col_quantite;

    @FXML
    private TableColumn<Materiel, String> col_statut;

    @FXML
    private TableColumn<Materiel, String> col_type;

    //////////////////////////////////////////////////////LIVREUR ATTRIBUT+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @FXML
    private Button btnDelete_liv;

    @FXML
    private Button btnClear_liv;

    @FXML
    private Button btnSave_liv;

    @FXML
    private Button btnUpdate_liv;

    @FXML
    private TextField cintxt;

    @FXML
    private TextField emailtxt;

    @FXML
    private TextField nomtxt;

    @FXML
    private TextField prenomtxt;

    @FXML
    private TextField searchName;

    @FXML
    private ComboBox<String> statut_liv_txt;

    @FXML
    private TextField tel;

    @FXML
    private TextField zone_liv_txt;

    @FXML
    private TableView<Livreur> table1;

    @FXML
    private TableColumn<Livreur, Integer> col_id1;

    @FXML
    private TableColumn<Livreur, String> col_cin;

    @FXML
    private TableColumn<Livreur, String> col_email;

    @FXML
    private TableColumn<Livreur, String> col_nom;

    @FXML
    private TableColumn<Livreur, String> col_prenom;

    @FXML
    private TableColumn<Livreur, String> col_statutliv;

    @FXML
    private TableColumn<Livreur, Integer> col_tel;

    @FXML
    private TableColumn<Livreur, String> col_zoneliv;



    //___________________________________________________________________________________________________________________________________

    ServiceMateriel serviceMateriel = new ServiceMateriel();
    ServiceLivreur serviceLivreur = new ServiceLivreur();

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        GesMaterielFXML.stage = stage;
    }

    @FXML
    void clearField(ActionEvent event) {
        typetxt.setText("");
        Descriptiontxt.setText("");
        Quantitetxt.setText("");
        dateexptxt.setValue(null);
        statuttxt.setValue("");
        livreurtxt.setValue(null);
        //////////////////////
        cintxt.setText("");
        nomtxt.setText("");
        prenomtxt.setText("");
        tel.setText("");
        emailtxt.setText("");
        statut_liv_txt.setValue(null);
        zone_liv_txt.setText("");
    }

    @FXML
    void createMateriel(ActionEvent event) {

        if (typetxt.getText().isEmpty() || Descriptiontxt.getText().isEmpty() || Quantitetxt.getText().isEmpty() || dateexptxt.getValue() == null || statuttxt.getValue() == null || livreurtxt.getValue() == null) {
            // Affichage d'un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill in all required fields.");
            alert.showAndWait();
            return;
        }

        // Vérification que la quantité est un entier valide
        try {
            Integer.parseInt(Quantitetxt.getText());
        } catch (NumberFormatException e) {
            // Affichage d'un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Quantity must be a valid integer.");
            alert.showAndWait();
            return;
        }
        //******************
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add confirmation");
        dialog.setHeaderText("Are you sure you want to create ");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());
        Label label = new Label("Materiel : " + typetxt.getText());
        dialog.getDialogPane().setContent(label);
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            Materiel materiel = new Materiel();

            materiel.setLivreur_id(livreurtxt.getValue().getId());
            materiel.setType_m(typetxt.getText());
            materiel.setDescription_m(Descriptiontxt.getText());
            materiel.setQuantite_m(Integer.parseInt(Quantitetxt.getText()));
            materiel.setDateexp_m(Date.valueOf(dateexptxt.getValue()));
            materiel.setStatut_m(statuttxt.getValue());
            try {
                serviceMateriel.insertOne1(materiel);
                ShowMateriel();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            table.refresh();
        }
    }

    @FXML
    void deleteMateriel(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add confirmation");
        dialog.setHeaderText("Are you sure you want to Delete ");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());
        Label label = new Label("Materiel : " + typetxt.getText());
        dialog.getDialogPane().setContent(label);
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            Materiel materiel = new Materiel();

            materiel.setId(this.materiel.getId());
//        materiel.setLivreur_id(livreurtxt.getValue().getId());
            materiel.setType_m(typetxt.getText());
            materiel.setDescription_m(Descriptiontxt.getText());
            materiel.setQuantite_m(Integer.parseInt(Quantitetxt.getText()));
            materiel.setDateexp_m(Date.valueOf(dateexptxt.getValue()));
            materiel.setStatut_m(statuttxt.getValue());
            try {
                serviceMateriel.deleteOne(materiel);
                ShowMateriel();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            table.refresh();
        }
    }

    @FXML
    void updateMateriel(ActionEvent event) {

        if (typetxt.getText().isEmpty() || Descriptiontxt.getText().isEmpty() || Quantitetxt.getText().isEmpty() || dateexptxt.getValue() == null || statuttxt.getValue() == null || livreurtxt.getValue() == null) {
            // Affichage d'un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill in all required fields.");
            alert.showAndWait();
            return;
        }

        // Vérification que la quantité est un entier valide
        try {
            Integer.parseInt(Quantitetxt.getText());
        } catch (NumberFormatException e) {
            // Affichage d'un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Quantity must be a valid integer.");
            alert.showAndWait();
            return;
        }

//        *********************************************
        Dialog<ButtonType> dialog= new Dialog<>();
        dialog.setTitle("Add confirmation");
        dialog.setHeaderText("Are you sure you want to Update ");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());
        Label label=new Label("Materiel : "+typetxt.getText());
        dialog.getDialogPane().setContent(label);
        ButtonType okButton=new ButtonType("Ok",ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton=new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton,cancelButton);
        Optional<ButtonType>result =dialog.showAndWait();
        if(result.isPresent()&& result.get()==okButton) {
        Materiel materiel = new Materiel();

        materiel.setId(this.materiel.getId());
        materiel.setLivreur_id(livreurtxt.getValue().getId());
        materiel.setType_m(typetxt.getText());
        materiel.setDescription_m(Descriptiontxt.getText());
        materiel.setQuantite_m(Integer.parseInt(Quantitetxt.getText()));
        materiel.setDateexp_m(Date.valueOf(dateexptxt.getValue()));
        materiel.setStatut_m(statuttxt.getValue());
        try {
            serviceMateriel.updateOne(materiel);
            ShowMateriel();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        table.refresh();
    }

}
    @FXML
    public void loadlivreur() {
        try {
            Connection cnx = DBConnection.getInstance().getCnx();

            String req = "SELECT id, nom  FROM livreur ";
            //String req = "SELECT * FROM livreur ";
            Statement st = cnx.createStatement();

            ResultSet rs = st.executeQuery(req);
            ObservableList<Livreur> data = FXCollections.observableArrayList();
            Livreur liv;
            //ObservableList<String> nom = new ObservableList<>();
            while (rs.next()) {
                liv = new Livreur(
                        rs.getInt("id"),
                        rs.getString("nom")
                          /*rs.getInt("id"),
                          rs.getString("cin"),
                          rs.getString("nom"),
                          rs.getString("prenom"),
                          rs.getInt("tel"),
                          rs.getString("email"),
                          rs.getString("statut_l"),
                          rs.getString("Zone_liv")*/
                );
                data.add(liv);
            }
            livreurtxt.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void mouseClicked(MouseEvent event){
    try{
        Materiel materie = table.getSelectionModel().getSelectedItem();
        materiel = new Materiel();

        materiel.setId(materie.getId());
        materiel.setLivreur_id(materie.getLivreur_id());
        materiel.setType_m(materie.getType_m());
        materiel.setDescription_m(materie.getDescription_m());
        materiel.setQuantite_m(materie.getQuantite_m());
        materiel.setDateexp_m(materie.getDateexp_m());
        materiel.setStatut_m(materie.getStatut_m());

        this.materiel=materie;
        if (materie.getDateexp_m() != null) {
            typetxt.setText(materie.getType_m());
            Descriptiontxt.setText(materie.getDescription_m());
            Quantitetxt.setText(String.valueOf(materie.getQuantite_m()));
            dateexptxt.setValue(materie.getDateexp_m().toLocalDate());
        } else {
            // Gérer le cas où la date d'expiration est nulle
            // Par exemple, en laissant le champ de date vide ou en utilisant une date par défaut
            // Ici, nous laissons le champ de date vide
             typetxt.setText(materiel.getType_m());
                    Descriptiontxt.setText(materiel.getDescription_m());
                    Quantitetxt.setText(String.valueOf(materiel.getQuantite_m()));
            dateexptxt.setValue(null);
        }



        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);

    }catch(Exception ex){
        ex.printStackTrace();
        }

    }


    public void ShowMateriel() throws SQLException {
        ServiceMateriel sp = new ServiceMateriel();
        ObservableList<Materiel> list = (ObservableList<Materiel>) sp.selectAll();
        table.setItems(list);
        col_id.setCellValueFactory(new PropertyValueFactory<Materiel, Integer>("id"));
        col_type.setCellValueFactory(new PropertyValueFactory<Materiel, String>("type_m"));
        col_descriprion.setCellValueFactory(new PropertyValueFactory<Materiel, String>("description_m"));
        col_quantite.setCellValueFactory(new PropertyValueFactory<Materiel, Integer>("quantite_m"));
        col_dateexp.setCellValueFactory(new PropertyValueFactory<Materiel, Date>("dateexp_m"));
        col_statut.setCellValueFactory(new PropertyValueFactory<Materiel, String>("statut_m"));

        col_livreur.setCellValueFactory(param -> {
            SimpleStringProperty s = null;
            try {
                s = new SimpleStringProperty(serviceLivreur.selectOne(param.getValue().getLivreur_id()).getNom());
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            return s;
        });
        //  col_livreur.setCellValueFactory(new PropertyValueFactory<Materiel, String>("Livreur"));
    }

    private void  filterData(String searchName) {
        ObservableList<Materiel> filterData = FXCollections.observableArrayList();
        ObservableList<Materiel> list = null;
        try {
            list = (ObservableList<Materiel>) serviceMateriel.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Materiel materiel : list) {
           if(materiel.getType_m().toLowerCase().contains(searchName.toLowerCase())){
               filterData.add(materiel);
           }
        }
        table.setItems(filterData);
    }



    ////////////////////////////////////////////////////////////////////////LIVREUR METHODES////////:::::::::::::::::::::::::::::::::::::::::::::

    @FXML
    void createLivreur(ActionEvent event) {

        // Vérification des champs obligatoires
        if (cintxt.getText().isEmpty() || nomtxt.getText().isEmpty() || prenomtxt.getText().isEmpty() ||
                tel.getText().isEmpty() || emailtxt.getText().isEmpty() || statut_liv_txt.getValue() == null ||
                zone_liv_txt.getText().isEmpty()) {
            // Affichage d'un message d'erreur
            showAlert("Error", "Please fill in all required fields.");
            return;
        }

        // Vérification du format du CIN
        if (cintxt.getText().length() > 10) {
            showAlert("Error", "CIN should not exceed 10 characters.");
            return;
        }

        // Vérification du format du nom et du prénom
        if (nomtxt.getText().length() < 5 || prenomtxt.getText().length() < 5) {
            showAlert("Error", "Name and surname should be at least 5 characters each.");
            return;
        }

        // Vérification du format du numéro de téléphone
        try {
            int telephone = Integer.parseInt(tel.getText());
            if (tel.getText().length() > 8) {
                showAlert("Error", "Telephone should be maximum 8 digits.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Telephone must be a valid integer.");
            return;
        }

        // Vérification du format de l'email
        if (!emailtxt.getText().matches(".+@.+\\.com")) {
            showAlert("Error", "Invalid email format. It should end with '@' followed by '.com'.");
            return;
        }

        //***********************************************************
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add confirmation");
        dialog.setHeaderText("Are you sure you want to create ");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());
        Label label = new Label("Livreur : " + nomtxt.getText());
        dialog.getDialogPane().setContent(label);
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            Livreur livreur = new Livreur();

             livreur.setCin(cintxt.getText());
             livreur.setNom(nomtxt.getText());
             livreur.setPrenom(prenomtxt.getText());
             livreur.setTel(Integer.parseInt(tel.getText()));
             livreur.setEmail(emailtxt.getText());
             livreur.setStatut_l(statut_liv_txt.getValue());
             livreur.setZone_liv(zone_liv_txt.getText());
            try {
                serviceLivreur.insertOne1(livreur);
                ShowLivreur();
                loadlivreur();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            table1.refresh();
        }
    }

    @FXML
    void deleteLivreur(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add confirmation");
        dialog.setHeaderText("Are you sure you want to Delete ");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());
        Label label = new Label("Livreur : " + nomtxt.getText());
        dialog.getDialogPane().setContent(label);
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            Livreur livreur = new Livreur();

            livreur.setId(this.livreur.getId());

            livreur.setCin(cintxt.getText());
            livreur.setNom(nomtxt.getText());
            livreur.setTel(Integer.parseInt(tel.getText()));
            livreur.setPrenom(prenomtxt.getText());
            livreur.setStatut_l(statut_liv_txt.getValue());
            try {
                serviceLivreur.deleteOne(livreur);
                ShowLivreur();
                loadlivreur();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            table1.refresh();
        }

    }

    @FXML
    void mouseClicked_livreur(MouseEvent event) {

        try{
            Livreur liv = table1.getSelectionModel().getSelectedItem();
            livreur = new Livreur();

            livreur.setId(liv.getId());
            livreur.setCin(liv.getCin());
            livreur.setNom(liv.getNom());
            livreur.setPrenom(liv.getPrenom());
            livreur.setTel(liv.getTel());
            livreur.setEmail(liv.getEmail());
            livreur.setStatut_l(liv.getStatut_l());
            livreur.setZone_liv(liv.getZone_liv());

            this.livreur=liv;
                cintxt.setText(liv.getCin());
                nomtxt.setText(liv.getNom());
                prenomtxt.setText(liv.getPrenom());
                tel.setText(String.valueOf(liv.getTel()));
                emailtxt.setText(liv.getEmail());
                statut_liv_txt.setValue(liv.getStatut_l());
                zone_liv_txt.setText(liv.getZone_liv());




            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @FXML
    void updateLivreur(ActionEvent event) throws SQLException{

        // Vérification des champs obligatoires
        if (cintxt.getText().isEmpty() || nomtxt.getText().isEmpty() || prenomtxt.getText().isEmpty() ||
                tel.getText().isEmpty() || emailtxt.getText().isEmpty() || statut_liv_txt.getValue() == null ||
                zone_liv_txt.getText().isEmpty()) {
            // Affichage d'un message d'erreur
            showAlert("Error", "Please fill in all required fields.");
            return;
        }

        // Vérification du format du CIN
        if (cintxt.getText().length() < 5 ||cintxt.getText().length() > 10) {
            showAlert("Error", "CIN should b 10 characters.");
            return;
        }

        // Vérification du format du nom et du prénom
        if (nomtxt.getText().length() < 5 || prenomtxt.getText().length() < 5) {
            showAlert("Error", "Name and surname should be at least 5 characters each.");
            return;
        }

        // Vérification du format du numéro de téléphone
        try {
            int telephone = Integer.parseInt(tel.getText());
            if (tel.getText().length() > 8) {
                showAlert("Error", "Telephone should be maximum 8 digits.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Telephone must be a valid integer.");
            return;
        }

        // Vérification du format de l'email
        if (!emailtxt.getText().matches(".+@.+\\.com")) {
            showAlert("Error", "Invalid email format. It should end with '@' followed by '.com'.");
            return;
        }


        //**************************************************************
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add confirmation");
        dialog.setHeaderText("Are you sure you want to update ");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());
        Label label = new Label("Livreur : " + nomtxt.getText());
        dialog.getDialogPane().setContent(label);
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            Livreur livreur = new Livreur();

            livreur.setId(this.livreur.getId());

            livreur.setCin(cintxt.getText());
            livreur.setNom(nomtxt.getText());
            livreur.setTel(Integer.parseInt(tel.getText()));
            livreur.setEmail(emailtxt.getText());
            livreur.setPrenom(prenomtxt.getText());
            livreur.setStatut_l(statut_liv_txt.getValue());
            livreur.setZone_liv(zone_liv_txt.getText());
            try {
                serviceLivreur.updateOne(livreur);
                ShowLivreur();
                loadlivreur();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            table.refresh();
        }
    }

    public void ShowLivreur() throws SQLException {
        ServiceLivreur sp = new ServiceLivreur();
        ObservableList<Livreur> list = (ObservableList<Livreur>) sp.selectAll();
        table1.setItems(list);
        col_id1.setCellValueFactory(new PropertyValueFactory<Livreur, Integer>("id"));
        col_cin.setCellValueFactory(new PropertyValueFactory<Livreur, String>("cin"));
        col_nom.setCellValueFactory(new PropertyValueFactory<Livreur, String>("nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<Livreur, String>("prenom"));
        col_tel.setCellValueFactory(new PropertyValueFactory<Livreur, Integer>("tel"));
        col_email.setCellValueFactory(new PropertyValueFactory<Livreur, String>("email"));
        col_statutliv.setCellValueFactory(new PropertyValueFactory<Livreur, String>("statut_l"));
        col_zoneliv.setCellValueFactory(new PropertyValueFactory<Livreur, String>("zone_liv"));
    }

    private void  filterDatal(String searchName) {
        ObservableList<Livreur> filterDatal = FXCollections.observableArrayList();
        ObservableList<Livreur> list = null;
        try {
            list = (ObservableList<Livreur>) serviceLivreur.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Livreur livreur : list) {
            if(livreur.getNom().toLowerCase().contains(searchName.toLowerCase())||livreur.getPrenom().toLowerCase().contains(searchName.toLowerCase())){
                filterDatal.add(livreur);
            }
        }
        table1.setItems(filterDatal);

    }

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }






//    ;;;;;;;;;....................................................................................................................................

    @FXML
    public void initialize(URL Location, ResourceBundle resources) {
        ObservableList<String> list = FXCollections.observableArrayList("en etat", "expirer", "en commande");
        statuttxt.setItems(list);
        ObservableList<String> listL = FXCollections.observableArrayList("Indisponible", "Libre", "en commande");
        statut_liv_txt.setItems(listL);
        loadlivreur();

        try {
            ShowMateriel();
            ShowLivreur();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        searchtypetxt.textProperty().addListener((ObservableList, oldValue,newValue)->{filterData(newValue);});
        searchName.textProperty().addListener((ObservableList, oldValue,newValue)->{filterDatal(newValue);});
    }
}

