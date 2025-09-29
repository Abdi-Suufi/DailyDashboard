package com.example.dailydashboard;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label dateLabel;
    @FXML private Label weatherLabel;
    @FXML private Label tasksLabel;
    @FXML private AreaChart<String, Number> productivityChart;
    @FXML private JFXListView<TodoItem> todoListView;
    @FXML private JFXTextField taskTextField;
    @FXML private JFXTextArea notesArea;

    private ObservableList<TodoItem> todoItems;
    private Map<String, Integer> productivityData = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set Welcome and Date
        welcomeLabel.setText("Good Morning, User!");
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));

        // Set weather data
        String weatherData = WeatherService.getWeatherData();
        weatherLabel.setText(WeatherService.parseWeatherData(weatherData));

        loadData();
        setupTodoListView();
        updateTasksLabel();
        updateProductivityChart();
        addContextMenuToTodo();

        // Add listener to save notes when focus is lost
        notesArea.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Focus lost
                saveData();
            }
        });
    }

    private void setupTodoListView() {
        todoListView.setItems(todoItems);
        todoListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            item.completedProperty().addListener((obs, wasCompleted, isNowCompleted) -> {
                if (isNowCompleted) {
                    item.setCompletionDate(LocalDate.now());
                    String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
                    productivityData.put(today, productivityData.getOrDefault(today, 0) + 1);
                } else {
                    // If a task is unchecked, remove its contribution from the chart
                    if (item.getCompletionDate() != null) {
                        String completionDay = item.getCompletionDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                        productivityData.put(completionDay, productivityData.getOrDefault(completionDay, 1) - 1);
                        item.setCompletionDate(null);
                    }
                }
                updateTasksLabel();
                updateProductivityChart();
                saveData();
            });
            return item.completedProperty();
        }));
    }


    private void updateTasksLabel() {
        long completedCount = todoItems.stream().filter(TodoItem::isCompleted).count();
        tasksLabel.setText(String.format("%d / %d", completedCount, todoItems.size()));
    }

    private void updateProductivityChart() {
        productivityChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tasks Completed");

        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            DayOfWeek dayOfWeek = day.getDayOfWeek();
            String shortDayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateKey = day.format(DateTimeFormatter.ISO_LOCAL_DATE);
            series.getData().add(new XYChart.Data<>(shortDayName, productivityData.getOrDefault(dateKey, 0)));
        }

        productivityChart.getData().add(series);
    }

    @FXML
    private void addTask() {
        String taskText = taskTextField.getText();
        if (taskText != null && !taskText.trim().isEmpty()) {
            todoItems.add(new TodoItem(taskText, false));
            taskTextField.clear();
            updateTasksLabel();
            saveData();
        }
    }

    private void addContextMenuToTodo() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                todoItems.remove(selectedItem);
                updateTasksLabel();
                saveData();
            }
        });
        contextMenu.getItems().add(deleteItem);
        todoListView.setContextMenu(contextMenu);
    }

    private void saveData() {
        AppData appData = new AppData();
        appData.setTodoItems(new ArrayList<>(todoItems));
        appData.setNotes(notesArea.getText());
        appData.setProductivityData(productivityData);
        DataService.saveData(appData);
    }

    private void loadData() {
        AppData appData = DataService.loadData();
        if (appData == null) {
            todoItems = FXCollections.observableArrayList();
            notesArea.setText("");
            productivityData = new HashMap<>();
        } else {
            todoItems = FXCollections.observableArrayList(appData.getTodoItems());
            notesArea.setText(appData.getNotes() != null ? appData.getNotes() : "");
            productivityData = appData.getProductivityData() != null ? appData.getProductivityData() : new HashMap<>();
        }
    }
}