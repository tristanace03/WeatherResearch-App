import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Date;

public class WeatherController{
  private DatabaseManager databaseManager;
  private UserManager userManager;
  private ESPNScores espnScores;
  private WeatherDataSource weatherSource;
  private WeatherDataFactory weatherDataFactory;

  public WeatherController(DatabaseManager dm, UserManager um, ESPNScores espn, WeatherDataSource weatherSource){
    this.databaseManager = dm;
    this.userManager = um;
    this.espnScores = espn;
    this.weatherSource = weatherSource;
    this.weatherDataFactory = new WeatherDataFactory();  
  }

  public String getWeatherByLocation(Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    return weatherService.getWeatherByLocation(location);
}

public String getAlerts(Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    return weatherService.getSevereWeatherAlerts(location);
}

public String getMultidayForecast(Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    return weatherService.getMultidayForecast(location);
}

  // public addObservation{
  // }

  // public getObservation{
  // }

  public void saveObservation(Location location){
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    weatherService.saveObservation(location);
  }

  // public queryObservations{
  // }

  // public saveUserPreferences{
  // }

  // public getUserPreferences{
  // }

  public void shareWeatherData(String to, Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    weatherService.shareWeatherData(to, location);
  }

  public void exportLogbookData(){
    SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
    dbFactory.createDatabaseManager("SQLLite").exportToCSV();
    System.out.println("Logbook data exported to CSV file.");
  }

  public void getESPNScores(){

    Scanner scan = new Scanner(System.in);
    int choice = 0;

    while(choice < 1 || choice > 2){
      System.out.print("Please choose 1 for baseball or 2 for NBA: ");
      if(scan.hasNextInt()){
        choice = scan.nextInt();
        if(choice >=1 && choice <= 2){
          break;
        }
        else{
          System.out.println("Invalid Input");
        }
      }
      else{
        String invalid = scan.next();
        System.out.println(invalid + " is not a valid input");
      }
    }
    
    if(choice == 1){
      System.out.println("Here are the current MLB Scores for Today:");
      espnScores.getScores("baseball", "mlb");
    }
    else if(choice == 2){
      System.out.println("Here are the current NBA Scores for Today:");
      espnScores.getScores("basketball", "nba");
    }
    



  }

}

