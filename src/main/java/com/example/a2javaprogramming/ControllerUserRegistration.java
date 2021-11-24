package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.sql.*;
import javafx.stage.Stage;

public class ControllerUserRegistration {

    @FXML
    private VBox userRegistration;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private TextArea statusArea;

    @FXML
    private Button signUpButton;

    @FXML
    private Button closeButton;

    @FXML
    private Hyperlink login;



    @FXML
    void onSignUp(ActionEvent event) {
        String fName = firstName.getText();
        String lName = lastName.getText();
        String uName = username.getText();
        String pass = password.getText();

        Connection conn = getConnection();
        //Following code creates a new user. If the user already exists, it shows an error
        try{
            //creates account only if username is unique.
            String sql = "INSERT INTO Users (firstName, lastName, username, password) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fName);
            pstmt.setString(2, lName);
            pstmt.setString(3, uName);
            pstmt.setString(4, pass);
            pstmt.executeUpdate();

            statusArea.setText("Sign Up Successful. Please Login");
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
            conn.close();

        }  catch (Exception e){
            statusArea.setText(e.getMessage());
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }
    }

    @FXML
    void onLogin(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(KanbanLauncher.class.getResource("user-login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 225, 480);
            // get a handle to the stage
            Stage stage = (Stage) login.getScene().getWindow();
            // do what you have to do
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            statusArea.setText(e.getMessage());
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }
    }

    @FXML
    void onClose(ActionEvent event){
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
