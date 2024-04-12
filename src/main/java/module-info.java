module com.example.harjoitustyo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.harjoitustyo to javafx.fxml;
    exports com.example.harjoitustyo;
}