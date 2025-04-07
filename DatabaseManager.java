import java.sql.*;

public class DatabaseManager{
  //uses SQLite operations for storing logs and preferences 
  

  /*
   * Save Preferences to the database
   * Also need a Preference class for this
   */
  public static void savePreference(String key, String value) {
    String sql = "REPLACE INTO preferences() VALUES()";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {


        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Save preference error: " + e.getMessage());
    }
}

  /*
   * Get Preferences from the database
   */
  public static String getPreference(String key) {
    String sql = "SELECT value FROM preferences WHERE key = ?";
    try (Connection conn = DatabaseHelper.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

}
