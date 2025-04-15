import java.sql.*;

public class DatabaseManager extends AbstractDatabaseManager{
  //uses SQLite operations for storing logs and preferences 
  
  public DatabaseManager() {
    // Constructor to initialize the database connection
    DatabaseHelper dbHelper = DatabaseHelper.getInstance();
    if (!dbHelper.connect()) {
        System.out.println("Failed to connect to the database.");
    } else {
        dbHelper.initializeDatabase();
    }
  }
  
  /*
   * Save Preferences to the database
   * Also need a Preference class for this
   */
  public void savePreference(String key, String value) {
    String sql = "REPLACE INTO preferences() VALUES()";

    try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {


        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Save preference error: " + e.getMessage());
    }
}

  /*
   * Get Preferences from the database
   */
  public String getPreference(String key) {
    String sql = "SELECT value FROM preferences WHERE key = ?";
    try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
        pstmt.setString(1, key);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("value");
        }
    } catch (SQLException e) {
        System.out.println("Get preference error: " + e.getMessage());
    }
    return null;
  }

  public boolean logObservation(Observation obs){
    String sql = "INSERT INTO logs(location, timestamp, notes, temperature) VALUES (?, ?, ?, ?)";

    try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, obs.getLocation());
            pstmt.setString(2, obs.getTimestamp().toString());
            pstmt.setString(3, obs.getNotes());
            pstmt.setDouble(4, obs.getTemperature());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error logging observation: " + e.getMessage());
            return false;
        }
    }



    /*
     * Method to search the logbook entries
     */
    public Observation searchLogs(Date timestamp){
        String sql = "SELECT * FROM logs WHERE timestamp = ?";
        try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setDate(1, timestamp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Observation(rs.getString("location"), rs.getDate("timestamp"), rs.getString("notes"), rs.getDouble("temperature"));
            }
        } catch (SQLException e) {
            System.out.println("Error searching logs: " + e.getMessage());
        }
      return null;
    }

     /*
      * Method to export to a CSV or a PDF
      */
}



