package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;

public class LoginController {

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
            statusArea.setText(e.getClass().getName() + ": " + e.getMessage());
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
        }else{
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
                }else {
                    statusArea.setText("Invalid Credentials");
                    statusArea.setWrapText(true);
                    statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
                }

            }  catch (Exception e){
                statusArea.setText(e.getClass().getName() + ": " + e.getMessage());
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
            statusArea.setText(e.getClass().getName() + ": " + e.getMessage());
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
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return conn;
    }
}