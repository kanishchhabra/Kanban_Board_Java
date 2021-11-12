package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Accordion;

public class ColumnController {

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
    void onAddTask(ActionEvent event) throws Exception {
        Accordion newColumn = FXMLLoader.load(getClass().getResource("task-view.fxml"));
        newColumnContainer.getChildren().add(newColumn);
    }

    @FXML
    void onDeleteColumn(ActionEvent event) {
        newColumnContainer.setVisible(false);
    }

    @FXML
    void onRenameColumn(ActionEvent event) {

    }

}
