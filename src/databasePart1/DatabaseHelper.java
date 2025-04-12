package databasePart1;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Answer;
import application.Question;
import application.User;
import application.Review;

/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt
	
    
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
			    + "id INT AUTO_INCREMENT PRIMARY KEY, "
			    + "userName VARCHAR(255) UNIQUE, "
			    + "password VARCHAR(255), "
			    + "role VARCHAR(255), "
			    + "suspended BOOLEAN DEFAULT FALSE)";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(invitationCodesTable);
	    //HW2 Tables start here
	    //Creates a new table that stores an ID, the question, the author of the question and the time the question was created
	    String questionTable = "CREATE TABLE IF NOT EXISTS questions ("
	    		+ "id INT AUTO_INCREMENT PRIMARY KEY, "
	    		+ "question TEXT NOT NULL, "
	    		+ "author VARCHAR(255) NOT NULL, "
	    		+ "timeOfCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
	    statement.execute(questionTable);
	    //Creates a new table that stores an ID, the id of the question that it is answering, the answer to that question, the author of the answer and the time the question was created	
	    String answerTable = "CREATE TABLE IF NOT EXISTS answers ("
	    		+ "id INT AUTO_INCREMENT PRIMARY KEY, "
	    		+ "questionID INT NOT NULL, "
	    		+ "answer TEXT NOT NULL, "
	    		+ "author VARCHAR(255) NOT NULL, "
	    		+ "timeOfCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
	    statement.execute(answerTable);
	    //Creates a new table of alerts that the admin can see
	    //Still needs to be implemented
	    String alertTable = "CREATE TABLE IF NOT EXISTS alerts ("
	    		+ "id INT AUTO_INCREMENT PRIMARY KEY, "
	    		+ "alert TEXT NOT NULL, "
	    		+ "userName VARCHAR(255) NOT NULL, "
	    		+ "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
	    statement.execute(alertTable);
	    String requestRoleTable = "CREATE TABLE IF NOT EXISTS requestRole ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "requestReviewer VARCHAR(255))";
		
		statement.execute(requestRoleTable);
	    //HW2 Tables end here
		
		//Table to store review comments for answers
		String reviewTable = "CREATE TABLE IF NOT EXISTS reviews ("
		        + "id INT AUTO_INCREMENT PRIMARY KEY, "
		        + "answerID INT NOT NULL, "
		        + "review TEXT NOT NULL, "
		        + "reviewer VARCHAR(255) NOT NULL, "
		        + "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
		statement.execute(reviewTable);
		
		String trustTable = "CREATE TABLE IF NOT EXISTS trusts ("
		        + "id INT AUTO_INCREMENT PRIMARY KEY, "
		        + "truster VARCHAR(255), "
		        + "trustee VARCHAR(255),"
		        + "trustWeight INT NOT NULL)";
		statement.execute(trustTable);
		
		//Table to store messages 
        String messageTable = "CREATE TABLE IF NOT EXISTS messages ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "sender VARCHAR(255) NOT NULL, "
                + "receiver VARCHAR(255) NOT NULL, "
                + "message TEXT NOT NULL, "
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        statement.execute(messageTable);
        
        //table for broadcast messages
        String broadcastTable = "CREATE TABLE IF NOT EXISTS broadcast_messages ("
        	    + "id INT AUTO_INCREMENT PRIMARY KEY, "
        	    + "message TEXT NOT NULL, "
        	    + "createdBy VARCHAR(255), "
        	    + "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        	statement.execute(broadcastTable);
        
	}
	

	// Check if the database is empty
	/**
	 * Checks if database is empty
	 * @return true if empty and return false if detected stuff in database
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	// Registers a new user in the database.
	/**
	 * Adds data into the rows of the database utilizing the INSERT INTO functionality
	 * @param user The user object includes the user's username, password, and role.
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, role) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			pstmt.executeUpdate();
		}
	}
	// Adds the roles to the user
	// 2nd New Users Story
	/**
	 * addRoles puts the role into the database where the correct name is located and updates that space
	 * @param role The role can either be Student, reviewer, instructor, and staff and is inserted into the database using UPDATE keyword
	 * @param user The user object includes the user's username, password, and role.
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void addRoles(String role, User user) throws SQLException {
		String insertRoles = "UPDATE cse360users SET role = ? WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(insertRoles)) {
			pstmt.setString(1, role);
			pstmt.setString(2, user.getUserName());
			pstmt.executeUpdate();
		}
	}
	// Grabs all users and their roles from the db
	// 4th Admin Story
	/**
	 * This method uses the SELECT functionality, which grabs the usernames and roles from the cse360users
	 * @return This method returns all the users along with the roles they have.
	 */
	public String userAndRoles() {
	    String query = "SELECT userName, role FROM cse360users";
	    StringBuilder result = new StringBuilder();

	    try (PreparedStatement statement = connection.prepareStatement(query);
	         ResultSet resultSet = statement.executeQuery()) {

	        while (resultSet.next()) {
	            String username = resultSet.getString("userName");
	            String role = resultSet.getString("role");
	            result.append(username).append(" - ").append(role).append("\n");
	        }

	    } catch (SQLException e) {
	        return "Error retrieving user and roles";
	    }

	    return result.length() > 0 ? result.toString().trim() : "No users yet";
	}
	
	// deletes a user from the db by querying by their username
	// 3rd Admin Story
	/**
	 * deletes a user from the db by querying by their username
	 * @param user The user object includes the user's username, password, and role.
	 */
	public void deleteUser(String userName) {
		String query = "DELETE FROM cse360users WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, userName);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Validates a user's login credentials.
	/**
	 * This method uses SELECT grabbing userName, password, and role and validates user's login credentials
	 * @param user The user object includes the user's username, password, and role.
	 * @return This returns true if there there is a valid row and false if there isnt
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return !rs.getBoolean("suspended");
	            }
	        }
	    }
	    return false;
	}
		
	
	
	// Checks if a user already exists in the database based on their userName.
	/**
	 * Checks if a user already exists in the database based on their userName.
	 * @param userName The userName is the user's userName as string parameter
	 * @return This returns true if user is found and false if user is not found
	 */
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Retrieves the role of a user from the database using their UserName.
	/**
	 * Retrieves the role of a user from the database using their UserName.
	 * @param userName The userName is the user's userName as string parameter
	 * @return if the user exists then the user's role gets returned
	 */
	public String getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("role"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	// Generates a new invitation code and inserts it into the database.
	/**
	 * Generates a new invitation code and inserts it into the database.
	 * @return returns the invitation code
	 */
	public String generateInvitationCode() {
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (code) VALUES (?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	/**
	 * Validates an invitation code to check if it is unused.
	 * @param code The code is the invitation code a user inputs to create an account
	 * @return returns true if the invite code is found and false if the code is not found in the db
	 */

	public boolean validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	//HW2 Functions start here
	//This takes in two parameters and creates a new row in my questions table
	/**
	 * This method finds inserts the question and user into a new data table for only storing questions
	 * @param question The question is the question the user filled out
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void createQuestion(Question question) throws SQLException {
		String input = "INSERT INTO questions (question, author) VALUES (?,?)";
		try (PreparedStatement pstmt = connection.prepareStatement(input)) {
			pstmt.setString(1, question.getQuestion());
			pstmt.setString(2, question.getAuthor());
			pstmt.executeUpdate();
		}
	}
	//This takes in three parameters and creates a new row in my answers table
	/**
	 * This method inserts the answer, author, and question id into the answer database
	 * @param answer The answer is the answer the user just filled out for a question
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void createAnswer(Answer answer) throws SQLException {
		String input = "INSERT INTO answers (questionID, answer, author) VALUES (?,?,?)";
		try (PreparedStatement pstmt = connection.prepareStatement(input)) {
			pstmt.setInt(1, answer.getQuestionID());
			pstmt.setString(2, answer.getAnswer());
			pstmt.setString(3, answer.getAuthor());
			pstmt.executeUpdate();
		}
	}
	//This queries the database for all questions that are asked
	/**
	 * This queries the database for all questions that are asked to get a question from that user
	 * @return an array list for the information about user and question
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public List<String> getQuestions() throws SQLException {
        String query = "SELECT * FROM questions";
        List<String> questions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String question = resultSet.getString("question");
                String author = resultSet.getString("author");
                String timeOfCreation = resultSet.getString("timeOfCreation");
                
                //Formats the question
                String formattedQuestion = id + ".\n"
                        + "User: " + author + "\n"
                        + question + "\n"
                        + "Posted on: " + timeOfCreation + "\n\n";

                questions.add(formattedQuestion); // Adds each question as a string to the list
            }

        } catch (SQLException e) {
            e.printStackTrace(); //Logs the error
            return new ArrayList<>(); //Returns an empty list instead of a string (to match method signature)
        }

        return questions.isEmpty() ? List.of("No questions yet") : questions; //Returns "No questions yet" if list is empty
    }
	
	//This queries the database for all reviews associated with an answer
	/**
	 * This queries the database for all reviews associated with an answer and puts the information into an array list
	 * @param answerID The answerID is the answer's id in the answer datatable
	 * @return return type is a array list that stores the information about the review
	 */
	public List<Review> getReview(int answerID) {
	    String query = "SELECT * FROM reviews WHERE answerID = ?";
	    List<Review> reviews = new ArrayList<>();

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, answerID);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String review = rs.getString("review");
	            String reviewer = rs.getString("reviewer");
	            String time = rs.getString("time");
	            reviews.add(new Review(id, answerID, review, reviewer, time));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return reviews;
	}

	//This queries the database using the questionID to grab all the answers for a specific question
	/**
	 * This queries the database using the questionID to grab all the answers for a specific question
	 * @param questionID The question id gets the user based on the question of the id
	 * @return this returns the author and answer and stops after the while loop has ended
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public String getAnswers(int questionID) throws SQLException {
		String query = "SELECT * FROM answers WHERE questionID = " + questionID;
	    StringBuilder result = new StringBuilder();

	    try (PreparedStatement statement = connection.prepareStatement(query);
	         ResultSet resultSet = statement.executeQuery()) {

	        while (resultSet.next()) {
	        	String id = resultSet.getString("id");
	            String answer = resultSet.getString("answer");
	            String author = resultSet.getString("author");
	            String timeOfCreation = resultSet.getString("timeOfCreation");
	            //Formats the answer
	            result.append(id).append(".user: ").append(author).append("- " + getUserRole(author)).append("\n").append(answer).append("\n").append("Posted on: ").append(timeOfCreation).append("+=+=+=+=+=+=");
	        }

	    } catch (SQLException e) {
	        return "Error retrieving answer";
	    }

	    return result.length() > 0 ? result.toString().trim() : "No answers yet";	
	}
	//This grabs a specific question using the id of the question
	/**
	 * This grabs a specific question using the id of the question
	 * @param id specifies which user id it is to grab correct question
	 * @return returns as a string array list of questions
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public String getQuestion(int id) throws SQLException{
			String query = "SELECT * FROM questions WHERE id = " + id;
		    StringBuilder result = new StringBuilder();

		    try (PreparedStatement statement = connection.prepareStatement(query);
		         ResultSet resultSet = statement.executeQuery()) {

		        while (resultSet.next()) {
		            String question = resultSet.getString("question");
		            String author = resultSet.getString("author");
		            String timeOfCreation = resultSet.getString("timeOfCreation");
		            //Formats the Question
		            result.append(id).append(".\n").append("user: ").append(author).append("\n").append(question).append("\n").append("Posted on: ").append(timeOfCreation).append("\n\n");
		        }

		    } catch (SQLException e) {
		        return "Error retrieving question";
		    }

		    return result.length() > 0 ? result.toString().trim() : "No questions yet";	
		}
	//This deletes the question and all of its answers using it's id
	/**
	 * This deletes the question and all of its answers using it's id
	 * @param id The id is the question id as a int parameter
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void deleteQuestion(int id) throws SQLException{
		//Deletes the question
		String delete = ("DELETE FROM questions WHERE id = " + id);
		PreparedStatement statement = connection.prepareStatement(delete);
			statement.executeUpdate();
		//Deletes all answers that are connected to the question
		String deleteAnswers = ("DELETE FROM answers WHERE questionID = " + id);
			PreparedStatement task = connection.prepareStatement(deleteAnswers);
			task.executeUpdate();
	}
	//This still needs work to be implemented
	/**
	 * This still needs work to be implemented, but is supposed to delete the answer
	 * @param id grabs the answer id
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void deleteAnswer(int id) throws SQLException{
		String delete = ("DELETE FROM answers WHERE id = " + id);
		PreparedStatement statement = connection.prepareStatement(delete);
			statement.executeUpdate();
	}
	//This gets the author of the question by querying the questions table by it's id
	/**
	 * This gets the author of the question by querying the questions table by it's id
	 * @param id The is the question's id
	 * @return if the author with the question id is found then returns that author
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public String getQuestionAuthor(int id) throws SQLException{
		String query = ("SELECT author FROM questions WHERE id = " + id);
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("author"); // Returns the author of the question
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If an error occurs
	}
	//This gets the author of an answer using the id of the answer
	/**
	 * This gets the author of an answer using the id of the answer
	 * @param id The id is the answer id
	 * @return this returns the author based off of the answer id
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public String getAnswerAuthor(int id) throws SQLException{
		String query = ("SELECT author FROM answers WHERE id = " + id);
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("author"); // Return the author of the answer
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If an error occurs
	}
	
	//HW2 Functions end here
	// Marks the invitation code as used in the database.
	/**
	 * Marks the invitation code as used in the database.
	 * @param code marks database to show used invitation code
	 */
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	//finds an answer from the database depending on the id number it has
	/**
	 * finds an answer from the database depending on the id number it has
	 * @param numer The numer is the answer id number
	 * @return this returns the answer and author of that answer if id number is found
	 */
	public String findAnswer(int numer) {

		String insertUser = "SELECT (answer) FROM answers WHERE id=" + numer;
	   
	    String test = "";
	    try (PreparedStatement statement = connection.prepareStatement(insertUser);
		         ResultSet resultSet = statement.executeQuery()) {
		    	
		        while (resultSet.next()) {
		            String username = resultSet.getString("answer");
		            test = test + username;
		            
		        }
		        

	    } catch (SQLException e) {
	    	e.printStackTrace();
	        
	    }

	    if(test.length() == 0) {
	    	return "input can't be empty";
	    }
	    
	    return test;

	}
	//updates database to have a checkmark where the user wants it
	/**
	 * updates database to have a checkmark where the user wants it
	 * @param answer The answer is the answer about to be highlighted
	 * @param num The num is the answer id
	 */
	public void highlightAnswer(String answer, int num) {
		String insertUser = "UPDATE answers SET answer = ? WHERE id=" + num;
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, answer + " (CHECKMARK)");
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
	    	e.printStackTrace();
	        
	    }

	}
	//updates database to update answer
	/**
	 * updates database to update answer
	 * @param answer The answer is the answer about to be updated
	 * @param num The num is answer id
	 */
	public void updateAnswer(String answer, int num) {
		String insertUser = "UPDATE answers SET answer = ? WHERE id=" + num;
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, answer);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
	    	e.printStackTrace();
	        
	    }
		
	}
	
	//-------------------------Getters for staff dash report----------------------------------------------------------------------------------
		//
	public int getUserCount() {
	    return getCount("cse360users");
	}

	public int getQuestionCount() {
	    return getCount("questions");
	}

	public int getAnswerCount() {
	    return getCount("answers");
	}

	public int getReviewCount() {
	    return getCount("reviews");
	}

	private int getCount(String tableName) {
	    String sql = "SELECT COUNT(*) FROM " + tableName;
	    try (PreparedStatement stmt = connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        return rs.next() ? rs.getInt(1) : 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	
	//-------------------------suspendUser method----------------------------------------------------------------------------------	
	
	public void suspendUser(String userName, boolean suspend) throws SQLException {
	    String query = "UPDATE cse360users SET suspended = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setBoolean(1, suspend);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    }
	}
	
	//-------------------------Reviwer permission----------------------------------------------------------------------------------
	//detes the user and role row from requestrole table
	/**
	 *deletes the user and role row from requestrole table 
	 * @param userName The username represents the user's username
	 * @param role The role represents the user's role
	 */
	public void deleteUserandRole(String userName, String role) {
		String query = "DELETE FROM requestRole WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, userName);


			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// Registers asks for review role in the database.
	/**
	 *  Registers asks for review role in the database.
	 * @param user The user represents the user object with username, password, and role
	 * @param request The request is the role the user wants to have
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void rolePermission(User user, String request) throws SQLException {
		String insertUser = "INSERT INTO requestRole (userName, requestReviewer) VALUES (?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, request);
			pstmt.executeUpdate();
		}
	}
	//grabbing the all username and role from table
	/**
	 * grabbing the all username and role from table
	 * @return returns all username and roles from reuestRole table
	 */
	public String PermissionUserandRoles() {
	    String query = "SELECT userName, requestReviewer FROM requestRole";
	    StringBuilder result = new StringBuilder();

	    try (PreparedStatement statement = connection.prepareStatement(query);
	         ResultSet resultSet = statement.executeQuery()) {

	        while (resultSet.next()) {
	            String username = resultSet.getString("userName");
	            String role = resultSet.getString("requestReviewer");
	            result.append(username).append("-").append(role).append("-");
	        }

	    } catch (SQLException e) {
	        return "Error retrieving user and roles";
	    }

	    return result.length() > 0 ? result.toString().trim() : "No users yet";
	}

	/**
	 * This adds the trust data into the database along with the weight of the trust
	 * @param truster The truster is the reviewer 
	 * @param trustee The trustee is the student user who is trusting
	 * @param trustWeight The trustWeight holds the amount of trust in a reviewer
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void addTrust(String truster, String trustee, int trustWeight) throws SQLException {
		String insert = "INSERT INTO trusts (truster, trustee, trustWeight) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insert)) {
			pstmt.setString(1, truster);
			pstmt.setString(2,  trustee);
			pstmt.setInt(3, trustWeight);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * This gets the student trustee and the weight of their trust of a reviewer
	 * @param truster is the reviewer
	 * @return this returns the user and trust weight for the reviewer
	 */
	public String getTrusts(String truster) {
		String query = "SELECT * FROM trusts WHERE truster = ?";
		StringBuilder result = new StringBuilder();
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, truster);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String trustee = rs.getString("trustee");
				int trustWeight = rs.getInt("trustWeight");
				
				result.append("Trustee: ").append(trustee).append(" - Trust Weight: ").append(String.valueOf(trustWeight)).append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error retrieving trusts";
		}
		
		return result.length() > 0 ? result.toString().trim() : "No trusted reviewers yet.";
	}
	
	//----------------------------------------------------------------------------
	
	//method to insert a review
	/**
	 * method to insert a review
	 * @param answerID The answerID is the answer's id
	 * @param review The review is the review
	 * @param reviewer The reviewer is the author of the review
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void addReview(int answerID, String review, String reviewer) throws SQLException {
	    String insert = "INSERT INTO reviews (answerID, review, reviewer) VALUES (?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(insert)) {
	        pstmt.setInt(1, answerID);
	        pstmt.setString(2, review);
	        pstmt.setString(3, reviewer);
	        pstmt.executeUpdate();
	    }
	}
	
	//method to update a review
	/**
	 * method to update a review
	 * @param reviewId The reviewId is the id of the review
	 * @param newReview The newReview is the updated review
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void updateReview(int reviewId, String newReview) throws SQLException {
	    String query = "UPDATE reviews SET review = ? WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, newReview);
	        pstmt.setInt(2, reviewId);
	        pstmt.executeUpdate();
	    }
	}
	
	//method to delete a review
	/**
	 * method to delete a review
	 * @param reviewId The reviewId is the review id
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void deleteReview(int reviewId) throws SQLException {
	    String query = "DELETE FROM reviews WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, reviewId);
	        pstmt.executeUpdate();
	    }
	}
	

	//----------------------------------------------------------------------------
	
	//getter to get review to display with the answer
	/**
	 * getter to get review to display with the answer
	 * @param answerID The answerID is the answer's id
	 * @return this returns the reviews for given answer
	 */
	public String getReviewsForAnswer(int answerID) {
	    String query = "SELECT * FROM reviews WHERE answerID = ?";
	    StringBuilder result = new StringBuilder();

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, answerID);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            String reviewer = rs.getString("reviewer");
	            String review = rs.getString("review");
	            String time = rs.getString("time");

	            result.append("- Reviewer: ").append(reviewer).append("\n")
	                  .append("  ").append(review).append("\n")
	                  .append("  (").append(time).append(")").append("\n\n");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error retrieving reviews.";
	    }

	    return result.length() > 0 ? result.toString().trim() : "No reviews yet.";
	}
	
	//Send a message
	/**
	 * Send a message
	 * @param sender The sender variable holds the sender's username
	 * @param receiver The receiver variable holds the receiver's username
	 * @param message The message variable is the message the sender sent
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public void sendMessage(String sender, String receiver, String message) throws SQLException {
	    String query = "INSERT INTO messages (sender, receiver, message) VALUES (?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, sender);
	        pstmt.setString(2, receiver);
	        pstmt.setString(3, message);
	        pstmt.executeUpdate();
	    }
	}

	//Get messages between two users
	/**
	 * Get messages between two users
	 * @param user1 The user1 is the first user in messaging
	 * @param user2 The user2 is the second user in messaging
	 * @return This returns the senders and their messages
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public String getMessages(String user1, String user2) throws SQLException {
	    String query = "SELECT * FROM messages WHERE "
	        + "(sender = ? AND receiver = ?) OR "
	        + "(sender = ? AND receiver = ?) "
	        + "ORDER BY timestamp ASC";
	    
	    StringBuilder result = new StringBuilder();
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, user1);
	        pstmt.setString(2, user2);
	        pstmt.setString(3, user2);
	        pstmt.setString(4, user1);
	        
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            String sender = rs.getString("sender");
	            String message = rs.getString("message");
	            String timestamp = rs.getString("timestamp");
	            
	            result.append("[").append(timestamp).append("] ")
	                  .append(sender).append(": ")
	                  .append(message).append("\n");
	        }
	    }
	    
	    return result.length() > 0 ? result.toString() : "No messages yet.";
	}
	
	//method to save staff broadcast message to instructors 
	public void createBroadcastMessage(String message, String createdBy) throws SQLException {
	    String sql = "INSERT INTO broadcast_messages (message, createdBy) VALUES (?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, message);
	        pstmt.setString(2, createdBy);
	        pstmt.executeUpdate();
	    }
	}
	
	//array to hold all messages to display
	public List<String> getAllBroadcastMessages() {
	    List<String> messages = new ArrayList<>();
	    String sql = "SELECT message, createdBy, createdAt FROM broadcast_messages ORDER BY createdAt DESC";
	    try (PreparedStatement stmt = connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	            String entry = "[From: " + rs.getString("createdBy") + " at " + rs.getString("createdAt") + "]\n"
	                         + rs.getString("message");
	            messages.add(entry);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return messages;
	}

	// Get all users for messaging
	/**
	 * Get all users for messaging
	 * @return this returns arraylist containing the users for messaging
	 * @throws SQLException if there's a database access error or other error then provides error information
	 */
	public List<String> getAllUsers() throws SQLException {
	    String query = "SELECT userName FROM cse360users";
	    List<String> users = new ArrayList<>();
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query);
	         ResultSet rs = pstmt.executeQuery()) {
	        
	        while (rs.next()) {
	            users.add(rs.getString("userName"));
	        }
	    }
	    
	    return users;
	}

	/**
	 * This grabs all the questions an author has and returns it
	 * @param userName The userName is the user being selected
	 * @return returns all the questions the user created
	 */

	public String getUsersQuestions(String userName) {
	    String query = "SELECT * FROM questions WHERE author = ?";
	    StringBuilder result = new StringBuilder();

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, userName);
	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            String question = resultSet.getString("question");
	            String author = resultSet.getString("author");
	            String timeOfCreation = resultSet.getString("timeOfCreation");
	            result.append("user: ").append(author)
	                  .append("\n").append(question)
	                  .append("\nPosted on: ").append(timeOfCreation)
	                  .append("\n\n");
	        }
	    } catch (SQLException e) {
	        return "Error retrieving questions";
	    }

	    return result.length() > 0 ? result.toString().trim() : "No questions yet";    
	}


	/**
	 * This grabs all the answers an author has and returns it
	 * @param userName The userName is the user being selected
	 * @return returns all the answers the user created
	 */

	public String getUsersAnswers(String userName) {
	    String query = "SELECT * FROM answers WHERE author = ?";
	    StringBuilder result = new StringBuilder();

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, userName);
	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            String id = resultSet.getString("id");
	            String answer = resultSet.getString("answer");
	            String author = resultSet.getString("author");
	            String timeOfCreation = resultSet.getString("timeOfCreation");
	            result.append(id).append(". user: ").append(author)
	                  .append(" - ").append(getUserRole(author)).append("\n")
	                  .append(answer).append("\nPosted on: ");
	        }

	    } catch (SQLException e) {
	        return "Error retrieving answers";
	    }

	    return result.length() > 0 ? result.toString().trim() : "No answers yet";    
	}
	//-----------------------------------for JUnit Testing----------------------------------------

	
	public boolean isUserSuspended(String username) {
	    String sql = "SELECT suspended FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getBoolean("suspended");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // Default to false if not found or error
	}

	//----------------------------------------------------------------------------

	// Closes the database connection and statement.
	/**
	 * Closes the database connection and statement.	
	 */
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}



}
