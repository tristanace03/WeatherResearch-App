import java.sql.*;
import java.util.*;

public class LogbookManager{
  //handles logging, searching and exporting weather observations. Interracts with SQLite

  // Need an observation class for this
  public static boolean logObservation(Observation obs){
    String sql =  "INSERT INTO logbook () VALUES ()";

    try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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


     /*
      * Method to export to a CSV or a PDF
      */

}


