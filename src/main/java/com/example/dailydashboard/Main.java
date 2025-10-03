package com.example.dailydashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String userName = prefs.get("userName", null);

        String fxmlFile;
        String title;
        double width;
        double height;

        if (userName == null) {
            // First run, show setup screen
            fxmlFile = "Setup.fxml";
            title = "Welcome!";
            width = 600;
            height = 400;
        } else {
            // Not the first run, show main dashboard
            fxmlFile = "Dashboard.fxml";
            title = "Daily Dashboard";
            width = 1200;
            height = 800;
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
        primaryStage.setTitle(title);
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}