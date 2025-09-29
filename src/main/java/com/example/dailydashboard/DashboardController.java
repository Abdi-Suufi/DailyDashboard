package com.example.dailydashboard;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.CheckBoxListCell;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label weatherLabel;

    @FXML
    private Label tasksLabel;

    @FXML
    private AreaChart<String, Number> productivityChart;

    @FXML
    private JFXListView<String> todoListView;

    @FXML
    private JFXTextField taskTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set Welcome and Date
        welcomeLabel.setText("Good Morning, User!");
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));

        // Set weather data
        String weatherData = WeatherService.getWeatherData();
        weatherLabel.setText(WeatherService.parseWeatherData(weatherData));

        tasksLabel.setText("5 / 8");

        // Populate To-Do List
        populateTodo();

        // Populate Productivity Chart
        populateChart();

        // Add context menu for deleting tasks
        addContextMenuToTodo();
    }

    private void populateTodo() {
        ObservableList<String> todoItems = FXCollections.observableArrayList(
                "Finish project proposal",
                "Call the design team",
                "Schedule a meeting for Friday",
                "Reply to important emails",
                "Workout session"
        );
        todoListView.setItems(todoItems);
        todoListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            // In a real app, you would handle the checkbox state changes
            return new javafx.beans.property.SimpleBooleanProperty();
        }));
    }

    private void populateChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Productivity");

        series.getData().add(new XYChart.Data<>("Mon", 8));
        series.getData().add(new XYChart.Data<>("Tue", 5));
        series.getData().add(new XYChart.Data<>("Wed", 10));
        series.getData().add(new XYChart.Data<>("Thu", 7));
        series.getData().add(new XYChart.Data<>("Fri", 9));
        series.getData().add(new XYChart.Data<>("Sat", 4));
        series.getData().add(new XYChart.Data<>("Sun", 6));

        productivityChart.getData().add(series);
    }
    @FXML
    private void addTask() {
        String task = taskTextField.getText();
        if (task != null && !task.isEmpty()) {
            todoListView.getItems().add(task);
            taskTextField.clear();
        }
    }
    private void addContextMenuToTodo() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            String selectedItem = todoListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                todoListView.getItems().remove(selectedItem);
            }
        });
        contextMenu.getItems().add(deleteItem);
        todoListView.setContextMenu(contextMenu);
    }

}