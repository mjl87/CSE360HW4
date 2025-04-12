
package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * <p> SuspendUser interacts with the {@link DatabaseHelper} class to generates a UI that allows for staff to suspend or unsuspend users using javafx to interact with the database </p>
 * @author Matthew Lidstone
 * 
 * @version 1.0
 *
 */
public class SuspendUser {

    private final DatabaseHelper databaseHelper;
    /**
     * Constructs the SuspendUser UI
     *
     * @param databaseHelper is used to connect the database
     */
    public SuspendUser(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    /**
     * Displays the Suspend/Unsuspend user interface in the provided <code> Stage </code>
     *
     * @param primaryStage the main application window to display this UI
     * @param user is the current user who has logged in
     */
    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("Suspend or Unsuspend User");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter username");
        
        //suspend button
        Button suspendButton = new Button("Suspend User");
        suspendButton.setOnAction(e -> {
            String username = userNameField.getText().trim();
            try {
                databaseHelper.suspendUser(username, true);
                showAlert(Alert.AlertType.INFORMATION, "User suspended");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Error while suspending user");
                ex.printStackTrace();
            }
        });

        //unsuspend button
        Button unsuspendButton = new Button("Unsuspend User");
        unsuspendButton.setOnAction(e -> {
            String username = userNameField.getText().trim();
            try {
                databaseHelper.suspendUser(username, false);
                showAlert(Alert.AlertType.INFORMATION, "User unsuspended");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Error");
                ex.printStackTrace();
            }
        });

        //back button to dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new StaffDashboardPage(databaseHelper).show(primaryStage, user));

        layout.getChildren().addAll(title, userNameField, suspendButton, unsuspendButton, backButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User Suspension");
        primaryStage.show();
    }
    /**
     * Displays an alert dialog with the specified type and message.
     *
     * @param type of alert
     * @param message the message to display in the alert dialog
     */
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }
}

