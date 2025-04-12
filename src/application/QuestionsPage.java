package application;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays questions from the database, or a "No Questions Yet" message if none exist.
 */


public class QuestionsPage {
	
	public boolean contains(String toSearch, String toSearchFor) {
		Pattern pattern = Pattern.compile(toSearchFor, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(toSearch);
	    return matcher.find();
	}

    private final DatabaseHelper databaseHelper;
    /**
     * QuestionsPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
    public QuestionsPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    /**
     * lists out all the questions allowing users to view these questions to look for answers
	 * @param primaryStage The primary stage where the scene will be displayed.
	 * @param user The user where user name, password, and role is stored for user.
     */
    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-spacing: 10;");
        
        //allows scrolling functionality
    ScrollPane scrolly = new ScrollPane();
    scrolly.setPrefSize(69, 69);
    scrolly.setContent(layout);
    
        // "Ask a Question" button (only for students)
        if (user.getRole().contains("Student")) {
            Button askButton = new Button("Ask a question");
            askButton.setOnAction(a -> new AskAQuestionPage(databaseHelper).show(primaryStage, user));
            layout.getChildren().add(askButton);
        }
        //Creates a list of the questions
        List<String> questions;
        //Grabs all the questions from the database
        try {
            questions = databaseHelper.getQuestions();
        } catch (SQLException e) {
            e.printStackTrace();
            questions = List.of("Error retrieving questions.");
        }

        // Show "No questions yet" if empty
        if (questions.isEmpty() || (questions.size() == 1 && questions.get(0).equals("No questions yet"))) {
            layout.getChildren().add(new Label("No questions yet."));
        } else { //Else it will parse through the list of questions and grab its id and create a view button for it so a user can read the answers
            for (String question : questions) {
                String[] parts = question.split("\\.", 2);
                int id = Integer.parseInt(parts[0].trim());
                Label questionLabel = new Label(question);
                Button viewButton = new Button("View");
                
                viewButton.setOnAction(e -> {
try {
new QuestionPage(databaseHelper).show(primaryStage, user, id);
} catch (SQLException e1) {
// TODO Auto-generated catch block
e1.printStackTrace();
}
});
                layout.getChildren().addAll(questionLabel, viewButton);
            }
        }
        
        // User can go back to their home page
        Button BackButton = new Button("<- Back");
    BackButton.setOnAction(a -> {
    new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
    });
    layout.getChildren().add(BackButton);
    
        Scene userScene = new Scene(scrolly, 800, 400);
        primaryStage.setScene(userScene);
        primaryStage.setTitle("Questions Page");
    }}