public class WeatherData{
  //represents the real time weather data (temp, humidity, etc.)

  private double temperature;
  private double humidity;
  private double windSpeed;
  private String condition;

  /**
   * Method to Format and override the toString() Method
   * 
   * @return String
   */
  @Override
  public String toString(){
    return "Temperature: " + this.temperature + ", Humidity: " + this.humidity + ", Wind Speed: " + this.windSpeed + ", Condition: " + this.condition;
  }
}
