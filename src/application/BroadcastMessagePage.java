
package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * <p> BroadcastMessagePage interacts with the {@link DatabaseHelper} class to generates
 *  a message that will be displayed to {@link UserHomePage} if the logged in user is an instructor or reviewer </p>
 *  
 * @author Matthew Lidstone
 * 
 * @version 1.0
 *
 */
public class BroadcastMessagePage {

    private final DatabaseHelper databaseHelper;
    /**
     * Constructs a new <code> BroadcastMessagePage </code> with the given database helper
     *
     * @param databaseHelper is used to connect the database
     */
    public BroadcastMessagePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    /**
     * Shows the broadcast message page.
     * @param primaryStage the main application window to display this UI
     * @param user is the current user who has logged in
     */
    public void show(Stage primaryStage, User staffUser) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label title = new Label("Broadcast a Message to Instructors and Reviewers");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Type your broadcast message here:");
        messageArea.setPrefRowCount(5);
        messageArea.setWrapText(true);
        messageArea.setMaxWidth(500);
        
        //send message button
        Button sendButton = new Button("Send Broadcast");
        sendButton.setOnAction(e -> {
            String message = messageArea.getText().trim();
            if (message.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Message cannot be empty").showAndWait();
                return;
            }

            try {
                databaseHelper.createBroadcastMessage(message, staffUser.getUserName());
                new Alert(Alert.AlertType.INFORMATION, "Broadcast sent to Instructors and Reviewers").showAndWait();
                messageArea.clear();
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Failed to send broadcast").showAndWait();
                ex.printStackTrace();
            }
        });
        //back button to staff dashboard
        Button backButton = new Button("Back to Staff Dashboard");
        backButton.setOnAction(e -> new StaffDashboardPage(databaseHelper).show(primaryStage, staffUser));

        layout.getChildren().addAll(title, messageArea, sendButton, backButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Broadcast");
        primaryStage.show();
    }
}
