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

    //WeatherController controller = new WeatherController(new DatabaseManager(), new UserManager(), new ESPNScores());
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

    // Validate input to ensure it's an integer
    if (scan.hasNextInt()) {
        int choice = scan.nextInt();
        scan.nextLine(); // Consume newline

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
                System.out.print("Enter the number of days for multiday forecasts (e.g., 3): ");
                int multidayDays = scan.nextInt();
                scan.nextLine(); // Consume newline

                // Prompt user for hourly forecast preference
                System.out.print("Enter the number of hours for hourly forecasts (e.g., 12): ");
                int hourlyHours = scan.nextInt();
                scan.nextLine(); // Consume newline

                // Save user preferences
                userManager.savePreferences(newUsername, "multiday_forecast_days", String.valueOf(multidayDays));
                userManager.savePreferences(newUsername, "hourly_forecast_hours", String.valueOf(hourlyHours));

                System.out.println("Preferences saved successfully! Please log in.");
                break;

            default:
                System.out.println("Invalid option. Please choose 1 or 2.");
        }
    } else {
        // Handle invalid (non-integer) input
        String invalidInput = scan.next(); // Consume the invalid input
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
      "13: Create User Login\n" +
      "14: Login into your account\n" +
      "15: Manage Weather Preferences\n" +
      "16: Suggest Activites based on the weather\n" +
      "17: Unfavorite a Location\n" +
      "18: View ESPN Scores\n" +
      "19: View Hourly Forecast\n" +
      "20: Compare the Weather of Two Locations");

      // System.out.print("Please enter a number for the choice you want: ");

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
      scan.nextLine(); // Clear newline after nextInt()
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
      }


      else if(choice == 3){
        // Call to log weather observations
        System.out.print("Enter a ZIP code: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        controller.saveObservation(location);
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


      else if(choice == 10) {
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
            String forecastInfo = controller.getMultidayForecast(location);
            System.out.println(forecastInfo);
        }
    }


      else if(choice == 11){
        // Call to share weather data
        System.out.print("Please enter the email address to share the weather data: ");
        String email = scan.next();
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


      else if(choice == 13){
        System.out.print("done ");
      }


      else if(choice == 14){
        System.out.print("done ");
      }


      else if(choice == 15){
        // Call to manage user preferences
        System.out.print("Enter your username: ");
        String username = scan.nextLine().trim();
        System.out.print("Enter the preference you want to set (e.g., unit): ");
        String preference = scan.nextLine().trim();
        System.out.print("Enter the value for the preference: ");
        String value = scan.nextLine().trim();
        controller.savePreferences(username, preference, value);
      }

      else if(choice == 16){
        // Call to suggest activites based on the weather
        System.out.print("Enter a ZIP code: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
        }
        System.out.println(controller.getSuggestedActivity(location));  
      }


      else if(choice == 17){
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
      else if(choice == 18){
        controller.getESPNScores();
      }
      else if(choice == 19) {
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
            String weatherInfo = controller.getWeatherByLocation(location);
            String[] parts = weatherInfo.split("=== Hourly Forecast ===");
            if (parts.length > 1) {
                String hourlyForecast = parts[1].trim();
                System.out.println("Hourly Forecast:\n" + hourlyForecast);
            }
        }
    }


      else if(choice == 20){
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