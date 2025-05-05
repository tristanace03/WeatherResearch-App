import java.sql.*;

public class DatabaseHelper extends AbstractDatabaseHelper{
    private static final String DB_URL = "jdbc:sqlite:weatherApp.db";
    private static DatabaseHelper instance = null;

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
            instance.connect();
            instance.initializeDatabase();  
        }
        return instance;
    }
  /*
   * Create a connection the the database
   */
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
   * Intialize the SQL database using Statements and the Connection we created
   */
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

  @Override
  public Connection getConnection() {
    return connection;
  } 
}
