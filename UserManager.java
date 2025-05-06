/**
 * Manages user-related operations such as login, preferences and favorite locations.
 * 
 * Handles user authentication
 * Provides methods to save and retrieve user preferences
 * Manages facorite locations for users
 * Allows for creation of new user accounts
 * 
 * This class acts as the main interfact for user-related functionalities in the application
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.Data;

public class UserManager{

    private String currentUsername = null;

    /**
     * Authenticates a user by verifying their credientals
     * 
     * @param username the username of the user
     * @param password the password of the user
     * @return if login is successful, returns true - otherwise, returns false
     */
    public boolean login(String username, String password){

        boolean loginSuccessful = false;

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                loginSuccessful = true;
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return loginSuccessful;
    }

    /**
     * Saves a user preference
     * 
     * @param username the username of the user
     * @param key the key for the preference
     * @param value the value for the preference
     * @return true if the preference is successfully saved
     */
    public boolean savePreferences(String username, String key, String value){
        SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
        dbFactory.createDatabaseManager("SQLLite").savePreference(username, key, value);
        return true;
    }

    /**
     * Retrieve a user preference
     * 
     * @param username the username of the user
     * @param key the key for the preference
     * @return the value of the preference, or null if not found
     */
    public String getPreferences(String username, String key){
        SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
        return dbFactory.createDatabaseManager("SQLLite").getPreference(username, key);
    }

    /**
     * Adds a favortie location for a user
     * 
     * @param username the username of the user
     * @param location the favorite location to add
     * @return true if the favorite location is added, false if now
     */
     public boolean addFavoriteLocation(String username, String location){

        boolean favoriteSuccessful = false;

        String sql = "INSERT INTO favorites(username, location) VALUES (?, ?)";
        try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, location);
            pstmt.executeUpdate();
            favoriteSuccessful = true;
        } catch (SQLException e) {
            System.out.println("Add favorite location error: " + e.getMessage());
        }  
        return favoriteSuccessful;
    }


    /**
     * Removes a favorite location for a user
     * 
     * @param username the username of the user
     * @param location the favorite location to remove
     * @return true if the favorite location is successfully removed, false otherwise
     */
    public boolean removeFavoriteLocation(String username, String location){

        boolean removeSuccessful = false;

        String sql = "DELETE FROM favorites WHERE username = ? AND location = ?";
        try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, location);
            pstmt.executeUpdate();
            removeSuccessful = true;
        } catch (SQLException e) {
            System.out.println("Remove favorite location error: " + e.getMessage());
        }  

        return removeSuccessful;
    }

    /**
     * Creates a new user account
     * 
     * @param username the username of the user
     * @param password the password of the user
     */
    public void createUser(String username, String password) {
        DatabaseManager dbManager = new DatabaseManager();
        User user = new User(username, password);
        dbManager.saveUser(user);
    }

    /**
     * Retrieves a favorite location for a user
     * 
     * @param username the username of the user
     * @return favorite location, or null if not found
     */
    public String getFavoriteLocation(String username) {
        String sql = "SELECT location FROM favorites WHERE username = ?";
        try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("location");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving favorite location: " + e.getMessage());
        }
        return null; 
    }

    /**
     * Checks if a user is currently logged in
     * 
     * @param username the username of the currently logged-in user
     */
    public void setLoggedInUser(String username) {
        currentUsername = username;
    }

    /**
     * Gets the usernmane of the currently logged-in user
     * 
     * @return the username of the currently logged-in user, or null if no user is logged in
     */
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Checks if a user is currently logged in 
     * 
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUsername != null;
    }

}



