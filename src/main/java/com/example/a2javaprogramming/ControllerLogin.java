package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;

public class ControllerLogin {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Button closeButton;

    @FXML
    private Hyperlink signUp;

    @FXML
    private TextArea statusArea;

    @FXML
    void onClose(ActionEvent event) {
        try{
            // get a handle to the stage
            Stage stage = (Stage) closeButton.getScene().getWindow();
            // do what you have to do
            stage.close();
        }
        catch (Exception e) {
            statusArea.setText(e.getMessage());
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        }
    }
    @FXML
    void onLogin(ActionEvent event) {
        if (username.getText().isEmpty() || password.getText().isEmpty()){
            statusArea.setText("Invalid Credentials");
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }
        else{
            String uName = username.getText();
            String pass = password.getText();
            String fName = "";
            String lName = "";

            Connection conn = getConnection();
            //Following code creates a new user. If the user already exists, it shows an error
            try{
                //creates account only if username is unique.
                String sql = "SELECT * FROM Users WHERE  username = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, uName);
                pstmt.setString(2, pass);
                ResultSet account = pstmt.executeQuery();

                statusArea.setText("Executing Query");
                statusArea.setWrapText(true);

                if (account.next() == true){
                    fName = account.getString("firstName");
                    lName = account.getString("lastName");

                    //Sets properties of the logged-in user
                    KanbanLauncher.loggedUser.setFirstName(fName);
                    KanbanLauncher.loggedUser.setLastName(lName);
                    KanbanLauncher.loggedUser.setUsername(uName);
                    KanbanLauncher.loggedUser.setPassword(pass);

                    statusArea.setText("Login Successful");
                    statusArea.setWrapText(true);
                    statusArea.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));

                    StackPane stackPane = FXMLLoader.load(getClass().getResource("workspace-view.fxml"));
                    KanbanLauncher.workspaceBorderPane = (BorderPane)stackPane.getChildren().get(0);
                    KanbanLauncher.workspaceStackPane = stackPane;

                    VBox topBarContainer = (VBox) KanbanLauncher.workspaceBorderPane.getTop();
                    HBox topBar = (HBox) topBarContainer.getChildren().get(1);
                    HBox infoBar = (HBox) topBar.getChildren().get(1);
                    Label firstName = (Label) infoBar.getChildren().get(1);
                    firstName.setText(KanbanLauncher.loggedUser.getFirstName());

                    TabPane tabPane = (TabPane)KanbanLauncher.workspaceBorderPane.getCenter();

                    sql = "SELECT * FROM Projects WHERE  username = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, uName);
                    ResultSet projects = pstmt.executeQuery();

                    //Iterating over projects to add new projects
                    while (projects.next()){
                        Tab newProject = FXMLLoader.load(getClass().getResource("project-view.fxml"));
                        newProject.setId(String.valueOf(projects.getInt("projectId")));
                        newProject.setText(projects.getString("projectName"));
                        KanbanLauncher.Projects.put(projects.getInt("projectId"), newProject);

                        //Fetching the HBox to add columns
                        HBox projectColumns = (HBox) ((ScrollPane) newProject.getContent()).getContent();
                        sql = "SELECT * FROM Columns WHERE  projectId = ?";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, projects.getInt("projectId"));
                        ResultSet columns = pstmt.executeQuery();

                        //Iterating over Columns to add new Columns
                        while (columns.next()){
                            VBox newColumn = FXMLLoader.load(getClass().getResource("column-view.fxml"));
                            newColumn.setId(Integer.toString(columns.getInt("columnId")));
                            Label columnName = (Label) ((HBox) newColumn.getChildren().get(0)).getChildren().get(0);
                            columnName.setText(columns.getString("columnName"));

                            //Adding new column to the Hash Map
                            KanbanLauncher.Columns.put(columns.getInt("columnId"), newColumn);

                            //Adding column on the GUI
                            projectColumns.getChildren().add(newColumn);

                            //Iterating Over Tasks to set Tasks
                            VBox columnTasks = (VBox) newColumn.getChildren().get(1);
                            sql = "SELECT * FROM Tasks WHERE  columnId = ?";
                            pstmt = conn.prepareStatement(sql);
                            pstmt.setInt(1, columns.getInt("columnId"));
                            ResultSet tasks = pstmt.executeQuery();
                            //Iterating over Columns
                            while (tasks.next()) {
                                Accordion newTask = FXMLLoader.load(getClass().getResource("task-view.fxml"));
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

                                //Adding new task to the Hash Map
                                KanbanLauncher.Tasks.put(tasks.getInt("taskId"), newTask);

                                //Adding tasks on the GUI
                                columnTasks.getChildren().add(newTask);
                            }
                        }
                    }

                    for (Tab project:
                        KanbanLauncher.Projects.values()) {
                        tabPane.getTabs().add(project);
                    }
                    Scene scene = new Scene(stackPane, 720, 580);
                    // get a handle to the stage
                    Stage stage = (Stage) signUp.getScene().getWindow();
                    // do what you have to do
                    stage.setTitle("Work Space");
                    stage.setScene(scene);
                    stage.show();
                    conn.close();


                }else {
                    statusArea.setText("Invalid Credentials");
                    statusArea.setWrapText(true);
                    statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
                }

            }  catch (Exception e){
                statusArea.setText(e.getMessage());
                statusArea.setWrapText(true);
                statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
            }
        }
    }

    @FXML
    void onSignUp(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(KanbanLauncher.class.getResource("user-registration-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 225, 480);
            // get a handle to the stage
            Stage stage = (Stage) signUp.getScene().getWindow();
            // do what you have to do
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            statusArea.setText(e.getMessage());
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }
    }

    //Following method creates the connection to database
    private Connection getConnection(){
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
