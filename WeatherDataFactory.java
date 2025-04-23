public class WeatherDataFactory {
    

    // Factory class to create WeatherDataSource instances
    // This class is responsible for creating instances of the weather data source based on the type of source required.
    
    public WeatherDataSource createWeatherDataSource(String type) {
        if ("NOAA".equalsIgnoreCase(type)) {
            return new NOAASource();
        } 
        return null;
}
}
