
package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * AskAQuestionPage class is where a user can ask a question and add their question to the list of questions
 */
public class AskAQuestionPage {
	
    private final DatabaseHelper databaseHelper;

    /**
     * AskAQuestionPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
    public AskAQuestionPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	/**
     * Displays a prompt asking a user to ask a question in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage, User user) {
    	// Input field for the user's userName, password
        TextField QuestionField = new TextField();
        QuestionField.setPromptText("Ask A Question");
        QuestionField.setMaxWidth(250);
        //Create an error label to let the user know if they are breaking the input validation
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button AskButton = new Button("Ask");
        AskButton.setOnAction(a -> {
            String Question = QuestionField.getText();
            	//Checks if the question that the user has inputed is too long
            	if(Question.length() > 500) {
        			errorLabel.setText("Your question is too long!!!");
            	}
            	//Check if the question field is empty
            	else if(Question.length() == 0) {
            		errorLabel.setText("You need to ask a question!!!");
            	}
            	//If there is a valid question, it will create a new question and add it to the database
            	else {
            		String Author = user.getUserName();
            		Question question = new Question(Question, Author);
            		try {
            			databaseHelper.createQuestion(question);
            		} catch (SQLException e) {
            			e.printStackTrace();
            		}
            		//This sends the user back to the list of questions
            		new QuestionsPage(databaseHelper).show(primaryStage, user);
            	}
        });
        


        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(QuestionField,AskButton,errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Ask A Question Page");
        primaryStage.show();
    }
}
