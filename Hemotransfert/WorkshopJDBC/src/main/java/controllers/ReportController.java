package com.hemotransfert.hemotransfert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportController {
    @FXML
    private TableView<DonData> donsParDateTable;
    @FXML
    private TableColumn<DonData, String> dateColumn;
    @FXML
    private TableColumn<DonData, Integer> donsColumn;

    @FXML
    private PieChart donTypeChart;

    private ReportService reportService;

    public void initialize() {
        // Initialize the ReportService
        reportService = new ReportService(new DemandededonRepository(DBConnection.getCon()));

        // Initialize the TableColumn cells
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        donsColumn.setCellValueFactory(new PropertyValueFactory<>("dons"));

        // Format the date and number cells
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateColumn.setCellFactory(column -> new TableCell<DonData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(LocalDate.parse(item)));
                }
            }
        });

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        donsColumn.setCellFactory(column -> new TableCell<DonData, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(numberFormat.format(item));
                }
            }
        });
    }

    @FXML
    public void generateReport() {
        Map<String, Object> report = reportService.generateReport();

        // Update the TableView
        List<DonData> donDataList = ((Map<String, Integer>) report.get("dons_par_date")).entrySet().stream()
                .map(entry -> new DonData(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        donsParDateTable.getItems().setAll(donDataList);

        // Update the PieChart
        Map<String, Integer> donTypeDistribution = (Map<String, Integer>) report.get("repartition_type_don");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : donTypeDistribution.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        donTypeChart.setData(pieChartData);
    }
}