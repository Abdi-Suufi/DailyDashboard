module com.example.dailydashboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.net.http;
    requires com.google.gson;

    opens com.example.dailydashboard to javafx.fxml;
    exports com.example.dailydashboard;
}