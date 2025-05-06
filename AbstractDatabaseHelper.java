/**
 * A base class for handling databse connections and operations.
 * Subclasses should implement the specific database operations.    
 * 
 * Manages the connection to the database and provides methods for initializing, connecting and disconnecting to the database.
 * 
 * Used by subclasses such as DatabaseHelper to implement specific dtabase operations.  
 */
import java.sql.Connection;

public abstract class AbstractDatabaseHelper {
    protected Connection connection = null;

    /**
     * Connects to the database
     * 
     * @return true if connection is successful, false otherwise
     */
    protected abstract boolean connect();

    /**
     * Sets up database tables and initializes the database.
     */
    protected abstract void initializeDatabase();

    /**
     * Disconnects from the database
     * 
     * @return true if disconnection is successful, false otherwise
     */
    protected abstract boolean disconnect();

    /**
     * Gets the current database connection
     * 
     * @return the datbase connection, or null if not connected
     */
    public abstract Connection getConnection();

}
