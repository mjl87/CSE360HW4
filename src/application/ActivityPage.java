package application;

import java.sql.SQLException;
import application.Review;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import application.Review;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * This class displays a user's activity for questions asked and answers asked
 */
public class ActivityPage {
	private DatabaseHelper databaseHelper = new DatabaseHelper();
    /**
     * ActivityPage constructor allowing for use of databaseHelper database operations
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */

	public ActivityPage(DatabaseHelper databaseHelper2) {
		// TODO Auto-generated constructor stub
		this.databaseHelper = databaseHelper2;
	}


	/**
	 * This lists the activity of a user, meaning the questions and answers of a specific user
	 * @param primaryStage The primary stage where the scene will be displayed.
	 * @param user The user object that has a user's username, password, and role.
	 * @param userName The userName holds the user the instructor wants to view
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */

	public void show(Stage primaryStage, User user, String userName) throws SQLException {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    //allows scrolling functionality
    	ScrollPane scrolly = new ScrollPane();
	    scrolly.setPrefSize(69, 69);
	    scrolly.setContent(layout);
	    Label questions = new Label("Questions asked: \n\n" + databaseHelper.getUsersQuestions(userName));
	    Label answers = new Label("Answers asked: \n\n" + databaseHelper.getUsersAnswers(userName));
	    //Creates a back button so the user can navigate back to the list of questions
	    Button BackButton = new Button("<- Back");
	    BackButton.setOnAction(a -> {
	    	try {
				new StudentActivityPage(databaseHelper).show(databaseHelper, primaryStage, user);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    });
	    
	    layout.getChildren().addAll(BackButton,questions,answers);
	    
	    Scene userScene = new Scene(scrolly, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Activity Page");
    	
	    }

	}

