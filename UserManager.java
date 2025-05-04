import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.Data;

public class UserManager{
  //handles login, preferenecs and favorite locations 


  /**
   * Method to handle a user login, takes in a username and password
   * Returns true on a successful login, false otherwise
   * 
   * @param username
   * @param password
   * @return boolean 
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
   * Method to handle a user saving a preference
   * Returns true when successful, false otherwise
   * 
   * @return boolean
   */
  public boolean savePreferences(String username, String key, String value){


   
    SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
    dbFactory.createDatabaseManager("SQLLite").savePreference(username, key, value);

    return true;
  }

  public String getPreferences(String username, String key){
    SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
    return dbFactory.createDatabaseManager("SQLLite").getPreference(username, key);
  }

  /**
   * Method to add a favorite location for a user
   * Returns true if save successful, false otherwise
   * 
   * 
   * @param location
   * @return boolean
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

    public void createUser(String username, String password) {
        DatabaseManager dbManager = new DatabaseManager();
        User user = new User(username, password);
        dbManager.saveUser(user);
    }

    // Add this method to UserManager.java
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
    return null; // Return null if no favorite location is found
}

// Add this field and methods to manage logged-in users
private String currentUsername = null;

// Call this method to set the logged-in user
public void setLoggedInUser(String username) {
    currentUsername = username;
}

// Call this method to get the current logged-in user
public String getCurrentUsername() {
    return currentUsername;
}

// Call this method to check if a user is logged in
public boolean isLoggedIn() {
    return currentUsername != null;
}

}



