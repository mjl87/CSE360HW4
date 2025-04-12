package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

/**
 * This class is where the user can assign a trust rating to a reviewer
 */
public class AssignTrustPage {

	/**
     * Displays the Assign Trust Page in the provided primary stage. Performs input validation on trust rating to ensure integer value between 1 and 10.
     * 
     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
     * @param primaryStage   The primary stage where the scene will be displayed.
     */
    public void show(DatabaseHelper databaseHelper,Stage primaryStage,User user, String reviewer) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display the title of the page
	    Label userLabel = new Label("Trust New Reviewer");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    
	    TextField trustTextField = new TextField();
	    
	    trustTextField.setPromptText("Trust Level (integer 1-10)");
	    trustTextField.setMaxWidth(100);
	    
	    Label errorLabel = new Label("Input must be integer 1-10");
	    
	    Button setTrust = new Button("Set Trust Level");
	    
	    layout.getChildren().addAll(userLabel, trustTextField, setTrust);
	   
	    
	    
	    setTrust.setOnAction(a -> {
	    	int trust;
	    	
	    	try {
	    		trust = Integer.parseInt(trustTextField.getText());
	    		System.out.print(trust);
	    		if(trust > 10 || trust < 1) {
	    			layout.getChildren().add(errorLabel);
	    		} else {
	    			databaseHelper.addTrust(user.getUserName(), reviewer, trust);
	    			new TrustedReviewerPage().show(databaseHelper, primaryStage, user);
	    		}
	    		
	    	} catch (Exception e) {
	    		layout.getChildren().add(errorLabel);
	    	}
	    });

        
	    Scene inviteScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(inviteScene);
	    primaryStage.setTitle("Invite Page");
	
    }
    
}