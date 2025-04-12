package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

/**
 * <p> StaffDashboardPage interacts with the {@link DatabaseHelper} class to generates
 *  a UI that allows for staff to have their own easy to use UI </p>
 *  
 * @author Matthew Lidstone
 * 
 * @version 1.0
 *
 */
public class StaffDashboardPage {

    private final DatabaseHelper databaseHelper;

    public StaffDashboardPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Shows the Staff Dashboard page.
     * @param primaryStage the main application window to display this UI
     * @param user is the current user who has logged in
     */
    public void show(Stage primaryStage, User user) {
        Label headerLabel = new Label("Staff Dashboard");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        //generate activity button
        Button viewReportsButton = new Button("Generate Activity Reports");
        viewReportsButton.setOnAction(e -> {
            new StaffActivityReportPage(databaseHelper).show(primaryStage, user);
        });
        
        //suspend user button
        Button suspendUserButton = new Button("Suspend /Unsuspend User");
        suspendUserButton.setOnAction(e -> {
            new SuspendUser(databaseHelper).show(primaryStage, user);
        });
        
        //broadcast message button
        Button broadcastButton = new Button("Send Message");
        broadcastButton.setOnAction(e -> {
            new BroadcastMessagePage(databaseHelper).show(primaryStage, user);
        });
        
        //back button to homepage
        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> {
            new UserHomePage(databaseHelper).show(primaryStage, user);
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        layout.getChildren().addAll(headerLabel, broadcastButton, suspendUserButton, viewReportsButton, backButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Dashboard");
        primaryStage.show();
    }
}
