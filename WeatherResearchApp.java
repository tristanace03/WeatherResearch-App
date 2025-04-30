import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.Scanner;
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
            WeatherDataSource weatherSource = WeatherDataSourceFactory.createWeatherDataSource("NOAA");
            WeatherController controller = new WeatherController(dbManager, userManager, espnScores, weatherSource);
    Scanner scan = new Scanner(System.in);

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
      "13: Get a safe route to travel from one location to another\n" +
      "14: Login into your account\n" +
      "15: Manage Weather Preferences\n" +
      "16: Suggest Activites based on the weather\n" +
      "17: Unfavorite a Location\n" +
      "18: View ESPN Scores\n" +
      "19: View Mesoscale Discussions\n" +
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
      else if(choice==1){
        System.out.print("Enter a ZIP code: ");
                    String zip = scan.nextLine().trim();
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
      }
      else if(choice == 4){
        // Call to view Heat Map of Logbook Locations
      }
      else if(choice == 5){
        // Call to set a Favorite Location
      }
      else if(choice == 6){
        System.out.print("Enter a ZIP code: ");
        String zip = scan.nextLine().trim();
        Location location = getCoordinatesFromZIP(zip);
        if (location != null) {
            System.out.println("\nLocation: " + location);
            String alertsInfo = controller.getAlerts(location);
            System.out.println(alertsInfo);
        }
      }
      else if(choice == 7){
        // Call to set weather preferences
      }
      else if(choice == 8){
        // Call to export Logbook Data
        controller.exportLogbookData();
      }
      else if(choice == 9){
        // Call to search for Historical logbook Entries
      }
      else if(choice == 10){
       
        System.out.print("Enter a ZIP code: ");
        String zip = scan.nextLine().trim();
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
        controller.shareWeatherData(email);
      }
      else if(choice == 12){
        // Call to see the current news
      }
      else if(choice == 13){
        // Call to plan a safe route 
      }
      else if(choice == 14){
        // Call to login to user account
      }
      else if(choice == 15){
        // Call to manager user preferences
      }
      else if(choice == 16){
        // Call to suggest activites based on the weather
      }
      else if(choice == 17){
        // Call to unfavorite a location
      }
      else if(choice == 18){
        controller.getESPNScores();
      }
      else if(choice == 19){
        // Call to Retrieve Mesoscale Discussions
      }
      else if(choice == 20){
        // Call to compare two locations
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