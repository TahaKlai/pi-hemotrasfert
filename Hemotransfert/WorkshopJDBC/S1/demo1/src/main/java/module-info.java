module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
 requires mysql.connector.j;
requires java.sql;
    requires java.desktop;
    requires org.apache.pdfbox;

    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
}