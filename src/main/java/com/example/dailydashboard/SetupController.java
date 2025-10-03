package com.example.dailydashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SetupController implements Initializable {

    @FXML private JFXTextField nameField;
    @FXML private JFXSpinner loadingSpinner;
    @FXML private VBox contentVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Show spinner initially
        loadingSpinner.setVisible(true);

        // Hide content initially
        contentVBox.setOpacity(0.0);

        // Simulate a loading process
        PauseTransition pause = new PauseTransition(Duration.seconds(2)); // 2-second loading animation
        pause.setOnFinished(event -> {
            loadingSpinner.setVisible(false);
            FadeTransition fadeInContent = new FadeTransition(Duration.seconds(0.8), contentVBox);
            fadeInContent.setFromValue(0.0);
            fadeInContent.setToValue(1.0);
            fadeInContent.play();
        });
        pause.play();
    }

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