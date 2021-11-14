package com.example.a2javaprogramming;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

public class KanbanLauncher extends Application {
    public static User loggedUser = new User("","","","");

    //Projects Java Class and GUI Tab
    public static LinkedList<Project> projects = new LinkedList<Project>();
    public static LinkedList<Tab> projectTabs = new LinkedList<Tab>();

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