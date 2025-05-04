import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
   */
  public void savePreference(String username, String key, String value) {
    String sql = "REPLACE INTO preferences(username, key, value) VALUES (?, ?, ?)";
    try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
        pstmt.setString(1, username);
        pstmt.setString(2, key);
        pstmt.setString(3, value);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Save preference error: " + e.getMessage());
    }
}

  /*
   * Get Preferences from the database
   */
  public String getPreference(String username, String key) {
    String sql = "SELECT value FROM preferences WHERE username = ? AND key = ?";
    try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
        pstmt.setString(1, username);
        pstmt.setString(2, key);
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
    String sql = "INSERT INTO logs(location, date, notes, temperature) VALUES (?, ?, ?, ?)";

    try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(obs.getTimestamp());

            pstmt.setString(1, obs.getLocation());
            pstmt.setString(2, formattedDate);
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
    public Observation searchLogs(String location){
        String sql = "SELECT * FROM logs WHERE location = ?";
        try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, location);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                try{
                  String dateString = rs.getString("date");
                  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                  Date date = dateFormat.parse(dateString);
                  return new Observation(rs.getString("location"), date, rs.getString("notes"), rs.getDouble("temperature"));
                }
                catch (Exception e){
                  System.out.println("Error parsing date: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching logs: " + e.getMessage());
        }
      return null;
    }

     /*
      * Method to export to a CSV file
      */
      public void exportToCSV(){
        String sql = "SELECT * FROM logs";
        try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql); FileWriter csvWriter = new FileWriter("logs.csv");) {
            ResultSet rs = pstmt.executeQuery();
            
          int columnCount = rs.getMetaData().getColumnCount();
          for(int i = 1; i <= columnCount; i++){
            csvWriter.append(rs.getMetaData().getColumnName(i));
            if(i < columnCount){
              csvWriter.append(",");
            }
          }
          csvWriter.append("\n");

          while(rs.next()){
            for(int i = 1; i <= columnCount; i++){
              csvWriter.append(rs.getString(i));
              if(i < columnCount){
                csvWriter.append(",");
              }
            }
            csvWriter.append("\n");
          }
        } catch (SQLException e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
      }

      @Override
      public void saveUser(User user) {
        String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
      }
    
      public Map<String, Integer> getObservationCountsByLocation(){
        String sql = "SELECT location, COUNT(*) as count FROM logs GROUP BY location";
        Map<String, Integer> observationCounts = new HashMap<>();
        try (PreparedStatement pstmt = DatabaseHelper.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String location = rs.getString("location");
                int count = rs.getInt("count");
                observationCounts.put(location, count);
            }
        } catch (SQLException e) {
            System.out.println("Error getting observation counts: " + e.getMessage());
        }
        return observationCounts;
      }
}



