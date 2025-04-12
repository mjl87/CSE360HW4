
package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.sql.SQLException;


import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;

    /**
     * AdminSetupPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
     * This is for creating an admin account which is for the first user and has textfields being checked by passwordevaluator and username recognizer class.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The object that holds user username, password, and role information.
     */
    public void show(Stage primaryStage) {
    	

        //here I created a new label that shows up in a bold red color when activated
        Label labelname = new Label();
        labelname.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        labelname.setTextFill(Color.RED);
        
    	// Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);


        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        

        Button setupButton = new Button("Setup");
        
    	

        
        setupButton.setOnAction(a -> {
        	boolean checky = true;
        	// Retrieve user input
        	//USERNAMESTUFFHEREEE-----------------------------------------------------------------
            String userName = userNameField.getText();
            
            //this checks the usernamerecognizer class to check if the user name is fine
 			String errMessage = UserNameRecognizer.checkForValidUserName(userName);
 			
 			//conditional if statement checks if there was an error and makes the user redo the username part
			if (errMessage != "") {
				
				//if checky is false then the password section will not work since the user name part wont work
				checky = false;
				
				//System.out.println(errMessage);
				//setText method changes the label to show the error message
				labelname.setText(errMessage);
				
				}

            //END USERNAMESTUFF---------------------------------------------------------------------
            
            //START PASSWROD STUFF HEREEEEEEEEEEEEEE-----------------------------------------------
            String password = passwordField.getText();
            boolean checky2 = true;
            if(checky) {
            	//this calls the passwordevaluator class to check for the user input
            	String resultText= PasswordEvaluator.evaluatePassword(password);
            	
            	//like username section the password section checks if theres an error message
            	if (resultText != "") {

                    labelname.setText(resultText);
            		//System.out.print(resultText);
            		checky2 = false;
            	}
            	
            }
            
            //END PASSWORD STUFF HEREEEEEEEEEEEEEEE----------------------------------------------
           
            //this conditional if statement has checky for username and checky2 for password and checks 
            //if they are both valid to make an account
            if(checky && checky2) {
	            try {
	            	// Create a new User object with admin role and register in the database
	            	User user=new User(userName, password, "admin");
	                databaseHelper.register(user);
	                System.out.println("Administrator setup completed.");
	                
	                // Navigate to the Welcome Login Page
	                //new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
	                new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	            } catch (SQLException e) {
	                System.err.println("Database error: " + e.getMessage());
	                e.printStackTrace();
	            }
            }
        });
        
        //In Vbox i added in labelname to include the label
        VBox layout = new VBox(10, userNameField, passwordField, setupButton, labelname);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        
        
        
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}