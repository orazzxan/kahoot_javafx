module com.example.project3f {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project3f to javafx.fxml;
    exports com.example.project3f;
}