package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * AddReviewPage class represents having a reviewer make a review for an Answer
 */
public class AddReviewPage {

    private final DatabaseHelper databaseHelper;
    
    /**
     * AddReviewPage constructor allowing for use of databaseHelper database operations
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
    public AddReviewPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
	/**
     * Displays the Review page in the provided primary stage, allowing for reviewer to enter in a review.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user object that has a user's username, password, and role.
     * @param answerID The variable that holds the answer's id
     */
    public void show(Stage primaryStage, User user, int answerID) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titleLabel = new Label("Write a review comment for Answer ID: " + answerID);
        TextArea reviewField = new TextArea();
        reviewField.setWrapText(true);
        reviewField.setPromptText("Enter your review here...");
        reviewField.setMaxWidth(400);
        reviewField.setMaxHeight(150);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button submitButton = new Button("Submit Review");
        submitButton.setOnAction(e -> {
            String review = reviewField.getText().trim();
            if (review.isEmpty()) {
                errorLabel.setText("Review cannot be empty.");
            } else if (review.length() > 500) {
                errorLabel.setText("Review is too long (max 500 characters).");
            } else {
                try {
                    databaseHelper.addReview(answerID, review, user.getUserName());
                    new QuestionsPage(databaseHelper).show(primaryStage, user); // Redirect back to questions
                } catch (Exception ex) {
                    ex.printStackTrace();
                    errorLabel.setText("Failed to submit review.");
                }
            }
        });

        Button backButton = new Button("<- Cancel");
        backButton.setOnAction(e -> new QuestionsPage(databaseHelper).show(primaryStage, user));

        layout.getChildren().addAll(titleLabel, reviewField, submitButton, backButton, errorLabel);

        Scene scene = new Scene(layout, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add Review Comment");
    }
}