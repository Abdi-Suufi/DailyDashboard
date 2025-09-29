package com.example.dailydashboard;

import java.util.List;
import java.util.Map;

public class AppData {
    private List<TodoItem> todoItems;
    private String notes;
    private Map<String, Integer> productivityData;

    // Getters and Setters
    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Map<String, Integer> getProductivityData() {
        return productivityData;
    }

    public void setProductivityData(Map<String, Integer> productivityData) {
        this.productivityData = productivityData;
    }
}