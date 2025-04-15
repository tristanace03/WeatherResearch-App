public abstract class WeatherDataSource {
    /**
     * Fetches weather data for a specific location.
     * @param location Name of the location (city, ZIP, etc.)
     * @return WeatherData object containing the weather details
     */
    public abstract WeatherData getWeatherData(String location);
}
