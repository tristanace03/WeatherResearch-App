public class NOAASource extends WeatherDataSource {
    @Override
    public WeatherData getWeatherData(String location) {
        // TODO: Replace with real HTTP API call to NOAA API

        // Example mocked response from NOAA
        double temperature = 5;
        double humidity = 5;
        double windSpeed = 5;
        String condition = "Cloudy";

        return new WeatherData(temperature, humidity, windSpeed, condition);
    }
}
