/**
 * A helepr class for managing SQLite database connections and operations.
 * Extends the AbstractDatabaseHelper class to implement specific logic for connecting to
 * and initializing the SQLite database.
 * 
 * Implements methods to connect to the database, initialize tables, and disconnect from the database.
 * Creates necessary tables for storing logs, user preferences, and user information.
 * Follows the Singleton design pattern to ensure only one instance of the database helper exists.
 * 
 * Used by classes such as DatabaseManager to perform database operations.
 */
import java.sql.*;

public class DatabaseHelper extends AbstractDatabaseHelper{

  private static final String DB_URL = "jdbc:sqlite:weatherApp.db";
  private static DatabaseHelper instance = null;

  /**
   * Provides the current database connection instance.
   * 
   * @return active database connection, or null if not connected. 
   */
  public static DatabaseHelper getInstance() {
    if (instance == null) {
      instance = new DatabaseHelper();
      instance.connect();
      instance.initializeDatabase();  
    }
    return instance;
  }
  
  /**
   * Establishes a connection to the SQLite database.
   * 
   *  @return true if the connection is successful, false otherwise
   */
  @Override
  protected boolean connect(){
    try {
      connection =  DriverManager.getConnection(DB_URL);
    }
    catch(SQLException e){
      System.out.println("Database Connection Fail: " + e.getMessage());
      return false;
    }
    return true;
  }

  /*
   * Initializes the database by creating necessary tables if they do not exist.
   */
  @Override
  protected void initializeDatabase() {
    String logsTable = "CREATE TABLE IF NOT EXISTS logs (" +
                   "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                   "location TEXT NOT NULL," +
                   "date TEXT NOT NULL," +
                   "notes TEXT," +
                   "temperature REAL);";
    
   String userPreferencesTable = "CREATE TABLE IF NOT EXISTS preferences (" +
                               "username TEXT NOT NULL," +
                               "key TEXT NOT NULL," +
                               "value TEXT NOT NULL," +
                               "PRIMARY KEY (username, key));";

    String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY," +
                    "password TEXT NOT NULL);";

    String favoritesTable = "CREATE TABLE IF NOT EXISTS favorites (" +
                    "username TEXT NOT NULL," +
                    "location TEXT NOT NULL," +
                    "PRIMARY KEY (username, location)," +
                    "FOREIGN KEY (username) REFERENCES users(username));";

    try (Statement stmt = connection.createStatement()){
        stmt.execute(logsTable);
        stmt.execute(userPreferencesTable);
        stmt.execute(usersTable);
        stmt.execute(favoritesTable);
    } catch (SQLException e) {
      System.out.println("Insert error: " + e.getMessage());
    }
  }

  /**
   * Disconnects from the SQLite database.
   * 
   * @return true if disconnection is successful, false otherwise
   */
  @Override
  protected boolean disconnect() {

    try {
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      System.out.println("Database disconnection error: " + e.getMessage());
      return false;
    }
    return true;
    
  }

  /**
   * Returns the current database connection.
   * 
   * @return the active database connection, or null if not connected
   */
  @Override
  public Connection getConnection() {
    return connection;
  } 
}
