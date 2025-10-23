module com.example.cinemamarket {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires javafx.media;

    opens com.example.cinemamarket to javafx.fxml;
    exports com.example.cinemamarket;
}