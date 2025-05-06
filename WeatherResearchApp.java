/**
 * Main entry point for the Weather Research Application
 * 
 * Provides a user interface for logging in, managing accounts and interracting with weather data
 * Supports weather forecasts, alerts, activity suggestions, and logbook management
 * Integrates with various modules such as weather services, user management and geocoding
 * 
 * This class initializes the applicaiton and cooridnates user interractions with the system
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle.Control;
import org.json.*;

public class WeatherResearchApp{
  private static final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/search";
  private static final String USER_AGENT = "WeatherResearchApp/1.0 (tristanace2@gmail.com)";

  /**
   * The main method to launch the applicaiton
   * 
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args){
    run();
  }

  /**
   * Runs the Weather Research Application
   * 
   * Handles user authentication, menu navigation and interaction with weather services
   */
  public static void run(){

    SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
    DatabaseManager dbManager = (DatabaseManager) dbFactory.createDatabaseManager("SQLite");
    UserManager userManager = new UserManager();
    ESPNScores espnScores = new ESPNScores();
    SuggestActivities activities = new SuggestActivities();
    WeatherDataSource weatherSource = WeatherDataSourceFactory.createWeatherDataSource("NOAA");
    WeatherController controller = new WeatherController(dbManager, userManager, espnScores, weatherSource, activities);
    Scanner scan = new Scanner(System.in);
   
    boolean loggedIn = false;

    while (!loggedIn) {
      System.out.println("Welcome to Weather Research App!");
      System.out.println("1: Log In");
      System.out.println("2: Create an Account");
      System.out.print("Choose an option: ");

      if (scan.hasNextInt()) {
        int choice = scan.nextInt();
        scan.nextLine(); 
        switch (choice) {
          case 1: // Login
            System.out.print("Enter username: ");
            String username = scan.nextLine();
            System.out.print("Enter password: ");
            String password = scan.nextLine();
            if (userManager.login(username, password)) {
              userManager.setLoggedInUser(username);
              loggedIn = true;
              System.out.println("Login successful!");
            } else {
              System.out.println("Invalid credentials. Please try again.");
            }
          break;

          case 2: // Create account
            System.out.print("Choose a username: ");
            String newUsername = scan.nextLine();
            System.out.print("Choose a password: ");
            String newPassword = scan.nextLine();
            userManager.createUser(newUsername, newPassword);
            System.out.println("Account created successfully! Now, set your forecast preferences.");

            // Prompt user for multiday forecast preference
            System.out.print("Enter the number of instances you'd like to see for multiday forecasts (ex. Monday, Monday Night, Tuesday...)(Max: 14 days): ");
            int multidayDays = scan.nextInt();
            scan.nextLine(); 

            // Prompt user for hourly forecast preference
            System.out.print("Enter the number of hours for hourly forecasts (ex. 21:00, 22:00, 23:00...)(Max: 120 hours): ");
            int hourlyHours = scan.nextInt();
            scan.nextLine(); 

            // Save user preferences
            userManager.savePreferences(newUsername, "multiday_forecast_days", String.valueOf(multidayDays));
            userManager.savePreferences(newUsername, "hourly_forecast_hours", String.valueOf(hourlyHours));

            System.out.println("Preferences saved successfully! Please log in.");
            break;

          default:
            System.out.println("Invalid option. Please choose 1 or 2.");
        }
      } else {
          String invalidInput = scan.next(); 
          System.out.println("\"" + invalidInput + "\" is not a valid option. Please enter 1 or 2.");
      }
    }

    boolean running = true;
    int choice = -1;

    System.out.println("Welcome to our Weather App!");

    while(running!=false){
      System.out.println();
      System.out.println("Options:\n" +
      "0: Exit\n" + 
      "1: View the Weather by Location\n" +
      "2: View Radar Visualization\n" +
      "3: Log Weather Observations\n" +
      "4: View Heat Map Based on Logbook Locations\n" +
      "5: Set a Favorite Location\n" +
      "6: View Recent Weather Alerts\n" +
      "7: Set Weather Preferences\n" +
      "8: Export Logbook Information\n" +
      "9: Search for Historical Logbook Entries\n" +
      "10: View Multiday Forecast\n" +
      "11: Share Weather Data\n" +
      "12: View The Current News Based on a Location\n" +
      "13: Manage Weather Preferences\n" +
      "14: Suggest Activities Based on the Weather\n" +
      "15: Unfavorite a Location\n" +
      "16: View ESPN Scores\n" +
      "17: View Hourly Forecast\n" +
      "18: Compare the Weather of Two Locations");

      while(choice < 0 || choice > 20){
        System.out.print("Please enter a number between 0 and 20: ");
        if(scan.hasNextInt()){
          choice = scan.nextInt();
          if(choice >=0 && choice <= 20){
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
      scan.nextLine(); 
      if(choice ==0){
        // Exit
        running = false;
      }

      //1: VIEW WEATHER BY LOCATION
      else if(choice == 1) {
        String zip = null;
        String username = userManager.getCurrentUsername();

        // Use favorite ZIP if set
        if (userManager.isLoggedIn()) {
          String favoriteZip = userManager.getFavoriteLocation(username);
          if (favoriteZip != null) {
              zip = favoriteZip;
              System.out.println("Using your favorite location ZIP: " + zip);
          }
        }

        if (zip == null) {
          // Check if search history is enabled
          String toggle = controller.getPreferences(username, "SearchHistoryEnabled");
          boolean historyEnabled = "true".equalsIgnoreCase(toggle);

          List<String> recentZips = new ArrayList<>();

          if (historyEnabled) {
            String history = controller.getPreferences(username, "searchHistory");
            if (history != null && !history.isEmpty()) {
              recentZips = new ArrayList<>(Arrays.asList(history.split(",")));
              System.out.println("Recent ZIPs: " + String.join(", ", recentZips));
            }
          }

          System.out.print("Enter a ZIP code: ");
          zip = scan.nextLine().trim();

          // Save search history only if enabled
          if (historyEnabled) {
            recentZips.remove(zip);
            recentZips.add(0, zip);
            if (recentZips.size() > 5) {
              recentZips = recentZips.subList(0, 5);
            }
            String updatedHistory = String.join(",", recentZips);
            controller.savePreferences(username, "searchHistory", updatedHistory);
          }
        }

        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
          System.out.println("\nLocation: " + location);
          String weatherInfo = controller.getWeatherByLocation(location);
          System.out.println(weatherInfo);
        }
      }
      
      //2: VIEW RADAR VISUALIZATION
      else if(choice ==2){
        RadarLinkGenerator.main(new String[] {});
      }

      //3: LOG WEATHER OBSERVATIONS
      else if (choice == 3) {
        System.out.print("Enter a ZIP code: ");
        String zip = scan.nextLine().trim();
        // Get the location based on the ZIP code
        Location location = getCoordinatesFromZIP(zip);
    
        // Prompt the user for optional notes
        System.out.print("Enter notes about this observation (optional): ");
        String notes = scan.nextLine();
    
        // Save the observation with location and notes
        controller.saveObservation(location, notes);
      }

      //4: VIEW HEAT MAP BASED ON LOGBOOK LOCATIONS
      else if(choice == 4){
        controller.getHeatMap();
      }

      //5: SET A FAVORITE LOCATION
      else if(choice == 5){
        System.out.print("Enter your username: ");
        String username = scan.nextLine().trim();
        System.out.print("Enter the ZIP code of your favorite location: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
          System.out.println("\nLocation: " + location);
          controller.createFavoriteLocation(username, zip);
        }
      }

      //6: VIEW RECENT WEATHER ALERTS
      else if(choice == 6) {
        String zip = null;
        if (userManager.isLoggedIn()) {
            String favoriteZip = userManager.getFavoriteLocation(userManager.getCurrentUsername());
            if (favoriteZip != null) {
                zip = favoriteZip;
                System.out.println("Using your favorite location ZIP: " + zip);
            }
        }
        if (zip == null) { // Prompt if no favorite location is set
            System.out.print("Enter a ZIP code: ");
            zip = scan.nextLine().trim();
        }
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
            String alertsInfo = controller.getAlerts(location);
            System.out.println(alertsInfo);
        }
      } 

      //7: SET WEATHER PREFERENCES
      else if(choice == 7){
        // Call to set weather preferences
        System.out.print("Do you want to have a search history? (yes/no): ");
        String input = scan.nextLine().trim().toLowerCase();

        String value;
        if (input.equals("yes")) {
            value = "true";
        } else if (input.equals("no")) {
            value = "false";
        } else {
            System.out.println("Invalid input. Please type 'yes' or 'no'.");
            return;
        }

        controller.savePreferences(userManager.getCurrentUsername(), "SearchHistoryEnabled", value);
      }

      //8: EXPORT LOGBOOK INFORMATION
      else if(choice == 8){
        System.out.println("Printing logbook data to CSV file...");
        controller.exportLogbookData();
      }

      //9: SEARCH FOR HISTORICAL LOGBOOK ENTRIES
      else if(choice == 9){
        // Call to search for Historical logbook Entries
        System.out.print("Enter a ZIP code to recieve logs of: ");
        String zip = scan.nextLine().trim();  
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
            controller.getObservation(location);
        }
      }

      //10: VIEW MULTIDAY FORECAST
      else if (choice == 10) {
        String zip = null;
        if (userManager.isLoggedIn()) {
            String favoriteZip = userManager.getFavoriteLocation(userManager.getCurrentUsername());
            if (favoriteZip != null) {
                zip = favoriteZip;
                System.out.println("Using your favorite location ZIP: " + zip);
            }
        }
        if (zip == null) { // Prompt if no favorite location is set
            System.out.print("Enter a ZIP code: ");
            zip = scan.nextLine().trim();
        }
    
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
    
            // Get the number of days for the forecast from user preferences
            int days = Integer.parseInt(userManager.getPreferences(userManager.getCurrentUsername(), "multiday_forecast_days"));
            String forecastInfo = controller.getMultidayForecast(location, days);
            System.out.println(forecastInfo);
        }
      }

      //11: SHARE WEATHER DATA
      else if(choice == 11){
        // Call to share weather data
        System.out.print("Please enter the email address to share the weather data: ");
        String email = scan.nextLine();
        System.out.print("Enter a ZIP code of location to share: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        controller.shareWeatherData(email, location);
      }

      //12: VIEW THE CURRENT NEWS BASED ON A LOCATION
      else if(choice == 12){
        System.out.print("Enter a ZIP code: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
            controller.getNewsByLocation(location);
        }
      }

      //13: MANAGE WEATHER PREFERENCES
      else if (choice == 13) {
        System.out.print("Enter your username: ");
        String username = scan.nextLine().trim();
    
        if (!userManager.isLoggedIn() || !userManager.getCurrentUsername().equals(username)) {
            System.out.println("You are not logged in as this user. Please log in first.");
            continue;
        }
    
        System.out.println("Manage Preferences:");
        System.out.println("1: Change the number of days for multiday forecasts");
        System.out.println("2: Change the number of hours for hourly forecasts");
        System.out.print("Choose an option (1 or 2): ");
    
        if (scan.hasNextInt()) {
            int preferenceChoice = scan.nextInt();
            scan.nextLine(); // Consume newline
    
            switch (preferenceChoice) {
                case 1: // Change multiday forecast days
                    System.out.print("Enter the new number of days for multiday forecasts (e.g., 3): ");
                    int newDays = scan.nextInt();
                    scan.nextLine(); // Consume newline
                    userManager.savePreferences(username, "multiday_forecast_days", String.valueOf(newDays));
                    System.out.println("Multiday forecast preference updated successfully!");
                    break;
    
                case 2: // Change hourly forecast hours
                    System.out.print("Enter the new number of hours for hourly forecasts (e.g., 12): ");
                    int newHours = scan.nextInt();
                    scan.nextLine(); // Consume newline
                    userManager.savePreferences(username, "hourly_forecast_hours", String.valueOf(newHours));
                    System.out.println("Hourly forecast preference updated successfully!");
                    break;
    
                default:
                    System.out.println("Invalid choice. Please choose 1 or 2.");
            }
        } else {
            String invalidInput = scan.next(); // Consume invalid input
            System.out.println("\"" + invalidInput + "\" is not a valid option. Please enter 1 or 2.");
        }
      }

      //14: SUGGEST ACTIVITIES BASED ON THE WEATHER
      else if(choice == 14){
        System.out.print("Enter a ZIP code: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
        }
        System.out.println(controller.getSuggestedActivity(location));  
      }

      //15: UNFAVORITE A LOCATION
      else if(choice == 15){
        // Call to unfavorite a location
        System.out.print("Enter your username: ");
        String username = scan.nextLine().trim();
        System.out.print("Enter the ZIP code of the location to unfavorite: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
            controller.removeFavoriteLocation(username, zip);
        }
      }

      //16: VIEW ESPN SCORES
      else if(choice == 16){
        controller.getESPNScores();
      }

      //17: VIEW HOURLY FORECAST
      else if (choice == 17) {
        String zip = null;
        if (userManager.isLoggedIn()) {
            String favoriteZip = userManager.getFavoriteLocation(userManager.getCurrentUsername());
            if (favoriteZip != null) {
                zip = favoriteZip;
                System.out.println("Using your favorite location ZIP: " + zip);
            }
        }
        if (zip == null) { // Prompt if no favorite location is set
            System.out.print("Enter a ZIP code: ");
            zip = scan.nextLine().trim();
        }
    
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
    
            // Get the number of hours for the forecast from user preferences
            int hours = Integer.parseInt(userManager.getPreferences(userManager.getCurrentUsername(), "hourly_forecast_hours"));
            String forecastInfo = controller.getHourlyForecast(location, hours);
            System.out.println(forecastInfo);
        }
      }

      //18: COMPARE THE WEATHER OF TWO LOCATIONS
      else if(choice == 18){
        System.out.print("Enter a ZIP code for location 1: ");
        String zip1 = scan.nextLine().trim();
        Location location1 = getCoordinatesFromZIP(zip1);
    
        System.out.print("Enter a ZIP code for location 2: ");
        String zip2 = scan.nextLine().trim();
        Location location2 = getCoordinatesFromZIP(zip2);

        System.out.println("Location 1: \n");
        if (location1 != null) {
          System.out.println("\nLocation: " + location1);
          String weatherInfo = controller.getWeatherByLocation(location1);
          System.out.println(weatherInfo);
        }

        System.out.println("Location 2: \n");
        if (location2 != null) {
          System.out.println("\nLocation: " + location2);
          String weatherInfo = controller.getWeatherByLocation(location2);
          System.out.println(weatherInfo);
        }
      }

      choice = -1;
      System.out.println();

    }
    scan.close();
    System.out.println("Thank you!");
  }


  /**
   * Retrieves geographic cooridnates and location details for a given ZIP code
   * 
   * @param zip the ZIP code to geocode
   * @return a LOcation object containing latitude, longitude, state and town information, or null if ZIP is invalid
   */
  private static Location getCoordinatesFromZIP(String zip) {
    try {
        String url = NOMINATIM_BASE_URL + "?postalcode=" + URLEncoder.encode(zip, "UTF-8") + "&country=USA&format=json&addressdetails=1";
        String response = getString(url);
        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() == 0) {
            System.out.println("Invalid ZIP code or no data found.");
            return null;
        }

        JSONObject result = jsonArray.getJSONObject(0);
        double lat = result.getDouble("lat");
        double lon = result.getDouble("lon");
        JSONObject address = result.getJSONObject("address");
        String state = address.optString("state", "Unknown");

        String town = address.optString("town", "");
        if (town.isEmpty()) {
            town = address.optString("city", "");
        }
        if (town.isEmpty()) {
            town = address.optString("village", "");
        }
        if (town.isEmpty()) {
            town = address.optString("hamlet", "");
        }

        return new Location(lat, lon, state, town);
    } catch (Exception e) {
        System.err.println("Error geocoding ZIP: " + e.getMessage());
        return null;
    }
  }

  /**
  * Fetches the response as a string from a given URL
  * 
  * @param urlStr the URL to fetch fata from
  * @return the response as a string
  */
  private static String getString(String urlStr) throws Exception {
    HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
    conn.setRequestProperty("User-Agent", USER_AGENT);

    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        return sb.toString();
    }
  }

}
