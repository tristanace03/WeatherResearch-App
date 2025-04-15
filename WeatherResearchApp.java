import java.util.Date;

public class WeatherResearchApp{
  //main class for application
  public static void main(String[] args){
   SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
   AbstractDatabaseManager db = dbFactory.createDatabaseManager("SQLLite");
   db.logObservation(new Observation("Chicago", new Date(), "hot out", 100));
  }
}
