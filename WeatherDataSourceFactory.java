/**
 * Factory class for creating instance of WeatherDataSource
 * 
 * Provides a centralized mechanism for creating weather data source objects.
 * Supports multiple weather data providers such as NOAA
 * Encapsulates the logic for selecting the appropate data source based on input parameters
 * 
 * This class simplifies the process of integrating additional weather data sources
 */
public class WeatherDataSourceFactory {
 
    /**
     * Creates a WeatherDataSource instance based on the specified type
     * 
     * @param type the type of weather data source
     * @return a WeatherDataSOurce instance for the specified type, or null if that type is not supported.
     */
    public static WeatherDataSource createWeatherDataSource(String type) {
        if ("NOAA".equalsIgnoreCase(type)) {
            return new NOAASource();
        } 
    return null;
    }
}
