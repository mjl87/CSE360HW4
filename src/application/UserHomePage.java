package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.control.TextArea;

/**
* This page displays a simple welcome message for the user as well as displaying their user information to them.
*/
public class UserHomePage {

    private final DatabaseHelper databaseHelper;
    private boolean requestSent = false;

    /**
    * UserHomePage constructor allowing for use of databaseHelper database operations.
    * @param databaseHelper The databaseHelper where interaction with database is allowed
    */
    public UserHomePage(DatabaseHelper databaseHelper2) {
        this.databaseHelper = databaseHelper2;
    }
    /**
     * This uses regular expressions to find matching strings
     * @param toSearch The to Search is the string its searching for
     * @param toSearchFor The toSearchFor is the String the search is looking at
     * @return returns true if found
     */
    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Cache role once to avoid repeating DB calls
        String role = databaseHelper.getUserRole(user.getUserName());

        // Labels
        Label userLabel = new Label("Hello, " + user.getUserName() + "!");
        Label infoLabel = new Label("Info: \nUser Name: " + user.getUserName() + "\nPassword: " + user.getPassword());
        Label rolesLabel = new Label("Roles: " + role);

        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        layout.getChildren().addAll(userLabel, infoLabel, rolesLabel);

        // If user is a Student (but not already a Reviewer), allow role request
        if (role != null && role.contains("Student") && !role.contains("Reviewer")) {
            Button requestReviewerRole = new Button("Become Reviewer?");
            requestReviewerRole.setOnAction(a -> {
                try {
                    databaseHelper.rolePermission(user, "Reviewer");  // fixed typo
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Label request = new Label("Request Sent!");
                layout.getChildren().add(request);
            });
            layout.getChildren().add(requestReviewerRole);

            Button seeTrustedReviewers = new Button("See Trusted Reviewers");
            seeTrustedReviewers.setOnAction(a -> {
                new TrustedReviewerPage().show(databaseHelper, primaryStage, user);
            });
            layout.getChildren().add(seeTrustedReviewers);
        }
        
        //if user is an instructor or reviewer, display staff broadcast message
        if (role.contains("Instructor") || role.contains("Reviewer")) {
            List<String> broadcasts = databaseHelper.getAllBroadcastMessages();
            if (!broadcasts.isEmpty()) {
                Label header = new Label("📣 Broadcast Messages:");
                layout.getChildren().add(header);
                for (String msg : broadcasts) {
                    TextArea area = new TextArea(msg);
                    area.setWrapText(true);
                    area.setEditable(false);
                    layout.getChildren().add(area);
                }
            }
        }

        // If user is an Instructor, allow access to student review
        if (role != null && role.contains("Instructor")) {
            Button studentActivity = new Button("Review Students");
            studentActivity.setOnAction(a -> {
                try {
                    new StudentActivityPage(databaseHelper).show(databaseHelper, primaryStage, user);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            layout.getChildren().add(studentActivity);
        }
        
        //if user is a staff member, create "staff dashbpard" button
        if (role != null && role.contains("Staff")) {
            Button staffDashboard = new Button("Staff Dashboard");
            staffDashboard.setOnAction(e -> {
                new StaffDashboardPage(databaseHelper).show(primaryStage, user);
            });
            layout.getChildren().add(staffDashboard);
        }


        // Back button
        Button backButton = new Button("<- Back");
        backButton.setOnAction(a -> {
            new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
        });
        layout.getChildren().add(backButton);


        // Set scene
        Scene userScene = new Scene(layout, 800, 400);
        primaryStage.setScene(userScene);
        primaryStage.setTitle("User Page");
    }
}
