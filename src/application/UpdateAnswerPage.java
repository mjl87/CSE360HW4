
package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class allows the author of an answer to a question to update their answer if they feel like they want to edit it.
 * This class also checks for a valid answer and will give the user an error message if answer is invalid.
 */
public class UpdateAnswerPage {
	private Label answerLabel;
	private Button AskButton;
	private Button deleteButton;
	private final DatabaseHelper databaseHelper;

    /**
     * UpdateAnswerPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
	public UpdateAnswerPage(DatabaseHelper databaseHelper2) {
		// TODO Auto-generated constructor stub
		this.databaseHelper = databaseHelper2;
	}

	//UI for update answer
	
	/**
     * Displays the update answer text field in the provided primary stage, which allows for updating answer.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user where the name, password, and role is stored.
     * @param grabid Gets the id of the answer's author.
     * @param oneAnswer The current Answer that's about to be updated.
     * @param oneQuestion The current Question that has the answer that's about to be updated.
     */
	public void show(Stage primaryStage, User user,int grabid, String oneAnswer, String oneQuestion) throws SQLException {
		VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    //Creates a back button so the user can navigate back to the list of questions
	    Button BackButton = new Button("<- Back");
	    BackButton.setOnAction(a -> {
	    	new QuestionsPage(databaseHelper).show(primaryStage, user);
	    });
	    Label questionstuff = new Label(oneQuestion);
	    Label errorlabel = new Label();
	    
	    TextField updatetext = new TextField();
    	updatetext.setText(oneAnswer);
    	
    	Button confirmUpdate = new Button("Update");
    	
    	confirmUpdate.setOnAction(ee -> {
    		
    		String update = updatetext.getText();
    		if(update != "") {
    		databaseHelper.updateAnswer(update, grabid);
    		new QuestionsPage(databaseHelper).show(primaryStage, user);
    		}
    		else {
    			errorlabel.setText("Error must have valid answer");
    		}
    	});
    	
    	layout.getChildren().addAll(questionstuff, updatetext, confirmUpdate, errorlabel);
    	
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Update Page");
	    
	}
}
	
	
	