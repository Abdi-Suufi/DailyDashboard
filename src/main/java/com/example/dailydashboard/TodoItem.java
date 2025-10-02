package com.example.dailydashboard;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class TodoItem {
    private transient StringProperty text;
    private transient BooleanProperty completed;
    private LocalDate completionDate;

    private String textValue;
    private boolean completedValue;

    public TodoItem(String text, boolean completed) {
        this.textValue = text;
        this.completedValue = completed;
        this.text = new SimpleStringProperty(text);
        this.completed = new SimpleBooleanProperty(completed);
    }

    public String getText() {
        if (text == null) { // Happens after deserialization before properties are initialized
            return textValue;
        }
        return text.get();
    }

    public void setText(String text) {
        if (this.text == null) {
            this.text = new SimpleStringProperty();
        }
        this.text.set(text);
        this.textValue = text;
    }


    public StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(textValue);
        }
        return text;
    }

    public boolean isCompleted() {
        if (completed == null) { // Happens after deserialization before properties are initialized
            return completedValue;
        }
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        if (this.completed == null) {
            this.completed = new SimpleBooleanProperty();
        }
        this.completed.set(completed);
        this.completedValue = completed;
    }

    public BooleanProperty completedProperty() {
        if (completed == null) {
            completed = new SimpleBooleanProperty(completedValue);
        }
        return completed;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    @Override
    public String toString() {
        return getText(); // This tells the ListView to display the task's text
    }
}