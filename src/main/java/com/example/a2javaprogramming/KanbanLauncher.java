package com.example.a2javaprogramming;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;

public class KanbanLauncher extends Application {
    public static User loggedUser = new User("","","","");

    //Public Columns Hash Table and Current Column
    public static Column currentColumn = new Column(0, "", 0);
    public static  HashMap<Integer, VBox> Columns = new HashMap<Integer, VBox>();

    //Public Columns Hash Table and Current Column
    public static Task currentTask = new Task(0, "","", 0, new java.sql.Date(System.currentTimeMillis()),"" );
    public static  HashMap<Integer, Accordion> Tasks = new HashMap<Integer, Accordion>();

    //Projects Java Class and GUI Tab
    public static Project currentProject = new Project(0, "","");
    public static  HashMap<Integer, Tab> Projects = new HashMap<Integer, Tab>();


    //Work Space
    public static BorderPane workspaceBorderPane = null;
    public static StackPane workspaceStackPane = null;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KanbanLauncher.class.getResource("user-login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 225, 480);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}