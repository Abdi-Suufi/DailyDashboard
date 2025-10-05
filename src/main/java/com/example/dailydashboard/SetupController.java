package com.example.dailydashboard;

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.scene.control.Button;


public class SetupController implements Initializable {

    @FXML private JFXTextField nameField;
    @FXML private ImageView loadingGif; // Changed from JFXSpinner to ImageView
    @FXML private VBox contentVBox;

    @FXML private Button minimizeButton;
    @FXML private Button maximizeButton;
    @FXML private Button closeButton;

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Show GIF initially
        loadingGif.setVisible(true);

        // Hide content initially
        contentVBox.setOpacity(0.0);

        // Simulate a loading process
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5)); // Increased duration slightly for a better feel
        pause.setOnFinished(event -> {
            loadingGif.setVisible(false);
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

            dashboardStage.initStyle(StageStyle.TRANSPARENT);

            Scene scene = new Scene(dashboardRoot, 1200, 800);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

            // Make the window draggable
            dashboardRoot.setOnMousePressed(e -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });
            dashboardRoot.setOnMouseDragged(e -> {
                dashboardStage.setX(e.getScreenX() - xOffset);
                dashboardStage.setY(e.getScreenY() - yOffset);
            });

            dashboardStage.setScene(scene);
            dashboardStage.show();

            // Close the setup window
            Stage setupStage = (Stage) nameField.getScene().getWindow();
            setupStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void minimizeWindow(ActionEvent event) {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void maximizeWindow(ActionEvent event) {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }
}