/**
 * Represents a user in the Weather Research App
 * 
 * Stores user credientals using username and password.
 * Provides methods to access the stored user information.
 * 
 */
public class User {
    private String username;
    private String password;

    /**
     * Constructor method for User
     * 
     * @param username the username of the user
     * @param password the password of the user
     */
    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username of the user
     * 
     * @return the username of the user in String format
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user
     * 
     * @return the password of the user in String format
     */
    public String getPassword() {
        return password;
    }
}
