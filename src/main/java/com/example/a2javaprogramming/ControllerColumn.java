package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ControllerColumn {
    //Controls from Column View
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

    //----------------------------------------------------------------------------------------
    //Controls from Create Column
    @FXML
    private VBox createColumnWindow;

    @FXML
    private TextField columnName;

    @FXML
    private Button createColumn;

    @FXML
    private Button closeAddColumn;

    @FXML
    private TextArea statusArea;

    //----------------------------------------------------------------------------------------
    //Controls from Renaming Column
    @FXML
    private VBox renameColumnWindow;

    @FXML
    private TextField columnNewName;

    @FXML
    private Button updateColumn;

    @FXML
    private Button closeRenameColumn;

    @FXML
    private TextArea renameStatusArea;

    //----------------------------------------------------------------------------------------
    //Functions from Column
    @FXML
    void onAddTask(ActionEvent event) throws Exception {
        Accordion newTask = FXMLLoader.load(getClass().getResource("task-view.fxml"));
        tasksContainer.getChildren().add(newTask);
    }

    @FXML
    void onDeleteColumn(ActionEvent event) {
        newColumnContainer.getChildren().removeAll(newColumnContainer.getChildren());
        Connection conn = getConnection();
        try {
            String sql = "DELETE FROM Columns WHERE columnId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, KanbanLauncher.currentColumn.getColumnId());
            pstmt.executeUpdate();
        }catch (Exception e){   }
    }

    @FXML
    void onRenameColumn(ActionEvent event) throws Exception {
        VBox renameColumn = FXMLLoader.load(getClass().getResource("rename-column-view.fxml"));
        KanbanLauncher.workspaceBorderPane.setDisable(true);
        KanbanLauncher.workspaceStackPane.getChildren().add(renameColumn);
    }

    //----------------------------------------------------------------------------------------
    //Functions from Create New Column
    @FXML
    void onCloseAddColumn(ActionEvent event) {
        //Getting to the Tab Pane
        StackPane workspaceStackPane = (StackPane) createColumnWindow.getParent();
        BorderPane borderPane = (BorderPane) workspaceStackPane.getChildren().get(0);
        TabPane workspaceTabArea = (TabPane) borderPane.getCenter();

        //Closing the column window
        createColumnWindow.setVisible(false);
        borderPane.setDisable(false);
        workspaceStackPane.getChildren().remove(createColumnWindow);

    }

    @FXML
    void onCreateColumn(ActionEvent event) throws Exception {
            Connection conn = getConnection();
            //Following code creates adds a new column to DB. Displays the errors in the status area
            try{
                //Getting to the Tab Pane
                StackPane workspaceStackPane = (StackPane) createColumnWindow.getParent();
                BorderPane borderPane = (BorderPane) workspaceStackPane.getChildren().get(0);
                TabPane workspaceTabArea = (TabPane) borderPane.getCenter();

                if (!workspaceTabArea.getTabs().isEmpty()) {
                    //Getting all the relevant controls
                    Tab currentTabContent = workspaceTabArea.getSelectionModel().getSelectedItem();
                    ScrollPane scrollPaneContent = (ScrollPane) currentTabContent.getContent();
                    HBox containerColumn = (HBox) scrollPaneContent.getContent();

                    //Inserting the new column into database
                    String sql = "INSERT INTO Columns (columnName, projectId) VALUES (?, (SELECT projectId FROM Projects WHERE  projectId = ? ))";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, columnName.getText());
                    pstmt.setInt(2, Integer.valueOf(currentTabContent.getId()));
                    pstmt.executeUpdate();

                    //Getting the new column created
                    sql = "SELECT * FROM Columns WHERE projectId = (SELECT projectId FROM Projects WHERE  projectId = ? ) ORDER BY columnId DESC LIMIT 1";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, Integer.valueOf(currentTabContent.getId()));
                    ResultSet column = pstmt.executeQuery();


                    statusArea.setText("Column Created");
                    statusArea.setWrapText(true);
                    statusArea.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));


                    //Creates new column window on the GUI
                    VBox newColumn = FXMLLoader.load(getClass().getResource("column-view.fxml"));
                    HBox columnContent = (HBox) newColumn.getChildren().get(0);
                    Label columnTitle = (Label) columnContent.getChildren().get(0);
                    columnTitle.setText(columnName.getText());

                    //Setting ID to th new column:
                    while (column.next()){
                        newColumn.setId(String.valueOf(column.getInt("columnId")));
                    }

                    //Showing new column on GUI and Hash Map
                    containerColumn.getChildren().add(newColumn);
                    KanbanLauncher.Columns.put(Integer.valueOf(newColumn.getId()), newColumn);

                    //Closing the column window
                    createColumnWindow.setVisible(false);
                    borderPane.setDisable(false);
                    workspaceStackPane.getChildren().remove(createColumnWindow);

                    //Closing connection
                    conn.close();
                }

            }  catch (Exception e){
                statusArea.setVisible(true);
                statusArea.setText(e.getClass().getName() + ": " + e.getMessage());
                statusArea.setWrapText(true);
                statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
            }
        }

    //----------------------------------------------------------------------------------------
    //Functions from Renaming Column
    @FXML
    void onCloseRenameColumn(ActionEvent event) {
        KanbanLauncher.workspaceBorderPane.setDisable(false);
        KanbanLauncher.workspaceStackPane.getChildren().remove(renameColumnWindow);
    }

    @FXML
    void onUpdateColumn(ActionEvent event) {
        Connection conn = getConnection();
        try{
            String sql = "UPDATE Columns SET columnName = ? WHERE columnId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, columnNewName.getText());
            pstmt.setInt(2, KanbanLauncher.currentColumn.getColumnId());
            pstmt.executeUpdate();

            //Creates new column window on the GUI
            VBox currentColumn = KanbanLauncher.Columns.get(KanbanLauncher.currentColumn.getColumnId());
            Label columnName = (Label)((HBox) currentColumn.getChildren().get(0)).getChildren().get(0);
            columnName.setText(columnNewName.getText());

            //Updating Current Column
            KanbanLauncher.currentColumn.setColumnName(columnNewName.getText());

            //Updating GUI
            KanbanLauncher.workspaceBorderPane.setDisable(false);
            KanbanLauncher.workspaceStackPane.getChildren().remove(renameColumnWindow);

            conn.close();



        }  catch (Exception e){
            renameStatusArea.setVisible(true);
            renameStatusArea.setText(e.getClass().getName() + ": " + e.getMessage());
            renameStatusArea.setWrapText(true);
            renameStatusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }

    }

    //----------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------
    //Sets Current Column
    public void currentColumnSetter() {
        try{
            Connection conn = getConnection();
            //Getting the new column created
            String sql = "SELECT * FROM Columns WHERE columnId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.valueOf(newColumnContainer.getId()));
            ResultSet column = pstmt.executeQuery();
            while (column.next()){
                KanbanLauncher.currentColumn.setColumnId(column.getInt("columnId"));
                KanbanLauncher.currentColumn.setColumnName(column.getString("columnName"));
                KanbanLauncher.currentColumn.setProjectId(column.getInt("projectId"));
            }

        }catch (Exception e){

        }
    }
}
