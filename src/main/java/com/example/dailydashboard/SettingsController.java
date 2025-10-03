package com.example.dailydashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.prefs.Preferences;

public class SettingsController {

    @FXML private TextField userNameField;
    private Label welcomeLabel;

    public void initData(Label welcomeLabel) {
        this.welcomeLabel = welcomeLabel;
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        userNameField.setText(prefs.get("userName", "User"));
    }

    @FXML
    private void saveUserName() {
        String userName = userNameField.getText();
        if (userName != null && !userName.trim().isEmpty()) {
            Preferences prefs = Preferences.userNodeForPackage(Main.class);
            prefs.put("userName", userName);
            welcomeLabel.setText("Good Morning, " + userName + "!");
            showAlert(Alert.AlertType.INFORMATION, "Success", "User name updated successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "User name cannot be empty.");
        }
    }

    @FXML
    private void clearAllData() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete all data? This cannot be undone.", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                DataService.saveData(new AppData());
                showAlert(Alert.AlertType.INFORMATION, "Data Cleared", "All data has been cleared. Please restart the application.");
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}