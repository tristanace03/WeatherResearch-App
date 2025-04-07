import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = ""; // Need to add database url when we create it

  /*
   * Create a connection the the database
   */
  public static Connection connect(){
    try {
      return DriverManager.getConnection(DB_URL);
    }
    catch(SQLException e){
      System.out.println("Database Connection Fail: " + e.getMessage());
      return null;
    }
  }

  /*
   * Intialize the SQL database using Statements and the Connection we created
   */
  public static void intializeDatabase() {
    String logsTable = "CREATE TABLE IF NOT EXISTS logs (" +
                           "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                           "timestamp TEXT NOT NULL," +
                           "message TEXT NOT NULL);";
    
    String userPreferencesTable = "CREATE TABLE IF NOT EXISTS preferences (" +
                            "key TEXT PRIMARY KEY," +
                            "value TEXT NOT NULL);";


    try (Connection conn = connect(); Statement stmt = conn.createStatement()){
        stmt.execute(logsTable);
        stmt.execute(userPreferencesTable);
    } catch (SQLException e) {
      System.out.println("Insert error: " + e.getMessage());
    }
  } 
}
