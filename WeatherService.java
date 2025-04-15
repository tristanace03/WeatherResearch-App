import java.util.List;

public class WeatherService{
  //manages weather data retrieval and potentially aggregating from multiple sources (if we use both NOAA and OpenWeatherMap APIs)

  private WeatherDataSource weatherSource;


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

    return realTimeWeather;
  }

  /**
   * 
   * @param location
   * @return List<WeatherData>
   */
  public List<WeatherData> getSevereWeatherAlerts(String location){
    //TODO
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
}
