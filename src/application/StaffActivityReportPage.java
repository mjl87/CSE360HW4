package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import javafx.scene.control.Button;


/**
 * <p> StaffActivityReportPage interacts with the {@link DatabaseHelper} class to generates
 *  a UI that allows for staff to see metrics related to user interaction </p>
 *  
 * @author Matthew Lidstone
 * 
 * @version 1.0
 *
 */
public class StaffActivityReportPage {

    private final DatabaseHelper databaseHelper;
    /**
     * Constructs the StaffActivityReportPage UI
     *
     * @param databaseHelper is used to connect the database
     */
    public StaffActivityReportPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    /**
     * Displays the Activity Report Page user interface in the provided <code> Stage </code>
     *
     * @param primaryStage the main application window to display this UI
     * @param user is the current user who has logged in
     */
    public void show(Stage primaryStage, User user) {
        int userCount = databaseHelper.getUserCount();
        int questionCount = databaseHelper.getQuestionCount();
        int answerCount = databaseHelper.getAnswerCount();
        int reviewCount = databaseHelper.getReviewCount();

        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("System Activity Report");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        layout.getChildren().addAll(
            title,
            new Label("Total Users: " + userCount),
            new Label("Total Questions: " + questionCount),
            new Label("Total Answers: " + answerCount),
            new Label("Total Reviews: " + reviewCount)
        );

        //back button
        Button back = new Button("Back to Dashboard");
        back.setOnAction(e -> new StaffDashboardPage(databaseHelper).show(primaryStage, user));
        layout.getChildren().add(back);

        Scene scene = new Scene(layout, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Activity Report");
        primaryStage.show();
    }
}
