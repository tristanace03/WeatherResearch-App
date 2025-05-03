public class WeatherDataSourceFactory {
    

    // Factory class to create WeatherDataSource instances
    // This class is responsible for creating instances of the weather data source based on the type of source required.
    
    public static WeatherDataSource createWeatherDataSource(String type) {
        if ("NOAA".equalsIgnoreCase(type)) {
            return new NOAASource();
        } 
        return null;
}
}
