package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Accordion;


public class ControllerColumn {

    @FXML
    private VBox newColumnContainer;

    @FXML
    private HBox columnHeading;

    @FXML
    private SplitMenuButton addTask;

    @FXML
    private MenuItem deleteColumn;

    @FXML
    private MenuItem renameColumn;

    @FXML
    private VBox tasksContainer;

    @FXML
    void onAddTask(ActionEvent event) throws Exception {
        Accordion newTask = FXMLLoader.load(getClass().getResource("task-view.fxml"));
        tasksContainer.getChildren().add(newTask);
    }

    @FXML
    void onDeleteColumn(ActionEvent event) {
        newColumnContainer.getChildren().removeAll(newColumnContainer.getChildren());
    }

    @FXML
    void onRenameColumn(ActionEvent event) {

    }

}
