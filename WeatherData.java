public class WeatherData {
  private String type;
  private String time;
  private int temperature;
  private String description;
  private int precipitationChance;
  private String severity;

  public WeatherData(String type, String time, int temperature, String description, int precipitationChance, String severity) {
      this.type = type;
      this.time = time;
      this.temperature = temperature;
      this.description = description;
      this.precipitationChance = precipitationChance;
      this.severity = severity;
  }

  public String getType() {
      return type;
  }

  public String getTime() {
      return time;
  }

  public int getTemperature() {
      return temperature;
  }

  public String getDescription() {
      return description;
  }

  public int getPrecipitationChance() {
      return precipitationChance;
  }

  public String getSeverity() {
      return severity;
  }

}

