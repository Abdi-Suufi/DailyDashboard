package com.example.dailydashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

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
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);
        } else {
            // Not the first run, show main dashboard
            fxmlFile = "Dashboard.fxml";
            title = "Daily Dashboard";
            width = 1200;
            height = 800;
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
        primaryStage.setTitle(title);

        // Make the stage transparent
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        // Apply dark theme if it's enabled
        if (prefs.getBoolean("darkMode", false)) {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("dark-theme.css")).toExternalForm());
        }

        // Make the window draggable by its root node
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}