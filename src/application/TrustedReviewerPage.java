
package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class displays the Trusted Reviewer Page in the provided primary stage.
 */
public class TrustedReviewerPage {

	/**
     * Displays the Trusted Reviewer Page in the provided primary stage.
     * 
     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
     * @param primaryStage   The primary stage where the scene will be displayed.
     */
    public void show(DatabaseHelper databaseHelper,Stage primaryStage,User user) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display the title of the page
	    Label userLabel = new Label("Trusted Reviewers");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    Label trustsLabel = new Label(databaseHelper.getTrusts(user.getUserName()));
	    trustsLabel.setStyle("-fx-font-size: 12px;");
	    
	    System.out.println(databaseHelper.getTrusts(user.getUserName()));
        
        Button BackButton = new Button("<- Back");
	    BackButton.setOnAction(a -> {
	    	new UserHomePage(databaseHelper).show(primaryStage, user);
	    });
	    

        layout.getChildren().addAll(userLabel, trustsLabel, BackButton);
	    Scene inviteScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(inviteScene);
	    primaryStage.setTitle("Trusted Reviewers Page");
	
    }
    
}
