package com.example.a2javaprogramming;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.QuadCurveTo;
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

    //Public Tasks Hash Table and Current Column
    public static Task currentTask = new Task(0, "","", 0, new java.sql.Date(System.currentTimeMillis()),"" );
    public static  HashMap<Integer, Accordion> Tasks = new HashMap<Integer, Accordion>();

    //Projects Java Class and GUI Tab
    public static Project currentProject = new Project(0, "","");
    public static  HashMap<Integer, Tab> Projects = new HashMap<Integer, Tab>();


    //Work Space
    public static BorderPane workspaceBorderPane = null;
    public static StackPane workspaceStackPane = null;

    //Quotes
    public static LinkedList<String> Quotes = new LinkedList<String>();
    @Override
    public void start(Stage stage) throws IOException {
        Quotes.add("“Don't be pushed around by the fears in your mind. Be led by the dreams in your heart.”\n" +
                "― Roy T. Bennett");
        Quotes.add("“There are only two ways to live your life. One is as though nothing is a miracle. The other is as though everything is a miracle.”\n" +
                "― Albert Einstein");
        Quotes.add("“I have not failed. I've just found 10,000 ways that won't work.”\n" +
                "― Thomas A. Edison");
        Quotes.add("“Everything you can imagine is real.”\n" +
                "― Pablo Picasso");
        Quotes.add("“And, when you want something, all the universe conspires in helping you to achieve it.”\n" +
                "― Paulo Coelho, The Alchemist");

        FXMLLoader fxmlLoader = new FXMLLoader(KanbanLauncher.class.getResource("user-login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 225, 480);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}