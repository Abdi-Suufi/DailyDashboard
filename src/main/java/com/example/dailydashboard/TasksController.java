package com.example.dailydashboard;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.CheckBoxListCell;

import java.util.function.Predicate;

public class TasksController {

    @FXML private JFXListView<TodoItem> todoListView;
    @FXML private JFXTextField taskTextField;

    private ObservableList<TodoItem> sourceTodoItems;
    private FilteredList<TodoItem> filteredTodoItems;

    public void initData(ObservableList<TodoItem> todoItems) {
        this.sourceTodoItems = todoItems;
        this.filteredTodoItems = new FilteredList<>(sourceTodoItems);

        todoListView.setItems(filteredTodoItems);
        todoListView.setCellFactory(CheckBoxListCell.forListView(TodoItem::completedProperty));
    }

    @FXML
    private void addTask() {
        String taskText = taskTextField.getText();
        if (taskText != null && !taskText.trim().isEmpty()) {
            sourceTodoItems.add(new TodoItem(taskText, false));
            taskTextField.clear();
        }
    }

    @FXML
    private void showAllTasks() {
        filteredTodoItems.setPredicate(null);
    }

    @FXML
    private void showPendingTasks() {
        filteredTodoItems.setPredicate(item -> !item.isCompleted());
    }

    @FXML
    private void showCompletedTasks() {
        filteredTodoItems.setPredicate(TodoItem::isCompleted);
    }

    @FXML
    private void clearCompletedTasks() {
        sourceTodoItems.removeIf(TodoItem::isCompleted);
    }
}