module com.example.dailydashboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.net.http;
    requires com.google.gson;
    requires java.prefs;

    opens com.example.dailydashboard to javafx.fxml, com.google.gson;
    exports com.example.dailydashboard;
}