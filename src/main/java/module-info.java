module com.example.thedrake.ui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.thedrake to javafx.fxml;
    exports com.example.thedrake;
    exports com.example.thedrake.ui;
    opens com.example.thedrake.ui to javafx.fxml;
}