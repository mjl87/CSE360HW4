
package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;


/**
 * AnswerAQuestionPage class handles the user answering a question and adds it to the list of answers.
 */
public class AnswerAQuestionPage {
	
    private final DatabaseHelper databaseHelper;

    /**
     * AnswerAQuestionPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed.
     */
    public AnswerAQuestionPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	/**
     * Displays the answer question text field in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param id The User's id for the answer to keep track of author of answer.
     */
    public void show(Stage primaryStage, User user, int id) {
    	// Input field for the user's Answer
        TextField AnswerField = new TextField();
        AnswerField.setPromptText("Answer A Question");
        AnswerField.setMaxWidth(250);
        //Create an error label to let the user know if they are breaking the input validation
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button AnswerButton = new Button("Answer");
        
        AnswerButton.setOnAction(a -> {
        	// Retrieve user inputs
            String Answer = AnswerField.getText();
            //Checks if the answer that the user has inputed is too long
            if(Answer.length() > 500) {
    			errorLabel.setText("Your Answer is too long!!!");
        	}
            //Check if the answer field is empty
        	else if(Answer.length() == 0) {
        		errorLabel.setText("You need to create an answer!!!");
        	}
            //If there is a valid answer, it will create a new answer and add it to the database
        	else {
        		String Author = user.getUserName();
        		Answer answer = new Answer(id, Answer, Author);
        		try {
        			databaseHelper.createAnswer(answer);
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		//This sends the user back to the list of questions
        		new QuestionsPage(databaseHelper).show(primaryStage, user);
        	}
        });
        
        

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(AnswerField,AnswerButton,errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Ask A Question Page");
        primaryStage.show();
    }
}