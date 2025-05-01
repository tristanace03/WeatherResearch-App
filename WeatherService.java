

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;

public class WeatherService{
  //manages weather data retrieval and potentially aggregating from multiple sources (if we use both NOAA and OpenWeatherMap APIs)

  private WeatherDataSourceFactory weatherDataSourceFactory;
  private WeatherDataSource weatherSource;
  private WeatherDataFactory weatherDataFactory;


  public WeatherService(){

  }

  public WeatherService(WeatherDataSource weatherSource, WeatherDataFactory weatherDataFactory){
    this.weatherSource = weatherSource;
    this.weatherDataFactory = weatherDataFactory;
  }
  /**
   * Method to retrieve the real time weather of a location, will return a Weather Data object
   * 
   * @param location
   * @return WeatherData
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
   * 
   * @param location
   * @return List<WeatherData>
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
}
