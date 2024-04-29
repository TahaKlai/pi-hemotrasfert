package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
public class StockController implements Initializable {


    Connection con =null;
    PreparedStatement st =null;
    ResultSet rs=null;



    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnexportToPDF;
    @FXML
    private Button btntri;
    @FXML
    private TableColumn<Stock, Integer> colid;
    @FXML
    private TableColumn<Stock, String> coltype;
    @FXML
    private TableColumn<Stock, Integer> colquantite;
    @FXML
    private TableColumn<Stock, String> coletat;
    @FXML
    private TableColumn<Stock, String> colorigine;





    @FXML
    private TableView<Stock> table;
    int id=0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchByType.textProperty().addListener((observable, oldValue, newValue) -> showStock());

        searchByEtat.textProperty().addListener((observable, oldValue, newValue) -> showStock());
        showStock();
        tquantite.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Vérifie si la saisie contient uniquement des chiffres
                tquantite.setText(newValue.replaceAll("[^\\d]", "")); // Remplace les caractères non-chiffres par une chaîne vide
            }
        });
        torigine.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() <= 2) { // Vérifie si la longueur de la saisie est inférieure ou égale à 2
                torigine.setStyle("-fx-border-color: red;"); // Modifie la couleur de la bordure en rouge pour indiquer une erreur
            } else {
                torigine.setStyle(""); // Réinitialise le style si la saisie est valide
            }
        });
    }


    public ObservableList<Stock> getStocks(){
      ObservableList<Stock> stocks= FXCollections.observableArrayList();
      String query= "SELECT * FROM stocks";
      con = DBConnection.getInstance().getCnx();
        try {
            st =  con.prepareStatement(query);
            rs= st.executeQuery();
            while (rs.next()){

                Stock st=new Stock();
                st.setId(rs.getInt("id"));
                st.setType(rs.getString("Type"));
                st.setQuantite(rs.getInt("Quantite"));
                st.setEtat(rs.getString("Etat"));
                st.setOrigine(rs.getString("Origine"));
                stocks.add(st);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }

public void showStock(){

       ObservableList<Stock>list=getStocks();

    String typeFilter = searchByType.getText().trim().toLowerCase();
    list = list.filtered(stock -> stock.getType().toLowerCase().contains(typeFilter));
    String etatFilter = searchByEtat.getText().trim().toLowerCase();
    list = list.filtered(stock -> stock.getEtat().toLowerCase().contains(etatFilter));

    table.setItems(list);

       coltype.setCellValueFactory(new PropertyValueFactory<Stock,String>("type"));
       colquantite.setCellValueFactory(new PropertyValueFactory<Stock,Integer>("quantite"));
       coletat.setCellValueFactory(new PropertyValueFactory<Stock,String>("etat"));
       colorigine.setCellValueFactory(new PropertyValueFactory<Stock,String>("origine"));
}

    @FXML
    private TextField tetat;

    @FXML
    private TextField torigine;

    @FXML
    private TextField tquantite;

    @FXML
    private TextField ttype;
    @FXML
    private TextField searchByType;

    @FXML
    private TextField searchByEtat;
    @FXML
    void clearField(ActionEvent event) {
clear();
    }

    @FXML
    void createStock(ActionEvent event) {

        String insert = "insert into stocks(TYPE,QUANTITE,ETAT,ORIGINE) values (?,?,?,?)";
        con = DBConnection.getInstance().getCnx();
        if (validerChamps()) {
            try {
                st = con.prepareStatement(insert);
                st.setString(1, ttype.getText());
                st.setString(2, tquantite.getText());
                st.setString(3, tetat.getText());
                st.setString(4, torigine.getText());
                st.executeUpdate();
                clear();
                showStock();
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
Stock stock=table.getSelectionModel().getSelectedItem();
id= stock.getId();
ttype.setText(stock.getType());
int quantite = stock.getQuantite();
tquantite.setText(String.valueOf(quantite));
tetat.setText(stock.getEtat());
torigine.setText(stock.getOrigine());
btnSave.setDisable(true);
    }

    void clear(){
ttype.setText(null);
tquantite.setText(null);
tetat.setText(null);
torigine.setText(null);
btnSave.setDisable(false);
    }


    @FXML
    void deleteStock(ActionEvent event) {
String delete ="delete from stocks where id =?";
        con = DBConnection.getInstance().getCnx();
        try {
            st =  con.prepareStatement(delete);
            st.setInt(1,id);
            st.executeUpdate();
showStock();
clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void updateStock(ActionEvent event) {
String update="update stocks set type=?,quantite=?,etat=?,origine=? where id=?" ;
        con = DBConnection.getInstance().getCnx();
        if (validerChamps()) { try {
            st =  con.prepareStatement(update);
            st.setString(1,ttype.getText());
            st.setString(2,tquantite.getText());
            st.setString(3,tetat.getText());
            st.setString(4,torigine.getText());
            st.setInt(5,id);
            st.executeUpdate();
showStock();
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




    private boolean validerChamps() {
        if (ttype.getText().isEmpty() || tquantite.getText().isEmpty() || tetat.getText().isEmpty() || torigine.getText().isEmpty()) {
            return false; // Vérifie si un champ est vide
        }
        // Valider la longueur du champ "origine"
        if (torigine.getText().length() <= 2) {
            return false; // Vérifie si la longueur du champ "origine" est inférieure ou égale à 2
        }
        // Valider si le champ "quantite" contient un nombre entier
        try {
            Integer.parseInt(tquantite.getText());
        } catch (NumberFormatException e) {
            return false; // Le champ "quantite" n'est pas un nombre entier
        }
        // Si toutes les validations réussissent, retourne vrai
        return true;
    }
    public void trierParType(ActionEvent event) {
        ObservableList<Stock> liste = FXCollections.observableArrayList(table.getItems());

        liste.sort(Comparator.comparing(Stock::getType));

        table.setItems(liste);
    }

    @FXML
    public void exportToPDF(ActionEvent event) {
        // Récupérer les données du TableView
        List<String[]> data = getDataFromTableView();

        // Demander l'emplacement du fichier PDF
        File file = askForSaveLocation("Save PDF");

        if (file != null) {
            // Exporter les données vers le fichier PDF
            try {
                exportToPDF(data, file.getAbsolutePath());
                showAlert(Alert.AlertType.INFORMATION, "Success", "PDF Exported Successfully!");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export PDF!");
                e.printStackTrace(); // Vous pouvez enregistrer cette trace de pile dans un fichier journal
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid file location selected!");
        }
    }


    private List<String[]> getDataFromTableView() {
        List<String[]> data = new ArrayList<>();
        int numColumns = 4; // Nombre de colonnes dans votre TableView

        // Parcours des lignes de la TableView
        for (Stock stock : table.getItems()) {
            String[] rowData = new String[numColumns];

            // Récupérer les données de chaque colonne pour cette ligne
            rowData[0] = stock.getType();
            rowData[1] = Integer.toString(stock.getQuantite()); // Utilisation de Integer.toString pour la quantité
            rowData[2] = stock.getEtat();
            rowData[3] = stock.getOrigine();

            // Ajouter les données de cette ligne à la liste de données
            data.add(rowData);
        }

        return data;
    }




    private File askForSaveLocation(String title) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        // Configurez le file chooser pour ne montrer que les fichiers PDF
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher le file chooser et attendre que l'utilisateur sélectionne un emplacement
        Stage stage = (Stage) table.getScene().getWindow(); // Obtenez la fenêtre parente pour afficher le file chooser
        File selectedFile = fileChooser.showSaveDialog(stage);

        return selectedFile;
    }

    private static final int MARGIN = 50;
    private static final int ROWS_PER_PAGE = 30;
    private static final int COLUMN_WIDTH = 100;
    private static final int LINE_HEIGHT = 20;

    private void exportToPDF(List<String[]> data, String filePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            // Define the title and logo content
            String title = "Liste de stock de sang disponible ";
            String logoPath = "src/main/resources/logo.png"; // Provide the path to your logo image

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                float yStart = page.getMediaBox().getHeight() - MARGIN;
                float yPosition = yStart;

                int numRows = data.size();
                int numPages = (numRows + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;

                for (int currentPage = 0; currentPage < numPages; currentPage++) {
                    // Draw the title
                    contentStream.beginText();
                    contentStream.newLineAtOffset(MARGIN, yPosition);
                    contentStream.showText(title);
                    contentStream.endText();
                    yPosition -= LINE_HEIGHT; // Move down for the title

                    // Load the logo image and resize it
                    float logoWidth = 40; // Adjust as needed
                    float logoHeight = 40; // Adjust as needed
                    PDImageXObject logo = PDImageXObject.createFromFile(logoPath, document);
                    contentStream.drawImage(logo, page.getMediaBox().getWidth() - logoWidth - MARGIN, page.getMediaBox().getHeight() - logoHeight - MARGIN, logoWidth, logoHeight);

                    // Adjust yPosition to leave space for the logo
                    yPosition -= logoHeight + MARGIN; // Move down for the logo

                    // Draw the header row
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    drawHeader(contentStream, MARGIN, yPosition);
                    yPosition -= LINE_HEIGHT;

                    // Draw data rows
                    int startRow = currentPage * ROWS_PER_PAGE;
                    int endRow = Math.min(startRow + ROWS_PER_PAGE, numRows);
                    for (int i = startRow; i < endRow; i++) {
                        drawRow(contentStream, data.get(i), MARGIN, yPosition);
                        yPosition -= LINE_HEIGHT;
                    }

                    if (currentPage < numPages - 1) {
                        document.addPage(new PDPage());
                        yPosition = yStart;
                    }
                }

            }

            document.save(filePath);
        }
    }

    private void drawHeader(PDPageContentStream contentStream, float xStart, float yPosition) throws IOException {
        String[] headers = {"Type", "Quantité", "État", "Origine"};
        float xOffset = xStart; // Initialize the horizontal offset
        for (String header : headers) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xOffset, yPosition); // Use xOffset
            contentStream.showText(header);
            contentStream.endText();
            xOffset += COLUMN_WIDTH; // Increment the horizontal offset for the next column
        }
    }

    private void drawRow(PDPageContentStream contentStream, String[] rowData, float xStart, float yPosition) throws IOException {
        float xOffset = xStart; // Initialize the horizontal offset
        for (String data : rowData) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xOffset, yPosition); // Use xOffset
            contentStream.showText(data);
            contentStream.endText();
            xOffset += COLUMN_WIDTH; // Increment the horizontal offset for the next column
        }
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
