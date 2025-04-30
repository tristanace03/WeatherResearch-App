

import java.util.Date;
import java.util.List;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;

public class WeatherService{
  //manages weather data retrieval and potentially aggregating from multiple sources (if we use both NOAA and OpenWeatherMap APIs)

  private WeatherDataSourceFactory weatherDataSourceFactory;


  /**
   * Method to retrieve the real time weather of a location, will return a Weather Data object
   * 
   * @param location
   * @return WeatherData
   */
  public WeatherData getRealTimeWeather(String location){
    //TODO

    // Need to set up with API to retrieve values for these
    double temperature = 0;
    double humidity = 0;
    double windSpeed = 0;
    String condition = "";

    WeatherData realTimeWeather = new WeatherData(temperature, humidity, windSpeed, condition);
    Observation obs = new Observation(location, new Date(), "Real-time weather data", temperature);
    SQLiteDatabaseFactory dbFactory = new SQLiteDatabaseFactory();
    dbFactory.createDatabaseManager("SQLLite").logObservation(obs);
    return realTimeWeather;
  }

  /**
   * 
   * @param location
   * @return List<WeatherData>
   */
  public List<WeatherData> getSevereWeatherAlerts(String location){
    //TODO
    return null;
  }

  /**
   * Method to retrieve Mesoscale Discussion, will return a String with the discussion
   * 
   * @param location
   * @return String
   */
  public String getMesoscaleDiscussion(String location){
    //TODO
    // Set up with API to retrieve discussion
    String discussion = "";

    return discussion;
  }

  public void shareWeatherData(String to){
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
        message.setText("Here is the weather data: " + getRealTimeWeather("Chicago").toString());

        Transport.send(message);
    } catch (MessagingException e) {
        e.printStackTrace();
    }
  }
}
