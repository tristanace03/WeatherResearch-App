/**
 * Provides core weather-related services, including data retrieval and aggregation
 * 
 * Manages weather data retrieval from multiple sources
 * Supprots daily, hourly and marine forecasts, as well as severe weather alrts
 * Encapsulates integration with external data sources using WeatherDataSource and WeatherDataFactory
 * 
 * This class acts as a central service for weather-related functionalities in the application
 */
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;

public class WeatherService{

  private WeatherDataSourceFactory weatherDataSourceFactory;
  private WeatherDataSource weatherSource;
  private WeatherDataFactory weatherDataFactory;

    /**
    * Default constructor for WeatherService
    */
     public WeatherService(){

    }

    /**
     * Constructor with specified data source and factory
     * 
     * @param weatherSource the data source for retrieving weather data
     * @param weatherDataFactory the factory for creating WeatherData objects
     */
    public WeatherService(WeatherDataSource weatherSource, WeatherDataFactory weatherDataFactory){
        this.weatherSource = weatherSource;
        this.weatherDataFactory = weatherDataFactory;
    }
  
  
    /**
     * Retrieves the real-time weather for a specified location
     * 
     * @param location the locaiton for which to retrieve the weather
     * @return a string containing the formatted weather data, or error if retrieval fails
     */
    public String getWeatherByLocation(Location location){
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

    /**
     * Retrieves severe weather alerts for a specified location
     * 
     * @param location the location for which to retrieve weather alerts
     * @return a string containing the formatted weather alrts, or an error message if retrieval fails
     */
    public String getSevereWeatherAlerts(Location location){
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
                output.append("").append(alert.getString("event"))
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
                output.append("").append(alert.getString("event")).append("\n")
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

    /**
     * Retrieves the multiday weather forecast for a specified location
     * 
     * @param location the location for which to retrieve the forecast
     * @return a string containing the multiday forecast, or an error message if retrieval fails
     */
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

    /**
     * Shares weather data for a specific location via email
     * 
     * @param to the recepients email address
     * @param location the location for which to share weather data
     */
    public void shareWeatherData(String to, Location location) {
        String from = "weatherappit326@gmail.com";
        String password = "yhrc kauq zrzx fvld";
        String host = "smtp.gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Weather Data");
            message.setText("Here is the weather data: " + getWeatherByLocation(location));

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves an observatino for a specified location with optional notes
     * 
     * @param location the location for which to save the observation
     * @param userNotes additional notes provided by the user
     */
    public void saveObservation(Location location, String userNotes) {
    
        String notes = (userNotes != null && !userNotes.isEmpty()) ? userNotes : "";
        
        WeatherDataSource.PointData pointData = weatherSource.getPointData(location);
        double temperature = 0.0;

        if (pointData != null) {
            JSONArray hourlyForecast = weatherSource.getHourlyForecast(pointData.hourlyForecastUrl);
            if (hourlyForecast != null && hourlyForecast.length() > 0) {
                JSONObject firstHour = hourlyForecast.getJSONObject(0);
                WeatherData data = weatherDataFactory.createWeatherData(firstHour, "hourly");
                temperature = data.getTemperature();
            }
        }

        Observation obs = new Observation(
            location.toString(),
            new java.sql.Date(System.currentTimeMillis()),
            notes,
            temperature
        );

        DatabaseManager dbManager = new DatabaseManager();
        dbManager.logObservation(obs);
        System.out.println("Observation saved: " + obs.toString());
    }

    /**
     * Retrieves the most recent observation for a specified location
     * 
     * @param location the location for which to retrieve the observation
     * @return the Observation object containing the observation data
     */
    public Observation getObservation(Location location) {
        SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
        return dbFactory.createDatabaseManager("SQLLite").searchLogs(location.toString());
    }

    /**
     * Generates a "heatmap" of observations by location
     */
    public void getHeatMap(){
        SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
        Map<String, Integer> heatmap = dbFactory.createDatabaseManager("SQLLite").getObservationCountsByLocation();
        if (heatmap.isEmpty()) {
            System.out.println("No observations logged yet.");
            return;
        }

        System.out.println("=== Observation Heatmap (by Location) ===");
        for (Map.Entry<String, Integer> entry : heatmap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " observations");
        }
    }
}
