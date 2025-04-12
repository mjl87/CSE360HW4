package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * In the DeleteReviewPage, the author of a review can delete their review
 */
public class DeleteReviewPage {
    private final DatabaseHelper databaseHelper;

    /**
     * DeleteReviewPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
    public DeleteReviewPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    /**
     * Allows the reviewer to delete their own review.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The object containing a user's username, password, and role information.
     * @param reviewId The id number of the review
     * @param reviewText The review
     * @param questionId The id number of the question
     */
    public void show(Stage primaryStage, User user, int reviewId, String reviewText, int questionId) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label confirmLabel = new Label("Are you sure you want to delete this review?");
        Label reviewLabel = new Label("\"" + reviewText + "\"");
        reviewLabel.setWrapText(true);

        Button deleteButton = new Button("Delete Review");
        deleteButton.setOnAction(e -> {
            try {
                databaseHelper.deleteReview(reviewId);
                new QuestionPage(databaseHelper).show(primaryStage, user, questionId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            try {
                new QuestionPage(databaseHelper).show(primaryStage, user, questionId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        layout.getChildren().addAll(confirmLabel, reviewLabel, deleteButton, cancelButton);

        primaryStage.setScene(new Scene(layout, 600, 250));
        primaryStage.setTitle("Delete Review");
    }
}