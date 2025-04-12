package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
	
    private final DatabaseHelper databaseHelper;
    // DatabaseHelper to handle database operations.
    /**
     * SetupAccoundPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Setup Account page in the provided stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	
    	
    	
    	// Input fields for userName, password, and invitation code
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        Button setupButton = new Button("Setup");
        //System.out.print("HIII");
        setupButton.setOnAction(a -> {
        	// Retrieve user input
        	boolean checky = true;
            String userName = userNameField.getText();
            
          //this checks the usernamerecognizer class to check if the user name is fine
 			String errMessage = UserNameRecognizer.checkForValidUserName(userName);
 			
 			//conditional if statement checks if there was an error and makes the user redo the username part
			if (errMessage != "") {
				
				//if checky is false then the password section will not work since the user name part wont work
				checky = false;
				
				//System.out.println(errMessage);
				//setText method changes the label to show the error message
				errorLabel.setText(errMessage);
				
				}
            
            String password = passwordField.getText();
            boolean checky2 = true;
            if(checky) {
            	//this calls the passwordevaluator class to check for the user input
            	String resultText= PasswordEvaluator.evaluatePassword(password);
            	
            	//like username section the password section checks if theres an error message
            	if (resultText != "") {

            		errorLabel.setText(resultText);
            		//System.out.print(resultText);
            		checky2 = false;
            	}
            	
            }
            
            String code = inviteCodeField.getText();
            if(checky && checky2) {
	            try {
	            	// Check if the user already exists
	            	if(!databaseHelper.doesUserExist(userName)) {
	            		
	            		// Validate the invitation code
	            		//Change this to avoid invitation code HEEEEEEEEEEEERRRRRRRRRREEEEEEEEEEE
	            		//if(databaseHelper.validateInvitationCode(code)) {
	            		if(true) {
	            			
	            			// Create a new user and register them in the database
			            	User user=new User(userName, password, "user");
			                databaseHelper.register(user);
			                
			             // Navigate to the Welcome Login Page 
			                //uncoment to go back to original code
			                //new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
			                
			                //CHANGE HEEERREEEEEEEEEEEEEEEEEE go to role select page
			                new RolePage(databaseHelper).role(primaryStage,user);
			               
	            		}
	            		else {
	            			errorLabel.setText("Please enter a valid invitation code");
	            		}
	            	}
	            	else {
	            		errorLabel.setText("This useruserName is taken!!.. Please use another to setup an account");
	            	}
	            	
	            } catch (SQLException e) {
	                System.err.println("Database error: " + e.getMessage());
	                e.printStackTrace();
	            }
            }
        });
        Button BackButton = new Button("<- Back");
        BackButton.setOnAction(a -> {
	    	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	    });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField,inviteCodeField, setupButton, errorLabel, BackButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}