package application;

import java.sql.SQLException;
import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

/**
 * The ChatPage class allows for sending messages from one user to another and displaying those messages to the users.
 */
public class ChatPage {
    private final DatabaseHelper databaseHelper;
    /**
     * ChatPage constructor allowing for use of databaseHelper database operations
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
    public ChatPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    /**
     * This is where the users can type in their messages and send them to each other.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param currentUser The currentUser is the object containing the user's username, password, and role
     * @param otherUser The otherUser is the other user in the messaging
     */
    public void show(Stage primaryStage, User currentUser, String otherUser) {
        //Initialize border pane with slight padding
    	BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #f5f5f5;");
        
        // Header
        Label titleLabel = new Label(otherUser);
        titleLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        // Header in top left with other username with padding
        HBox header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 15, 0));
        header.setStyle("-fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");
        
        // Messages area using Vertical Box and slight padding
        VBox messagesContainer = new VBox(10);
        messagesContainer.setPadding(new Insets(10));
        //Make entire screen scrollable and cap area that is currently displayed
        ScrollPane scrollPane = new ScrollPane(messagesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);
        
        // Load messages
        try {
            displayMessages(messagesContainer, currentUser, otherUser);
            scrollPane.setVvalue(1.0); // Auto-scroll to bottom
        } catch (SQLException e) {
            messagesContainer.getChildren().add(new Label("Error loading messages"));
        }
        
        // Message input area with HBox expanding with Message Field and setting width cap.
        TextField messageField = new TextField();
        messageField.setPrefWidth(400);
        messageField.setPromptText("Type your message here...");
        HBox.setHgrow(messageField, javafx.scene.layout.Priority.ALWAYS);
        //Send message with function
        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        
        // Send message function, create runnable to interact with database helper
        Runnable sendMessage = () -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                try {
                    databaseHelper.sendMessage(currentUser.getUserName(), otherUser, message);
                    messageField.clear();
                    show(primaryStage, currentUser, otherUser); // Refresh chat
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
        //Send message, first using the send button, second using enter which triggers onEvent in JavaFX
        sendButton.setOnAction(e -> sendMessage.run());
        messageField.setOnAction(e -> sendMessage.run());
        //HBox for reset inputted area
        HBox inputArea = new HBox(10, messageField, sendButton);
        inputArea.setPadding(new Insets(10, 0, 0, 0));
        
        // Back button using arrow
        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db;");
        backButton.setOnAction(e -> 
            new MessagesListPage(databaseHelper).show(primaryStage, currentUser));
        
        // Set up layouts using VBOX for back button
        layout.setTop(header);
        layout.setCenter(scrollPane);
        layout.setBottom(new VBox(10, inputArea, backButton));
        
        primaryStage.setScene(new Scene(layout, 600, 450));
        primaryStage.setTitle("Chat with " + otherUser);
    }
    //Takes a container and puts the messagesi nto the container
    /**
     * Takes a container and puts the messages into the container
     * @param container The container is the vbox screen the user sees
     * @param currentUser The currentUser is the object containing the user's username, password, and role
     * @param otherUser The otherUser is the other user in the messaging
     * @throws SQLException if there's a database access error or other error then provides error information
     */
    private void displayMessages(VBox container, User currentUser, String otherUser) throws SQLException {
    	//Get messages using current user and other user
        String messagesText = databaseHelper.getMessages(currentUser.getUserName(), otherUser);
        //Set container middle to no messages yet if returned from getMessages
        if (messagesText.equals("No messages yet.")) {
            Label noMessagesLabel = new Label("No messages yet.");
            noMessagesLabel.setTextFill(Color.GRAY);
            HBox centerBox = new HBox(noMessagesLabel);
            centerBox.setAlignment(Pos.CENTER);
            container.getChildren().add(centerBox);
            return;
        }
        //Check for messages and exclude newline
        for (String line : messagesText.split("\n")) {
            if (line.trim().isEmpty()) continue;
            //Database is formatted as [timestamp] sendername: message content
            //Gets timestamp index substrings
            int timestampEnd = line.indexOf("]") + 1;
            int senderEnd = line.indexOf(":", timestampEnd) + 1;
            //Get each value
            String timestamp = line.substring(0, timestampEnd);
            String sender = line.substring(timestampEnd, senderEnd).trim();
            String message = line.substring(senderEnd).trim();
            //Check if sender is current user or other user with simple if else for colors
            boolean isSentByCurrentUser = sender.contains(currentUser.getUserName());
            String bubbleStyle = isSentByCurrentUser ? 
                "-fx-background-color: #3498db; -fx-background-radius: 15px 0px 15px 15px;" : 
                "-fx-background-color: #e9e9eb; -fx-background-radius: 0px 15px 15px 15px;";
            //Add padding with textflow for container with multiple CSS properties
            TextFlow messageFlow = new TextFlow();
            messageFlow.setStyle(bubbleStyle + " -fx-padding: 10px;");
            //Sender + new line + color if isSentByCurrentUser
            Text senderText = new Text(sender + "\n");
            senderText.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
            senderText.setFill(isSentByCurrentUser ? Color.WHITE : Color.GRAY);
            //Message + color if isSentByCurrentUser
            Text messageText = new Text(message);
            messageText.setFill(isSentByCurrentUser ? Color.WHITE : Color.BLACK);
            //Message + color if isSentByCurrentUser
            Text timeText = new Text("\n" + timestamp);
            timeText.setFill(isSentByCurrentUser ? Color.web("#cceeff") : Color.GRAY);
            timeText.setFont(Font.font("Roboto", 10));
            //Add all to MessageFlow
            messageFlow.getChildren().addAll(senderText, messageText, timeText);
            //Place message in HBOX and return HBOX wrapped in the original container
            HBox messageBox = new HBox(messageFlow);
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            
            container.getChildren().add(messageBox);
        }
    }
}