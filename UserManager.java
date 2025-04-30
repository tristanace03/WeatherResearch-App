import java.util.HashMap;
import java.util.Map;

public class UserManager{
  //handles login, preferenecs and favorite locations 

  private DatabaseManager databaseManager;

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

    //TODO

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
  public boolean addFavoriteLocation(String location){

    boolean favoriteSuccessful = false;

    //TODO

    return favoriteSuccessful;
  }

}
