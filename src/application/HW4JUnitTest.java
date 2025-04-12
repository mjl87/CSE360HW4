package application;

import databasePart1.DatabaseHelper;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;

/**
 * <p>JUnit test cases for user generating, user suspending, staff reporting, and broadcast
 *  messaging </p>
 
 * @author Matthew Lidstone
 * 
 * @version 1.0
 */

public class HW4JUnitTest {

    /**
     * Tests that the {@link User} class constructor correctly initializes
     */
	
	@Test
	public void testConstructor() {
        User user = new User("matt", "password123", "admin");
        assertEquals("matt", user.getUserName());
        assertEquals("password123", user.getPassword());
        assertEquals("admin", user.getRole());
	}
	
    /**
     * Tests that the <code> roleChange </code> method in the {@link User} class correctly changes the role of the user
     */
	
	@Test
	public void testRoleChange() {
		User user = new User("matt", "password123", "admin");
		user.setRole("student");
		assertEquals("student", user.getRole());
	}
	
    /**
     * Tests that the <code> getUserName </code> method in the {@link User} class retrieves the correct username when called
     */
	
	@Test
	public void testGetUserName() {
		User user = new User("matt", "password123", "admin");
				assertEquals("matt", user.getUserName());
	}
	
    /**
     * Tests that the <code> getPassword </code> method in the {@link User} class retrieves the correct password when called
     */
	
	@Test
	public void testGetPassword() {
		User user = new User("matt", "password123", "admin");
		assertEquals("password123", user.getPassword());
	}
	
    /**
     * Tests that the <code> getRole </code> method in the {@link User} class retrieves the correct role when called
     */
	
	@Test
	public void testGetRole() {
		User user = new User("matt", "password123", "admin");
		assertEquals("admin", user.getRole());
	}
	
    /**
     * Tests that the <code> suspendUser </code> method in the {@link DatabaseHelper} class functions correctly and suspends the correct user
     */
	
	@Test
	public void testSuspendAndUnsuspendUser() {
	    DatabaseHelper db = new DatabaseHelper();
	    try {
	        db.connectToDatabase();

	        String username = "test";

	        // Ensure user exists (register or skip if already there)
	        if (!db.doesUserExist(username)) {
	            db.register(new User(username, "pass", "student"));
	        }

	        db.suspendUser(username, true);
	        assertTrue("User should be suspended", db.isUserSuspended(username));

	        db.suspendUser(username, false);
	        assertFalse("User should be unsuspended", db.isUserSuspended(username));

	    } catch (Exception e) {
	        fail("Exception occurred: " + e.getMessage());
	    } finally {
	        db.closeConnection();
	    }
	}
	
	  /**
     * Tests that the <code> CreateBroadcastMessage </code> method in the {@link DatabaseHelper} class functions correctly and creates a message in the database
     */
	
	
	@Test
	public void testCreateBroadcastMessage() {
	    DatabaseHelper db = new DatabaseHelper();
	    try {
	        db.connectToDatabase();

	        String testSender = "test";
	        String testMessage = "testmsg";

	        db.createBroadcastMessage(testMessage, testSender);
	        List<String> messages = db.getAllBroadcastMessages();

	        boolean found = messages.stream().anyMatch(m -> m.contains(testMessage) && m.contains(testSender));
	        assertTrue("Broadcast message should exist in list", found);

	    } catch (Exception e) {
	        fail("Exception occurred: " + e.getMessage());
	    } finally {
	        db.closeConnection();
	    }
	}

}

