module com.example.dailydashboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;

    opens com.example.dailydashboard to javafx.fxml;
    exports com.example.dailydashboard;
}