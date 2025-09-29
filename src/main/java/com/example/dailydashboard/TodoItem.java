package com.example.dailydashboard;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class TodoItem {
    private final StringProperty text = new SimpleStringProperty();
    private final BooleanProperty completed = new SimpleBooleanProperty();
    private LocalDate completionDate;

    // This field is necessary for Gson to serialize/deserialize the properties correctly
    private transient String textValue;
    private transient boolean completedValue;

    public TodoItem(String text, boolean completed) {
        setText(text);
        setCompleted(completed);
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
        this.textValue = text;
    }


    public StringProperty textProperty() {
        return text;
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
        this.completedValue = completed;
    }

    public BooleanProperty completedProperty() {
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