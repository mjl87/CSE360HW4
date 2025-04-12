package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

/**
 * This class allows the user to pick what role they want, and they can choose either Student, Reviewer,
 * Instructor, or Staff role
 */
public class RolePage {
	
	private final DatabaseHelper databaseHelper;

    /**
     * RolePage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
    public RolePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    private int inc = 0;
    public static String roles = "";
    /**
     * role method allows the user to pick which role they want and will be added to their roles in the database
	 * @param primaryStage The primary stage where the scene will be displayed.
	 * @param user The user where user name, password, and role is stored for user.
     */
	public void role(Stage primaryStage, User user) {
		
		
		
		VBox layout = new VBox(10);
		Label labelstuff = new Label("Pick one or more roles");
		Label morelabelstuff = new Label();
		CheckBox adminChecky = new CheckBox("Admin");
		CheckBox studChecky = new CheckBox("Student");
		CheckBox revChecky = new CheckBox("Reviewer");
		CheckBox instChecky = new CheckBox("Instructor");
		CheckBox staffChecky = new CheckBox("Staff");
		Button buttonstuff = new Button("Apply");
		
		
		studChecky.setOnAction(a -> {
			if (inc > 0) {
				roles += ", ";
			}
			if(studChecky.isSelected()) {
				inc ++;
				roles += "Student";
			}

		});
		revChecky.setOnAction(a -> {
			if (inc > 0) {
				roles += ", ";
			}
			if(revChecky.isSelected()) {
				inc ++;
				roles += "Reviewer";
			}

		});
		instChecky.setOnAction(a -> {
			if (inc > 0) {
				roles += ", ";
			}
			if(instChecky.isSelected()) {
				inc ++;
				roles += "Instructor";
			}
			
		});
		staffChecky.setOnAction(a -> {
			if (inc > 0) {
				roles += ", ";
			}
			if(staffChecky.isSelected()) {
				inc ++;
				roles += "Staff";
			}

		});

		buttonstuff.setOnAction(a -> {
			if(inc != 0) {
				try {
					databaseHelper.addRoles(roles, user);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
			}
			if(inc == 0) {
				morelabelstuff.setText("Must select at least one role");
			}
		});
		
		
		
		
		

		
		layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
		layout.getChildren().addAll(labelstuff, studChecky, revChecky, instChecky, staffChecky, buttonstuff, morelabelstuff);

		

		
		Scene welcomeScene = new Scene(layout, 800, 400);
		primaryStage.setScene(welcomeScene);
	    primaryStage.setTitle("Role Page");
		
	}

}