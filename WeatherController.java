import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Date;

public class WeatherController{
  // private LogbookManager logbookManager;
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
    StringBuilder output = new StringBuilder();
    WeatherDataSource.PointData pointData = weatherSource.getPointData(location);
    if (pointData == null) {
        return "Unable to fetch weather data for this location.";
    }

    output.append("\n=== Daily Forecast ===\n");
    JSONArray dailyForecast = weatherSource.getDailyForecast(pointData.forecastUrl);
    if (dailyForecast != null) {
        for (int i = 0; i < Math.min(dailyForecast.length(), 7); i++) {
            JSONObject period = dailyForecast.getJSONObject(i);
            WeatherData data = weatherDataFactory.createWeatherData(period, "daily");
            output.append(data.getTime()).append(": ")
                  .append(data.getDescription()).append("\n");
        }
    } else {
        output.append("No daily forecast available.\n");
    }

    output.append("\n=== Hourly Forecast ===\n");
    JSONArray hourlyForecast = weatherSource.getHourlyForecast(pointData.hourlyForecastUrl);
    if (hourlyForecast != null) {
        for (int i = 0; i < Math.min(hourlyForecast.length(), 12); i++) {
            JSONObject period = hourlyForecast.getJSONObject(i);
            WeatherData data = weatherDataFactory.createWeatherData(period, "hourly");
            output.append(data.getTime()).append(": ")
                  .append(data.getTemperature()).append("°F, ")
                  .append(data.getDescription()).append(", ")
                  .append(data.getPrecipitationChance()).append("% chance of rain\n");
        }
    } else {
        output.append("No hourly forecast available.\n");
    }

    if (!pointData.marineZone.isEmpty()) {
        output.append("\n=== Marine Forecast ===\n");
        JSONArray marineForecast = weatherSource.getMarineForecast(pointData.marineZone);
        if (marineForecast != null) {
            for (int i = 0; i < Math.min(marineForecast.length(), 3); i++) {
                JSONObject period = marineForecast.getJSONObject(i);
                WeatherData data = weatherDataFactory.createWeatherData(period, "marine");
                output.append(data.getTime()).append(": ")
                      .append(data.getDescription()).append("\n");
            }
        } else {
            output.append("No marine forecast available.\n");
        }
    }

    return output.toString();
}

public String getAlerts(Location location) {
    StringBuilder output = new StringBuilder();
    WeatherDataSource.PointData pointData = weatherSource.getPointData(location);
    if (pointData == null) {
        return "Unable to fetch alerts for this location.";
    }

    output.append("\n=== Severe Weather Alerts ===\n");
    JSONArray alerts = weatherSource.getAlerts(pointData.alertsUrl);
    if (alerts != null && alerts.length() > 0) {
        for (int i = 0; i < alerts.length(); i++) {
            JSONObject alert = alerts.getJSONObject(i).getJSONObject("properties");
            output.append("⚠️ ").append(alert.getString("event"))
                  .append(" (").append(alert.getString("severity")).append(")\n")
                  .append("Headline: ").append(alert.getString("headline")).append("\n")
                  .append("Description: ").append(alert.getString("description")).append("\n")
                  .append("Instructions: ").append(alert.optString("instruction", "No instructions provided")).append("\n")
                  .append("Effective: ").append(alert.getString("effective")).append("\n")
                  .append("Expires: ").append(alert.getString("expires")).append("\n")
                  .append("---\n");
        }
    } else {
        output.append("No current severe weather alerts.\n");
    }

    output.append("\n=== Fire Weather Warnings ===\n");
    JSONArray fireWeather = weatherSource.getFireWeather(pointData.fireWeatherUrl);
    if (fireWeather != null && fireWeather.length() > 0) {
        for (int i = 0; i < fireWeather.length(); i++) {
            JSONObject alert = fireWeather.getJSONObject(i).getJSONObject("properties");
            output.append("🔥 ").append(alert.getString("event")).append("\n")
                  .append("Headline: ").append(alert.getString("headline")).append("\n")
                  .append("Description: ").append(alert.getString("description")).append("\n")
                  .append("Effective: ").append(alert.getString("effective")).append("\n")
                  .append("Expires: ").append(alert.getString("expires")).append("\n")
                  .append("---\n");
        }
    } else {
        output.append("No current fire weather warnings.\n");
    }

    return output.toString();
}

public String getMultidayForecast(Location location) {
    StringBuilder output = new StringBuilder();
    WeatherDataSource.PointData pointData = weatherSource.getPointData(location);
    if (pointData == null) {
        return "Unable to fetch multiday forecast for this location.";
    }

    output.append("\n=== Multiday Forecast ===\n");
    JSONArray dailyForecast = weatherSource.getDailyForecast(pointData.forecastUrl);
    if (dailyForecast != null) {
        for (int i = 0; i < Math.min(dailyForecast.length(), 7); i++) {
            JSONObject period = dailyForecast.getJSONObject(i);
            WeatherData data = weatherDataFactory.createWeatherData(period, "daily");
            output.append(data.getTime()).append(": ")
                  .append(data.getDescription()).append("\n");
        }
    } else {
        output.append("No multiday forecast available.\n");
    }

    return output.toString();
}

  // public addObservation{
  // }

  // public getObservation{
  // }

  // public saveObservation{
  // }

  // public queryObservations{
  // }

  // public saveUserPreferences{
  // }

  // public getUserPreferences{
  // }

  public void shareWeatherData(String to){
    WeatherService weatherService = new WeatherService();
    weatherService.shareWeatherData(to);
  }

  public void exportLogbookData(){
    SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
    dbFactory.createDatabaseManager("SQLLite").exportToCSV();
    System.out.println("Logbook data exported to CSV file.");
  }

  public void getWeatherData(String location){
    WeatherService weatherService = new WeatherService();
    weatherService.getRealTimeWeather(location);
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

