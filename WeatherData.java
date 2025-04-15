public class WeatherData{
  //represents the real time weather data (temp, humidity, etc.)

  private double temperature;
  private double humidity;
  private double windSpeed;
  private String condition;

  public WeatherData(double temperature, double humidity, double windSpeed, String condition){
    this.temperature = temperature;
    this.humidity = humidity;
    this.windSpeed = windSpeed;
    this.condition = condition;
  }


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
