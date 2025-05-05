/**
 * A base class for managing database operations.
 * Subclasses need to implement methods for saving/retrieving data, logging observations, and exporting data.
 * 
 * Defines methods for saving/retrieving user prefernces.
 * Supports logging/searching seather observations
 * Provides methods to export data to CSV, save user information, and get observation counts by location.
 * 
 * Used by subclasses such as DatabaseManager to implement specific database operations.
 */
import java.util.Map;

public abstract class AbstractDatabaseManager {

    /**
     * Saves user preferences to the database.
     * 
     * @param username the user's name
     * @param key the preference key
     * @param value preference value 
     */
    public abstract void savePreference(String username, String key, String value);

    /**
     * Retrieves user preferences from the database.
     * 
     * @param username the user's name
     * @param key the preference key
     * @return the preference value, or null if not found
     */
    public abstract String getPreference(String username, String key);

    /**
     * Logs an observation to the database.
     * 
     * @param obs the observation to log
     * @return true if the observation was logged successfully, false otherwise
     */
    public abstract boolean logObservation(Observation obs);

    /**
     * Searches the logs for observations at a specific location.
     * @param location the location to search for
     * @return Observation object containing the search results, or null otherwise. 
     */
    public abstract Observation searchLogs(String location);

    /**
     * Exports the logged observations to a CSV file.
     */
    public abstract void exportToCSV();

    /**
     * Saves user information to the database.
     * 
     * @param user the user to save
     */
    public abstract void saveUser(User user);

    /**
     * Retrieves user information from the database.
     * 
     * @return a map where the key is location and the value is the number of observations at that location
     */
    public abstract Map<String, Integer> getObservationCountsByLocation();
}
