/**
 * Represents weather data for a specific time and type of forecast
 * 
 * Stores various weather attributes such as temerature, description and severity
 * Supports multiple types of weather data, such as daily, hourly, marine and alert
 */
public class WeatherData {

    private String type;
    private String time;
    private int temperature;
    private String description;
    private int precipitationChance;
    private String severity;

    /**
     * Constructs a new WeatherCode object with the specified attributes
     * 
     * @param type the type of weather data (daily, hourly, alert, etc.)
     * @param time the time/name associated with the weather data
     * @param temperature the temperature (in Fahrenheit)
     * @param description a short description of the weather conditions
     * @param precipitationChance chance of precipitation as a percentage
     * @param severity the severity level of the weather (Moderate, Severe, etc.)
     */
    public WeatherData(String type, String time, int temperature, String description, int precipitationChance, String severity) {
    this.type = type;
        this.time = time;
        this.temperature = temperature;
        this.description = description;
        this.precipitationChance = precipitationChance;
        this.severity = severity;
    }

    /**
     * Get the type of weather data
     * 
     * @return the type of weather data
     */
    public String getType() {
        return type;
    }

    /**
     * Get the time/name associated with the weather data
     * 
     * @return the time or name
     */
    public String getTime() {
        return time;
    }

    /**
     * Gets the temperature in degrees Fahrenheit
     * 
     * @return the temperature
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * Gets a short description of the weather conditions
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the chance of precipitation as a percentage
     * 
     * @return the precipitation chance
     */
    public int getPrecipitationChance() {
         return precipitationChance;
    }

    /**
     * Gets the severity level of the weather
     * 
     * @return the severity level
     */
    public String getSeverity() {
        return severity;
    }

}

