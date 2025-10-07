package com.example.dailydashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
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
import java.util.prefs.Preferences;


public class DashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label dateLabel;
    @FXML private Label weatherLabel;
    @FXML private Label tasksLabel;
    @FXML private AreaChart<String, Number> productivityChart;
    @FXML private JFXListView<TodoItem> todoListView;
    @FXML private JFXTextField taskTextField;
    @FXML private JFXTextArea notesArea;
    @FXML private VBox mainContent;
    @FXML private StackPane contentArea;

    // Window control buttons
    @FXML private Button minimizeButton;
    @FXML private Button maximizeButton;
    @FXML private Button closeButton;

    // Sidebar navigation buttons
    @FXML private JFXButton homeButton;
    @FXML private JFXButton analyticsButton;
    @FXML private JFXButton tasksButton;
    @FXML private JFXButton settingsButton;

    private Node homeView;
    private JFXButton selectedButton; // To track the currently selected button

    private ObservableList<TodoItem> todoItems;
    private Map<String, Integer> productivityData = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeView = mainContent;

        // Set initial selected button
        selectedButton = homeButton;
        selectedButton.getStyleClass().add("sidebar-button-selected");

        // Set Welcome and Date
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String userName = prefs.get("userName", "User");
        welcomeLabel.setText("Good Morning, " + userName + "!");
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));

        // Set weather data
        String location = prefs.get("weatherLocation", "Bristol, UK");
        String weatherData = WeatherService.getWeatherData(location);
        weatherLabel.setText(WeatherService.parseWeatherData(weatherData, location));

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

        // Add a fade-in animation to the main content
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), mainContent);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
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
            TodoItem newItem = new TodoItem(taskText, false);
            todoItems.add(newItem);
            taskTextField.clear();
            updateTasksLabel();
            saveData();

            final int index = todoListView.getItems().size() - 1;
            todoListView.scrollTo(index);
            Node node = todoListView.lookup(".list-cell:indexed(" + index + ")");
            if (node != null) {
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), node);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
            }
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
            todoItems = FXCollections.observableArrayList(appData.getTodoItems() != null ? appData.getTodoItems() : new ArrayList<>());
            todoItems.forEach(item -> item.textProperty()); // This initializes the transient properties after loading
            todoItems.forEach(item -> item.completedProperty());
            notesArea.setText(appData.getNotes() != null ? appData.getNotes() : "");
            productivityData = appData.getProductivityData() != null ? appData.getProductivityData() : new HashMap<>();
        }
    }

    @FXML
    private void handleNavClick(ActionEvent event) {
        // Remove style from the previously selected button
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("sidebar-button-selected");
        }

        // Update the selected button and apply the new style
        JFXButton clickedButton = (JFXButton) event.getSource();
        selectedButton = clickedButton;
        selectedButton.getStyleClass().add("sidebar-button-selected");

        String id = clickedButton.getId();
        switch (id) {
            case "homeButton":
                updateTasksLabel(); // Update dashboard stats when returning home
                updateProductivityChart();
                setView(homeView);
                break;
            case "analyticsButton":
                loadView("Analytics.fxml");
                break;
            case "tasksButton":
                loadView("Tasks.fxml");
                break;
            case "settingsButton":
                loadView("Settings.fxml");
                break;
        }
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node view = loader.load();
            Object controller = loader.getController();

            if (controller instanceof AnalyticsController) {
                ((AnalyticsController) controller).initData(productivityData, todoItems);
            } else if (controller instanceof TasksController) {
                ((TasksController) controller).initData(todoItems);
            } else if (controller instanceof SettingsController) {
                ((SettingsController) controller).initData(welcomeLabel, weatherLabel);
            }

            setView(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setView(Node view) {
        contentArea.getChildren().setAll(view);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), view);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @FXML
    private void minimizeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void maximizeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}