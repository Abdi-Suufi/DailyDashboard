package com.example.dailydashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.prefs.Preferences;

public class SettingsController {

    @FXML private TextField userNameField;
    @FXML private TextField weatherLocationField;
    private Label welcomeLabel;
    private Label weatherLabel;

    public void initData(Label welcomeLabel, Label weatherLabel) {
        this.welcomeLabel = welcomeLabel;
        this.weatherLabel = weatherLabel;
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        userNameField.setText(prefs.get("userName", "User"));
        weatherLocationField.setText(prefs.get("weatherLocation", "Bristol, UK"));
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
    private void saveWeatherLocation() {
        String location = weatherLocationField.getText();
        if (location == null || location.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Weather location cannot be empty.");
            return;
        }

        // Attempt to get weather data first
        String weatherData = WeatherService.getWeatherData(location);

        if (weatherData != null) {
            // Success! Data was fetched. Now save and update UI.
            Preferences prefs = Preferences.userNodeForPackage(Main.class);
            prefs.put("weatherLocation", location);
            weatherLabel.setText(WeatherService.parseWeatherData(weatherData, location));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Weather location updated successfully.");
        } else {
            // Failure. Show an error message.
            showAlert(Alert.AlertType.ERROR, "Error", "Could not find weather for '" + location + "'. Please check the location and try again.");
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