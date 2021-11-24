package com.example.a2javaprogramming;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProjectRefresh {
    public static TextArea statusArea = (TextArea) KanbanLauncher.workspaceBorderPane.getBottom();

    public void completeRefresh(String uName, String pass){

        Connection conn = getConnection();
        //Following code creates a new user. If the user already exists, it shows an error
        try{
            //creates account only if username is unique.
            String sql = "SELECT * FROM Users WHERE  username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uName);
            pstmt.setString(2, pass);
            ResultSet account = pstmt.executeQuery();

            if (account.next() == true){
                TabPane tabPane = (TabPane)KanbanLauncher.workspaceBorderPane.getCenter();
                tabPane.getTabs().clear();

                sql = "SELECT * FROM Projects WHERE  username = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, uName);
                ResultSet projects = pstmt.executeQuery();

                //Iterating over projects Refresh projects
                while (projects.next()){
                    //Gets the project from global Hash Map for Projects
                    Tab currentProject = KanbanLauncher.Projects.get(projects.getInt("projectId"));
                    currentProject.setId(String.valueOf(projects.getInt("projectId")));
                    currentProject.setText(projects.getString("projectName"));

                    //Refreshing the Hash Map
                    KanbanLauncher.Projects.replace(projects.getInt("projectId"), currentProject);

                    //Fetching the HBox to add columns
                    HBox projectColumns = (HBox) ((ScrollPane) currentProject.getContent()).getContent();
                    //Clearing the container
                    projectColumns.getChildren().clear();
                    sql = "SELECT * FROM Columns WHERE  projectId = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, projects.getInt("projectId"));
                    ResultSet columns = pstmt.executeQuery();

                    //Iterating over Columns to refresh Columns
                    while (columns.next()){
                        VBox currentColumn = KanbanLauncher.Columns.get(columns.getInt("columnId"));
                        currentColumn.setId(Integer.toString(columns.getInt("columnId")));
                        Label columnName = (Label) ((HBox) currentColumn.getChildren().get(0)).getChildren().get(0);
                        columnName.setText(columns.getString("columnName"));

                        //Refreshing the Hash Map
                        KanbanLauncher.Columns.replace(columns.getInt("columnId"), currentColumn);

                        //Adding column on the GUI
                        projectColumns.getChildren().add(currentColumn);

                        //Iterating Over Tasks to set Tasks
                        VBox columnTasks = (VBox) currentColumn.getChildren().get(1);
                        //Clearing the container
                        columnTasks.getChildren().clear();

                        sql = "SELECT * FROM Tasks WHERE  columnId = ?";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, columns.getInt("columnId"));
                        ResultSet tasks = pstmt.executeQuery();
                        //Iterating over Columns
                        while (tasks.next()) {
                            Accordion newTask = KanbanLauncher.Tasks.get(tasks.getInt("taskId"));
                            VBox task = (VBox) newTask.getPanes().get(0).getContent();

                            //Setting Status
                            ChoiceBox status = (ChoiceBox) task.getChildren().get(5);
                            status.getItems().add("Yet to Start");
                            status.getItems().add("Ongoing");
                            status.getItems().add("Completed");

                            //Setting Status
                            ChoiceBox taskColumns = (ChoiceBox) task.getChildren().get(9);
                            sql = "SELECT * FROM Columns WHERE  projectId = ?";
                            pstmt = conn.prepareStatement(sql);
                            pstmt.setInt(1, projects.getInt("projectId"));
                            ResultSet iterableColumns = pstmt.executeQuery();
                            while (iterableColumns.next()){
                                taskColumns.getItems().add(Integer.toString(iterableColumns.getInt("columnId")) + "  :  " + iterableColumns.getString("columnName"));
                                if (iterableColumns.getInt("columnId") == columns.getInt("columnId")){
                                    taskColumns.setValue(Integer.toString(iterableColumns.getInt("columnId")) + "  :  " + iterableColumns.getString("columnName"));
                                }
                            }

                            //Setting ID on the GUI
                            newTask.setId(Integer.toString(tasks.getInt("taskId")));

                            //Setting Name on the Title Pane in Accordion
                            TitledPane taskPane = newTask.getPanes().get(0);
                            taskPane.setText(tasks.getString("taskName"));

                            //Setting GUI values
                            VBox taskContainer = (VBox) taskPane.getContent();
                            ((TextArea) taskContainer.getChildren().get(1)).setText(tasks.getString("taskDescription"));
                            ((ChoiceBox) taskContainer.getChildren().get(5)).setValue(tasks.getString("taskStatus"));
                            ((DatePicker) taskContainer.getChildren().get(3)).setValue((tasks.getDate("dueDate")).toLocalDate());
                            ((TextField) taskContainer.getChildren().get(7)).setText(tasks.getString("taskName"));

                            //Refreshing the Hash Map
                            KanbanLauncher.Tasks.replace(tasks.getInt("taskId"), newTask);

                            //Adding tasks on the GUI
                            columnTasks.getChildren().add(newTask);
                        }
                    }
                }

                for (Tab project: KanbanLauncher.Projects.values()) {
                    tabPane.getTabs().add(project);
                }
                conn.close();


            }else {
                statusArea.setText("Invalid Credentials");
                statusArea.setWrapText(true);
                statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
            }

        }  catch (Exception e){
            System.out.println(e.toString());
            statusArea.setText(e.getMessage());
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }
    }

    public static Connection getConnection(){
        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:kanbanDB.db");
        } catch ( Exception e ) {
            System.exit(0);
        }
        return conn;
    }
}
