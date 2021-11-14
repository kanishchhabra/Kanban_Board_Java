package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ControllerWorkspace {

    //From the Workspace View
    @FXML
    private StackPane workspaceStackPane;

    @FXML
    private BorderPane workSpaceArea;

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

    //Create Project View
    @FXML
    private VBox createProjectWindow;

    @FXML
    private TextField projectName;

    @FXML
    private Button createProject;

    @FXML
    private TextArea statusArea;


    @FXML
    private Button closeAddProject;

    //Create Project View Functions
    @FXML
    void onCreateProject(ActionEvent event) {
        Connection conn = getConnection();
        //Following code creates adds a new project to DB. Displays the errors in the status area
        try{
            String sql = "INSERT INTO Projects (projectName, username) VALUES (?, (SELECT username FROM Users WHERE  username = ? ))";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, projectName.getText());
            pstmt.setString(2, KanbanLauncher.loggedUser.getUsername());
            pstmt.executeUpdate();

            statusArea.setText("Project Created");
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));


            //Creates new project window on the GUI
            Tab newProject = FXMLLoader.load(getClass().getResource("project-view.fxml"));
            newProject.setText(projectName.getText());

            //Moving up the hierarchy to add new tab
            StackPane stackPane = (StackPane) createProjectWindow.getParent();
            stackPane.getChildren().remove(createProjectWindow);
            BorderPane borderPane =(BorderPane) stackPane.getChildren().get(0);
            borderPane.setDisable(false);
            BorderPane borderPaneArea = (BorderPane) stackPane.getChildren().get(0);
            TabPane tabArea = (TabPane) borderPaneArea.getCenter();
            tabArea.getTabs().add(newProject);

            conn.close();



        }  catch (Exception e){
            statusArea.setVisible(true);
            statusArea.setText(e.getClass().getName() + ": " + e.getMessage());
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }

    }

    @FXML
    void onCloseAddProject(ActionEvent event) {
        StackPane stackPane = (StackPane) createProjectWindow.getParent();
        stackPane.getChildren().remove(createProjectWindow);
        BorderPane borderPane =(BorderPane) stackPane.getChildren().get(0);
        borderPane.setDisable(false);
    }

    // Workspace View Functions
    @FXML
    void onNewProject(ActionEvent event) throws Exception {
            VBox createProject = FXMLLoader.load(getClass().getResource("add-project-view.fxml"));
            BorderPane borderPane =(BorderPane) workspaceStackPane.getChildren().get(0);
            borderPane.setDisable(true);
            workspaceStackPane.getChildren().add(createProject);
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

    //Following method creates the connection to database
    private Connection getConnection(){
        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:kanbanDB.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return conn;
    }
}

