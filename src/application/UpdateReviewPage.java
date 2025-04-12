package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The UpdateReviewPage class allows the author of a review to update their review.
 */
public class UpdateReviewPage {
    private final DatabaseHelper databaseHelper;

    /**
     * UpdateReviewPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
    public UpdateReviewPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
     * This allows the reviewer to update their review
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user object that has a user's username, password, and role.
     * @param reviewId The review id
     * @param currentReview The current review
     * @param questionId The question id
     */
    public void show(Stage primaryStage, User user, int reviewId, String currentReview, int questionId) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label instruction = new Label("Update your review:");
        Label errorLabel = new Label();

        TextField updateText = new TextField();
        updateText.setText(currentReview);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            String newReview = updateText.getText().trim();
            if (!newReview.isEmpty()) {
                try {
                    databaseHelper.updateReview(reviewId, newReview);
                    new QuestionPage(databaseHelper).show(primaryStage, user, questionId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    errorLabel.setText("Failed to update review.");
                }
            } else {
                errorLabel.setText("Review cannot be empty.");
            }
        });

        Button backButton = new Button("<- Cancel");
        backButton.setOnAction(e -> {
            try {
                new QuestionPage(databaseHelper).show(primaryStage, user, questionId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        layout.getChildren().addAll(instruction, updateText, updateButton, backButton, errorLabel);
        primaryStage.setScene(new Scene(layout, 600, 300));
        primaryStage.setTitle("Update Review");
    }
}