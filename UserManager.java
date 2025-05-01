import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
  public boolean Login(String username, String password){

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
  public boolean savePreferences(){


    boolean saveSuccessful = false;

    //TODO

    return saveSuccessful;
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
}

