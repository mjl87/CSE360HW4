
package application;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private String role;

    // Constructor to initialize a new User object with userName, password, and role.
    /**
     * Constructor to initialize a new User object with userName, password, and role.
     * @param userName The user's username.
     * @param password The user's password
     * @param role The user's role(s).
     */
    public User( String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }
    
    // Sets the role of the user.
    /**
     * Sets the role of the user.
     * @param role The role of a user
     */
    public void setRole(String role) {
    	this.role=role;
    }
    
    /**
     * returns the user's username.
     * @return userName returns the user's username.
     */
    public String getUserName() { return userName; }
    /**
     * returns the user's password.
     * @return password returns the user's password.
     */
    public String getPassword() { return password; }
    /**
     * returns the user's role or roles.
     * @return role returns the user's role or roles.
     */
    public String getRole() { return role; }
}