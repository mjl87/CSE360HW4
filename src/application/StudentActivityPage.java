package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * This class displays the student users and allows the instructor to click on a button to view them
 */
public class StudentActivityPage {
    public StudentActivityPage(DatabaseHelper databaseHelper2) {
    }
    

    /**
     * This method provides the instructor to view the students and click them to see their activity
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     * @param primaryStage   The primary stage where the scene will be displayed.
     * @param user The user where the name, password, and role is stored.
     * @throws SQLException if there's a database access error or other error then provides error information
     */

    public void show(DatabaseHelper databaseHelper, Stage primaryStage, User user) throws SQLException {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Allows scrolling functionality
        ScrollPane scrolly = new ScrollPane();
        scrolly.setPrefSize(69, 69);
        scrolly.setContent(layout);

        List<String> users = databaseHelper.getAllUsers();

        // Show "No questions yet" if empty
        for (String user1 : users) {
            // Removed incorrect line: String[] parts = users.split("\\.", 2);
        	if(databaseHelper.getUserRole(user1).contains("Student")) {
            Label userLabel = new Label(user1);
            String userName = user1;
            Button viewButton = new Button("View");

            viewButton.setOnAction(e -> {
                try {
					new ActivityPage(databaseHelper).show(primaryStage, user, userName);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            });

            layout.getChildren().addAll(userLabel, viewButton);
        }
        }

        // Creates a back button so the user can navigate back to the list of questions
        Button BackButton = new Button("<- Back");
        BackButton.setOnAction(a -> {
            new UserHomePage(databaseHelper).show(primaryStage, user);
        });

        layout.getChildren().add(BackButton); // Removed trailing comma

        Scene userScene = new Scene(scrolly, 800, 400);

        // Set the scene to primary stage
        primaryStage.setScene(userScene);
        primaryStage.setTitle("Student Activity Page");
    }

}

