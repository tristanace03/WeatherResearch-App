/**
 * Acts as the central controller for managing interractions between the application and various app components
 * 
 * Manages weather related services, user preferences and activity suggestions.
 * Handles interactions with data sources, including weather, sports and news APIs
 * Provides utility methods for saving observations, exporting data and sharing weather information
 * 
 * This class serves as the main interface for handling core application logic
 */
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

  /**
   * Contructs a new WeatherController with the provided components
   * 
   * @param dm the databse manager for handling storage operations
   * @param um the user manager for managing user accounts and preferences
   * @param espn the ESPN scores manager for retrieving sports scores
   * @param weatherSource the weather data source for fetching weather information
   * @param weatherActivities the activity suggestions service
   */
  public WeatherController(DatabaseManager dm, UserManager um, ESPNScores espn, WeatherDataSource weatherSource, SuggestActivities weatherActivities){
    this.databaseManager = dm;
    this.userManager = um;
    this.espnScores = espn;
    this.weatherSource = weatherSource;
    this.weatherDataFactory = new WeatherDataFactory();
    this.activities = weatherActivities;

  }

  /**
   * Retrieves the current weather for a specified location
   * 
   * @param location the location for which to retrieve the weather
   * @return a string containing the weather information
   */
  public String getWeatherByLocation(Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    return weatherService.getWeatherByLocation(location);
  }

  /**
   * Retrieves severe weather alerts for a specifed location 
   * 
   * @param location the location for which to retrieve alerts
   * @return a string containing the weather alers
   */
  public String getAlerts(Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    return weatherService.getSevereWeatherAlerts(location);
  }

  /**
   * Retrieves and prints an observation for a specified location
   * 
   * @param location the location for which to retrieve the observation
   */
  public void getObservation(Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    Observation observation = weatherService.getObservation(location);
    System.out.println(observation);
  }

  /**
   * Saves an observation for a specified location with additional notes
   * 
   * @param location the location for which to save the observation
   * @param notes additional notes about the observation
   */
  public void saveObservation(Location location, String notes){
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    weatherService.saveObservation(location, notes);
  }

  /**
   * Saves a user preference
   * 
   * @param username the username of the user
   * @param key the preference key
   * @param value the preference value
   */
  public void savePreferences(String username, String key, String value){
    userManager.savePreferences(username, key, value);
    System.out.println("Preferences saved for " + key + " with value " + value);
  }

  /**
   * Retrieves a user preference
   * 
   * @param username the username of the user
   * @param key the preference key
   * @return the value of the preference
   */
  public String getPreferences(String username, String key) {
    return userManager.getPreferences(username, key);
  }

  /**
   * Shares weather data for a specified location with another user
   * 
   * @param to the recipient of the shared data
   * @param location the location for which to share the weather data
   */
  public void shareWeatherData(String to, Location location) {
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    weatherService.shareWeatherData(to, location);
  }

  /**
   * Exports logbook data to a CSV file
   */
  public void exportLogbookData(){
    SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
    dbFactory.createDatabaseManager("SQLLite").exportToCSV();
    System.out.println("Logbook data exported to CSV file.");
  }

  /**
   * Suggests an activity based on the weather forecast for a specified location
   * 
   * @param location the location for which to suggest an activity
   * @return a string containing the suggested activity
   */
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

  /**
   * Prompts the user to select a sports category and retrieves ESPN scores
   */
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

  /**
   * Creates a new user account
   * 
   * @param username the username of the new user
   * @param password the password of the new user
   */
  public void createUser(String username, String password) {
    userManager.createUser(username,password);
  }

  /**
   * Logs in a user with the provided credientals
   * 
   * @param username the username of the user 
   * @param password the password of the user
   */
  public void loginUser(String username, String password) {
    if (userManager.login(username, password)) {
        userManager.setLoggedInUser(username); // Set the logged-in user
        System.out.println("Login successful!");
    } else {
        System.out.println("Login failed. Please check your username and password.");
    }
  }
  
  /**
   * Adds a favorite location for a user
   * 
   * @param username the username of the user
   * @param location the location to add as a favorite
   */
  public void createFavoriteLocation(String username, String location) {
    if(userManager.addFavoriteLocation(username, location)){
      System.out.println("Favorite location added successfully!");
    }
    else{
      System.out.println("Failed to add favorite location. Please try again.");
    }
  }

  /**
   * Removes a favorite location for a user
   * 
   * @param username the username of the user
   * @param location the location to remove from favorites
   */
  public void removeFavoriteLocation(String username, String location) {
    if(userManager.removeFavoriteLocation(username, location)){
      System.out.println("Favorite location removed successfully!");
    }
    else{
      System.out.println("Failed to remove favorite location. Please try again.");
    }
  }

  /**
   * Fetches news articles related to a specified location
   * 
   * @param location the location for which to fetch news
   */
  public void getNewsByLocation(Location location) {
    NewsService newsService = new NewsService();
    newsService.getNewsByLocation(location);
  }

  /**
   * Generates a "heat map"
   */
  public void getHeatMap(){
    WeatherService weatherService = new WeatherService(weatherSource, weatherDataFactory);
    weatherService.getHeatMap();
  }

  /**
   * Retrieves the multiday weather forecast for a specified location
   * 
   * @param location the location for which to retrieve the forecast
   * @param days the number for days to include in the forecast
   * @return a string containing the multiday forecast
   */
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

  /**
   * Retrieves the hourly weather forecast for a specified location
   * 
   * @param location the location for which to retrieve the forecast
   * @param hours the number of hours to include in the forecast
   * @return a string containing the hourly forecast
   */
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

