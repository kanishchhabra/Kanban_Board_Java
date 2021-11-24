package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class ControllerTask {

    //Controls from the Tasks View
    @FXML
    private Accordion newTask;

    @FXML
    private TitledPane taskPane;

    @FXML
    private TextArea taskDescription;

    @FXML
    private DatePicker dueDate;

    @FXML
    private ChoiceBox<?> taskStatus;

    @FXML
    private Hyperlink columnDelete;

    @FXML
    private Hyperlink saveTask;

    @FXML
    private TextField taskNName;

    @FXML
    private ChoiceBox<?> taskColumn;

    //-----------------------------------------------------------------------
    //Controls from Add Task View
    @FXML
    private DatePicker dueDateForm;

    @FXML
    private ScrollPane createTaskPane;

    @FXML
    private VBox createTaskWindow;

    @FXML
    private TextField taskNameForm;

    @FXML
    private TextArea taskDescriptionForm;

    @FXML
    private ChoiceBox<?> taskStatusForm;

    @FXML
    private Button createTask;

    @FXML
    private Button closeAddTask;

    @FXML
    private TextArea statusArea;

    //----------------------------------------------
    //Functions from Task View

    @FXML
    void onColumnDelete(ActionEvent event) {


    }

    @FXML
    void onSaveTask(ActionEvent event) {
        Connection conn = getConnection();

        try{
            //Fetching date
            String year = String.valueOf(dueDate.getValue().getYear());
            String month = String.valueOf(dueDate.getValue().getMonthValue());
            String day = String.valueOf(dueDate.getValue().getDayOfMonth());
            String date = year + month + day;

            java.util.Date dueDate = new SimpleDateFormat("yyyyMMdd").parse(date);
            java.sql.Date sqlDueDate = new java.sql.Date(dueDate.getTime());

            //Fetching Targeted Column
            String taskColumnName = taskColumn.getSelectionModel().getSelectedItem().toString();
            String columnId = taskColumnName.substring(0, (taskColumnName.indexOf(":") - 2) );

            String sql = "UPDATE Tasks SET taskName = ?, taskStatus = ?, columnId = ?, dueDate = ?, taskDescription = ? WHERE taskId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taskNName.getText());
            pstmt.setString(2, taskStatus.getSelectionModel().getSelectedItem().toString());
            pstmt.setInt(3, Integer.valueOf(columnId));
            pstmt.setDate(4, sqlDueDate);
            pstmt.setString(5, taskDescription.getText());
            pstmt.setInt(6, KanbanLauncher.currentTask.getTaskId());
            pstmt.executeUpdate();

            taskPane.setText(taskNName.getText());

            //Adding Task to the targeted column
            VBox targetColumn = KanbanLauncher.Columns.get(Integer.valueOf(columnId));
            VBox targetedColumnContainer = (VBox)targetColumn.getChildren().get(1);
            Accordion currentTask = KanbanLauncher.Tasks.get(Integer.valueOf(newTask.getId()));

            if (!targetedColumnContainer.getChildren().contains(currentTask)){
                targetedColumnContainer.getChildren().add(currentTask);
            }


            //Updating task on Hash Map
            KanbanLauncher.Tasks.replace(KanbanLauncher.currentTask.getTaskId(), newTask);

            //Closing connection
            conn.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    @FXML
    void onViewTaskList(ActionEvent event) {

    }

    //Functions from Add Task View
    @FXML
    void onCloseAddTask(ActionEvent event) {
        //Closing the column window
        createTaskPane.setVisible(false);
        KanbanLauncher.workspaceBorderPane.setDisable(false);
        KanbanLauncher.workspaceStackPane.getChildren().remove(createTaskPane);
    }

    @FXML
    void onCreateTask(ActionEvent event) {
        Connection conn = getConnection();
        //Following code creates adds a new column to DB. Displays the errors in the status area
        try{
            //Inserting the new task into database
            String year = String.valueOf(dueDateForm.getValue().getYear());
            String month = String.valueOf(dueDateForm.getValue().getMonthValue());
            String day = String.valueOf(dueDateForm.getValue().getDayOfMonth());
            String date = year + month + day;
            java.util.Date dueDate = new SimpleDateFormat("yyyyMMdd").parse(date);
            java.sql.Date sqlDueDate = new java.sql.Date(dueDate.getTime());
            System.out.println(sqlDueDate);

            String sql = "INSERT INTO Tasks (taskName, taskStatus, columnId, dueDate, taskDescription) VALUES (?,?, (SELECT columnId FROM Columns WHERE  columnId = ? ), ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taskNameForm.getText());
            pstmt.setString(2, taskStatusForm.getSelectionModel().getSelectedItem().toString());
            pstmt.setInt(3, Integer.valueOf(KanbanLauncher.currentColumn.getColumnId()));
            pstmt.setDate(4, sqlDueDate);
            pstmt.setString(5, taskDescriptionForm.getText());
            pstmt.executeUpdate();

            //Getting the new task created
            sql = "SELECT * FROM Tasks WHERE columnId = (SELECT columnId FROM Columns WHERE  columnId = ? ) ORDER BY taskId DESC LIMIT 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.valueOf(KanbanLauncher.currentColumn.getColumnId()));
            ResultSet task = pstmt.executeQuery();


            statusArea.setText("Task Created");
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));


            //Creates new column window on the GUI
            Accordion newTask = FXMLLoader.load(getClass().getResource("task-view.fxml"));
            VBox tasksContainer = (VBox) KanbanLauncher.Columns.get(KanbanLauncher.currentColumn.getColumnId()).getChildren().get(1);

            VBox addedTask = (VBox) ((TitledPane) newTask.getPanes().get(0)).getContent();
            ChoiceBox status = (ChoiceBox) addedTask.getChildren().get(5);
            status.getItems().add("Yet to Start");
            status.getItems().add("Ongoing");
            status.getItems().add("Completed");

            tasksContainer.getChildren().add(newTask);

            //Setting ID to th new column and setting current column:
            while (task.next()){
                //Setting current task
                KanbanLauncher.currentTask.setTaskId(task.getInt(1));
                KanbanLauncher.currentTask.setTaskName(task.getString(2));
                KanbanLauncher.currentTask.setTaskStatus(task.getString(3));
                KanbanLauncher.currentTask.setColumnId(task.getInt(4));
                KanbanLauncher.currentTask.setDueDate(task.getDate(5));
                KanbanLauncher.currentTask.setTaskDescription(task.getString(6));

                //Setting task on GUI
                ((TextArea) addedTask.getChildren().get(1)).setText(task.getString(6));
                ((ChoiceBox) addedTask.getChildren().get(5)).setValue(task.getString(3));
                ((DatePicker) addedTask.getChildren().get(3)).setValue(LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day)));

                newTask.setId(String.valueOf(task.getInt("taskId")));
                ((TitledPane) newTask.getPanes().get(0)).setText(task.getString(2));
            }

            //Showing new column on  Hash Map
            KanbanLauncher.Tasks.put(Integer.valueOf(newTask.getId()), newTask);

            //Closing the column window
            createTaskPane.setVisible(false);
            KanbanLauncher.workspaceBorderPane.setDisable(false);
            KanbanLauncher.workspaceStackPane.getChildren().remove(createTaskPane);

            //Closing connection
            conn.close();

        }  catch (Exception e){
            statusArea.setVisible(true);
            statusArea.setText(e.getClass().getName() + ": " + e.getMessage() + "----" + e.toString());
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }
    }

    //-------------------------------------------
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

    //Gets current task
    public void setCurrentTask()  {

        try{
            Connection conn = getConnection();
            //Getting the new task created
            String sql = "SELECT * FROM Tasks WHERE taskId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.valueOf(newTask.getId()));
            ResultSet task = pstmt.executeQuery();
            while (task.next()) {
                KanbanLauncher.currentTask.setTaskId(task.getInt("taskId"));
                KanbanLauncher.currentTask.setTaskName(task.getString("taskName"));
                KanbanLauncher.currentTask.setColumnId(task.getInt("columnId"));
                KanbanLauncher.currentTask.setTaskStatus(task.getString("taskStatus"));
                KanbanLauncher.currentTask.setTaskDescription(task.getString("taskDescription"));
                KanbanLauncher.currentTask.setDueDate(task.getDate("dueDate"));
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
