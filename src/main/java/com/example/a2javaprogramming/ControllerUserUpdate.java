package com.example.a2javaprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ControllerUserUpdate {
    @FXML
    private ScrollPane userDetailsContainer;

    @FXML
    private VBox userUpdate;

    @FXML
    private ImageView profilePicture;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private Hyperlink fileSelector;

    @FXML
    private TextField fileURL;

    @FXML
    private TextArea statusArea;

    @FXML
    private Button updateButton;

    @FXML
    private Button closeButton;

    @FXML
    void onClose(ActionEvent event) {
        try{
            KanbanLauncher.workspaceStackPane.getChildren().remove(userDetailsContainer);
            KanbanLauncher.workspaceBorderPane.setDisable(false);

            //Refreshing Project
            KanbanLauncher.workspaceBorderPane.getCenter().setDisable(true);
            ProjectRefresh refresh = new ProjectRefresh();
            refresh.completeRefresh(KanbanLauncher.loggedUser.getUsername(), KanbanLauncher.loggedUser.getPassword());
            KanbanLauncher.workspaceBorderPane.getCenter().setDisable(false);}
        catch (Exception e){
            statusArea.setText(e.getMessage());
            statusArea.setWrapText(true);
            statusArea.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }
    }

    @FXML
    void onFileSelector(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPEG Files", "*.jpg")
                    ,new FileChooser.ExtensionFilter("PNG Files", "*.png")
            );
            Stage stage = (Stage) userDetailsContainer.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            //Setting File URL
            fileURL.setText(selectedFile.getPath());

            //Showing Image
            FileInputStream imageInput = new FileInputStream(fileURL.getText());
            Image image = new Image(imageInput);
            profilePicture.setImage(image);

        }catch (Exception e){
            statusArea.setText(e.getMessage());
        }
    }

    @FXML
    void onUpdate(ActionEvent event) {
        Connection conn = getConnection();
        //Following code updates the user.
        try{
            //Form Validation
            if (!(firstName.getText().isEmpty() && lastName.getText().isEmpty())) {
                //Updating the DB.
                String sql = "UPDATE Users SET firstName = ?, lastName = ?, profilePicture = ? WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, firstName.getText());
                pstmt.setString(2, lastName.getText());
                pstmt.setString(3, fileURL.getText());
                pstmt.setString(4, KanbanLauncher.loggedUser.getUsername());
                pstmt.executeUpdate();

                //Updating the logged user
                KanbanLauncher.loggedUser.setProfilePicture(fileURL.getText());
                KanbanLauncher.loggedUser.setFirstName(firstName.getText());
                KanbanLauncher.loggedUser.setLastName(lastName.getText());

                statusArea.setText("Account Updated");
                statusArea.setWrapText(true);
                statusArea.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
                conn.close();

                KanbanLauncher.workspaceStackPane.getChildren().remove(userDetailsContainer);
                KanbanLauncher.workspaceBorderPane.setDisable(false);

                //Refreshing Project
                KanbanLauncher.workspaceBorderPane.getCenter().setDisable(true);
                ProjectRefresh refresh = new ProjectRefresh();
                refresh.completeRefresh(KanbanLauncher.loggedUser.getUsername(), KanbanLauncher.loggedUser.getPassword());
                KanbanLauncher.workspaceBorderPane.getCenter().setDisable(false);
            }
        }  catch (Exception e){
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
