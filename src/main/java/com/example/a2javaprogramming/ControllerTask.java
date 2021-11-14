package com.example.a2javaprogramming;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControllerTask {


    @FXML
    private VBox taskContainer;

    @FXML
    private Accordion newTask;

    @FXML
    private TitledPane taskPane;

    @FXML
    private TextArea taskDescription;

    @FXML
    private DatePicker dueDate;

    @FXML
    private TextField taskStatus;

    @FXML
    private ProgressBar taskProgress;

    @FXML
    private Hyperlink viewTaskList;

    @FXML
    private Hyperlink columnDelete;

    @FXML
    private Hyperlink saveTask;

    @FXML
    void onColumnDelete(ActionEvent event) {
        VBox tasksContainer = (VBox)newTask.getParent();
        tasksContainer.getChildren().remove(newTask);
    }

    @FXML
    void onSaveTask(ActionEvent event) {

    }

    @FXML
    void onViewTaskList(ActionEvent event) {

    }

}
