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
  //main class for application
  private static final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/search";
    private static final String USER_AGENT = "WeatherResearchApp/1.0 (tristanace2@gmail.com)";
  public static void main(String[] args){

    run();
  }

  /*
   * Method to run the Weather App
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
      "1: View the weather by location\n" +
      "2: View Radar Visualization\n" +
      "3: Log Weather Observations\n" +
      "4: View Heat Map based on Logbook Locations\n" +
      "5: Set a favorite Location\n" +
      "6: View Recent Weather alerts\n" +
      "7: Set weather preferences\n" +
      "8: Export Logbook Information\n" +
      "9: Search for Historical Logbook Entries\n" +
      "10: View Multiday forecast\n" +
      "11: Share Weather Data\n" +
      "12: View The Current news based on a location\n" +
      "13: Manage Weather Preferences\n" +
      "14: Suggest Activites based on the weather\n" +
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

      else if(choice ==2){
        // Call for viewing Radar Visualization
        RadarLinkGenerator.main(new String[] {});
  
      }


      else if (choice == 3) {
        // Call to log weather observations
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


      else if(choice == 4){
        // Call to view Heat Map of Logbook Locations
        controller.getHeatMap();
      }


      else if(choice == 5){
        // Call to set a Favorite Location
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


      else if(choice == 8){
        // Call to export Logbook Data
        System.out.println("Printing logbook data to CSV file...");
        controller.exportLogbookData();
      }


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


      else if(choice == 11){
        // Call to share weather data
        System.out.print("Please enter the email address to share the weather data: ");
        String email = scan.nextLine();
        System.out.print("Enter a ZIP code of location to share: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        controller.shareWeatherData(email, location);
      }


      else if(choice == 12){
        // Call to see the current news
        System.out.print("Enter a ZIP code: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
            controller.getNewsByLocation(location);
        }
      }


      else if (choice == 13) {
        // Manage user preferences
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

      else if(choice == 14){
        // Call to suggest activites based on the weather
        System.out.print("Enter a ZIP code: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
        }
        System.out.println(controller.getSuggestedActivity(location));  
      }


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
      else if(choice == 16){
        controller.getESPNScores();
      }
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
