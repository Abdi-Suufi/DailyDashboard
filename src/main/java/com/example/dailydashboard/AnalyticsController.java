package com.example.dailydashboard;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class AnalyticsController {

    @FXML private Label tasksCompletedLabel;
    @FXML private Label completionRateLabel;
    @FXML private Label productiveDayLabel;
    @FXML private BarChart<String, Number> weeklyChart;

    public void initData(Map<String, Integer> productivityData, List<TodoItem> todoItems) {
        // Calculate and set statistics
        long totalCompleted = todoItems.stream().filter(TodoItem::isCompleted).count();
        tasksCompletedLabel.setText(String.valueOf(totalCompleted));

        if (!todoItems.isEmpty()) {
            double rate = (double) totalCompleted / todoItems.size() * 100;
            completionRateLabel.setText(String.format("%.0f%%", rate));
        } else {
            completionRateLabel.setText("0%");
        }

        // Find the most productive day
        Optional<Map.Entry<String, Integer>> maxEntry = productivityData.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        if (maxEntry.isPresent() && maxEntry.get().getValue() > 0) {
            LocalDate date = LocalDate.parse(maxEntry.get().getKey());
            productiveDayLabel.setText(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        } else {
            productiveDayLabel.setText("-");
        }

        // Populate the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            DayOfWeek dayOfWeek = day.getDayOfWeek();
            String shortDayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateKey = day.format(DateTimeFormatter.ISO_LOCAL_DATE);
            series.getData().add(new XYChart.Data<>(shortDayName, productivityData.getOrDefault(dateKey, 0)));
        }
        weeklyChart.getData().add(series);
    }
}