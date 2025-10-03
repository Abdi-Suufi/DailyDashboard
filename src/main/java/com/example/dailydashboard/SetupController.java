package com.example.dailydashboard;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class SetupController {

    @FXML
    private JFXTextField nameField;

    @FXML
    void getStarted(ActionEvent event) {
        String name = nameField.getText();
        if (name == null || name.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your name to continue.");
            alert.showAndWait();
            return;
        }

        // Save the name
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        prefs.put("userName", name);

        // Load and show the main dashboard
        try {
            Parent dashboardRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Daily Dashboard");
            Scene scene = new Scene(dashboardRoot, 1200, 800);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
            dashboardStage.setScene(scene);
            dashboardStage.show();

            // Close the setup window
            Stage setupStage = (Stage) nameField.getScene().getWindow();
            setupStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}