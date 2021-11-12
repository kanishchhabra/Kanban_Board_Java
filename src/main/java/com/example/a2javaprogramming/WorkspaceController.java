package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WorkspaceController {


    @FXML
    private Menu workspaceMenu;

    @FXML
    private MenuItem newProject;

    @FXML
    private Menu projectMenu;

    @FXML
    private MenuItem newColumn;

    @FXML
    private TabPane workspaceTabArea;

    @FXML
    private Tab newProjectTab;


    @FXML
    void onNewProject(ActionEvent event) throws Exception {
        Tab newProject = FXMLLoader.load(getClass().getResource("project-view.fxml"));
        workspaceTabArea.getTabs().add(newProject);
    }

    @FXML
    void onNewColumn(ActionEvent event) throws Exception {
        if (!workspaceTabArea.getTabs().isEmpty()) {
            VBox newColumn = FXMLLoader.load(getClass().getResource("column-view.fxml"));
            Tab currentTabContent = workspaceTabArea.getSelectionModel().getSelectedItem();
            ScrollPane scrollPaneContent = (ScrollPane) currentTabContent.getContent();
            HBox containerColumn = (HBox) scrollPaneContent.getContent();
            containerColumn.getChildren().add(newColumn);
        }
    }
}

