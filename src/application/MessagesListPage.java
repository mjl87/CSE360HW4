package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * This class lists the users that can be messaged and allows the user to pick another user to interact with
 */
public class MessagesListPage {
    private final DatabaseHelper databaseHelper;
    
    /**
     * MessagesListPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed.
     */
    public MessagesListPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    /**
     * This is the main function of listing and showing users to be messaged
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param currentUser The currentUser is the object containing a user's username, password, and role information.
     */
    public void show(Stage primaryStage, User currentUser) {
    	// Create borderpane as root, set margins around layout, and set background color
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5f5f5;");
        
        // Messages Header + Messages padding
        Label titleLabel = new Label("My Messages");
        titleLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        titleLabel.setPadding(new Insets(0, 0, 20, 0));
        
        // Users list, create container with slight top padding as they are loaded in
        VBox usersContainer = new VBox(10);
        usersContainer.setPadding(new Insets(10, 0, 0, 0));
        
        try {
        	//Load in all users as strings that will be placed in users list
            List<String> users = databaseHelper.getAllUsers();
            //If only the current user exists ignore
            if (users.isEmpty() || (users.size() == 1 && users.get(0).equals(currentUser.getUserName()))) {
                Label noUsersLabel = new Label("No other users available to message.");
                noUsersLabel.setFont(Font.font("Roboto", 14));
                usersContainer.getChildren().add(noUsersLabel);
            } else {
            	//For each user in users, if not current user, create new button with padding that is the user
                for (String username : users) {
                    if (!username.equals(currentUser.getUserName())) {
                        HBox userRow = new HBox();
                        userRow.setAlignment(Pos.CENTER_LEFT);
                        userRow.setPadding(new Insets(15));
                        userRow.setStyle("-fx-background-color: #d4e9f7; -fx-background-radius: 5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);");

              
                        // Only show username
                        Label nameLabel = new Label(username);
                        nameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
                        nameLabel.setTextFill(Color.web("#2c3e50"));
                        // Make the entire row clickable
                        userRow.setOnMouseClicked(e -> {
                            new ChatPage(databaseHelper).show(primaryStage, currentUser, username);
                        });
                        
                       // Add all buttons to userRow, and add all userRows to userContainer
                        userRow.getChildren().addAll(nameLabel);
                        
                        usersContainer.getChildren().add(userRow);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error loading users");
            errorLabel.setTextFill(Color.RED);
            usersContainer.getChildren().add(errorLabel);
        }
        // Allow scroll and fit the scroll to container
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(usersContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        // Footer with back button
        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db;");
        
        backButton.setOnAction(e -> {
            new QuestionsPage(databaseHelper).show(primaryStage, currentUser);
        });
        
        // Set up the layout
        layout.setTop(titleLabel);
        layout.setCenter(scrollPane);
        layout.setBottom(backButton);
        
        Scene scene = new Scene(layout, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Messages");
    }
}