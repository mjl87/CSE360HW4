
package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or quit the application.
 */
public class AdminRolePermission {
	
	private final DatabaseHelper databaseHelper;

    /**
     * AdminRolePermission constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed.
     */
    public AdminRolePermission(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    /**
     * UI for listing the users who want to become a reviewer while having admin select yes or no to give them permission of role
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The object that holds user username, password, and role information.
     */
    public void show( Stage primaryStage, User user) {
    	
    	VBox layout = new VBox(5);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    //allows scrolling functionality
    	ScrollPane scrolly = new ScrollPane();
	    scrolly.setPrefSize(69, 69);
	    scrolly.setContent(layout);
	    
	    
	    String[] UserAndRole = databaseHelper.PermissionUserandRoles().split("-");
	   // labely.setText(databaseHelper.PermissionUserandRoles());
	    System.out.print(UserAndRole[0]);
	    System.out.print(UserAndRole.length);
	    
	    //loop generates list of users who asks for permission to be reviewer role
	    if(!UserAndRole[0].equals("No users yet")) {
	        for (int i = 0; i <= UserAndRole.length-1; i++) {
	        	
	        	Label labely = new Label();
	        	
	        	
	        	Button yesButton = new Button("yes");
	        	Button noButton = new Button("no");
	        	String grabName = UserAndRole[i];
	        	String grabRole = UserAndRole[i+1];
	        	labely.setText("Give " + grabName +" the "+ grabRole + " role?");
	        	i++;
	        	
	        	yesButton.setOnAction(a -> {
	        		
	        		User user2 = new User(grabName, null, null);
	        		try {
	        			//adding reviewer role to user's role
						databaseHelper.addRoles(databaseHelper.getUserRole(grabName)+ ", Reviewer", user2);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		databaseHelper.deleteUserandRole(grabName, grabRole);
	        		new AdminRolePermission(databaseHelper).show(primaryStage, user);
	        	});
	        	noButton.setOnAction(a -> {
	        		databaseHelper.deleteUserandRole(grabName, grabRole);
	        		new AdminRolePermission(databaseHelper).show(primaryStage, user);
	        	});
	        	
	        	layout.getChildren().addAll(labely, yesButton, noButton);
	        }
	    }
	    else {
	    	Label labely = new Label("No users yet");
	    	layout.getChildren().add(labely);
	    }
    
	    
	      Button BackButton = new Button("<- Back");
	      BackButton.setOnAction(a -> {
	          new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
	      });
	      layout.getChildren().add(BackButton);
	    
	    //labely.setText("Give"+"Permission for"+"Role:");
	    
	    //layout.getChildren().addAll(labely);
	    Scene RolePermScene = new Scene(scrolly, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(RolePermScene);
	    primaryStage.setTitle("Role Permissions Page");
    }
}