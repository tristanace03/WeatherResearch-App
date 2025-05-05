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
  private SuggestActivities activities;

  public WeatherController(DatabaseManager dm, UserManager um, ESPNScores espn, WeatherDataSource weatherSource, SuggestActivities weatherActivities){
    this.databaseManager = dm;
    this.userManager = um;
    this.espnScores = espn;
    this.weatherSource = weatherSource;
    this.weatherDataFactory = new WeatherDataFactory();
    this.activities = weatherActivities;

  }

  public String getWeatherByLocation(Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    return weatherService.getWeatherByLocation(location);
}

public String getAlerts(Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    return weatherService.getSevereWeatherAlerts(location);
}


  // public addObservation{
  // }

  public void getObservation(Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    Observation observation = weatherService.getObservation(location);
    System.out.println(observation);
  }

  public void saveObservation(Location location){
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    weatherService.saveObservation(location);
  }

  public void savePreferences(String username, String key, String value){
    userManager.savePreferences(username, key, value);
    System.out.println("Preferences saved for " + key + " with value " + value);
  }

  public void getPreferences(String username, String key){
    String value = userManager.getPreferences(username, key);
    if(value != null){
      System.out.println("Preferences for " + key + " is " + value);
    }
    else{
      System.out.println("No preferences found for " + key);
    }
  }

  public void shareWeatherData(String to, Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    weatherService.shareWeatherData(to, location);
  }

  public void exportLogbookData(){
    SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
    dbFactory.createDatabaseManager("SQLLite").exportToCSV();
    System.out.println("Logbook data exported to CSV file.");
  }

  public String getSuggestedActivity(Location location) {
    String weatherInfo = getWeatherByLocation(location);
    String[] parts = weatherInfo.split("=== Hourly Forecast ===");
    if (parts.length > 1) {
        String hourlyForecast = parts[1].trim();

        String[] lines = hourlyForecast.split("\n");

        if (lines.length > 0) {
            System.out.println(lines[0]+"\n");
        } 

        return SuggestActivities.suggestActivityFromForecast(hourlyForecast);
    } else {
        return "Weather data unavailable.";
    }
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

  public void createUser(String username, String password) {
    userManager.createUser(username,password);
  }

  public void loginUser(String username, String password) {
    if (userManager.login(username, password)) {
        userManager.setLoggedInUser(username); // Set the logged-in user
        System.out.println("Login successful!");
    } else {
        System.out.println("Login failed. Please check your username and password.");
    }
}
  
  public void createFavoriteLocation(String username, String location) {
    if(userManager.addFavoriteLocation(username, location)){
      System.out.println("Favorite location added successfully!");
    }
    else{
      System.out.println("Failed to add favorite location. Please try again.");
    }
  }

  public void removeFavoriteLocation(String username, String location) {
    if(userManager.removeFavoriteLocation(username, location)){
      System.out.println("Favorite location removed successfully!");
    }
    else{
      System.out.println("Failed to remove favorite location. Please try again.");
    }
  }

  public void getNewsByLocation(Location location) {
    NewsService newsService = new NewsService();
    newsService.getNewsByLocation(location);
  }

  public void getHeatMap(){
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    weatherService.getHeatMap();
  }
  public String getMultidayForecast(Location location, int days) {
    StringBuilder output = new StringBuilder();
    WeatherDataSource.PointData pointData = weatherSource.getPointData(location);

    if (pointData == null) {
        return "Unable to fetch multiday forecast for this location.";
    }

    output.append("\n=== Multiday Forecast ===\n");
    JSONArray dailyForecast = weatherSource.getDailyForecast(pointData.forecastUrl);

    if (dailyForecast != null) {
        for (int i = 0; i < Math.min(dailyForecast.length(), days); i++) {
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

public String getHourlyForecast(Location location, int hours) {
    StringBuilder output = new StringBuilder();
    WeatherDataSource.PointData pointData = weatherSource.getPointData(location);

    if (pointData == null) {
        return "Unable to fetch hourly forecast for this location.";
    }

    output.append("\n=== Hourly Forecast ===\n");
    JSONArray hourlyForecast = weatherSource.getHourlyForecast(pointData.hourlyForecastUrl);

    if (hourlyForecast != null) {
        for (int i = 0; i < Math.min(hourlyForecast.length(), hours); i++) {
            JSONObject period = hourlyForecast.getJSONObject(i);
            WeatherData data = weatherDataFactory.createWeatherData(period, "hourly");
            output.append(data.getTime()).append(": ")
                  .append(data.getTemperature()).append("°F, ")
                  .append(data.getDescription()).append("\n");
        }
    } else {
        output.append("No hourly forecast available.\n");
    }

    return output.toString();
}
}

