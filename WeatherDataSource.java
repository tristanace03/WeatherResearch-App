/**
 * Defines the contract for weather data sources used within the application
 * 
 * Provides methods for retrieving weather fata, forecasts and alerts
 * Supports integration with various weather APIs and data providers
 * Encapuslates the URLs and endpoints required for fetching weather data
 * 
 * This interface allows for the implementation of multiple weather data sources with a consistent API
 */
import org.json.JSONArray;

public interface WeatherDataSource {

    /**
     * Retrieves point data for a specific location
     * 
     * @param location the location for which to retrieve point data
     * @return a PointData object containing URLs for weather data
     */
    PointData getPointData(Location location);

    /**
     * Retrieves daily forecasts from the specified URL
     * 
     * @param forecastUrl the URL for daily forecasts
     * @return a JSONArray object containing daily forecast data
     */
    JSONArray getDailyForecast(String forecastUrl);

    /**
     * Retrieves hourly forecasts from the specified URL
     * 
     * @param hourlyForecastUrl the URL for hourly forecasts
     * @return a JSONArray object containing hourly forecast data
     */
    JSONArray getHourlyForecast(String hourlyForecastUrl);

    /**
     * Retrieves weather alerts from the specified URL
     * 
     * @param alertsUrl the URL for weather alerts
     * @return a JSONArray object containing weather alert data
     */
    JSONArray getAlerts(String alertsUrl);

    /**
     * Retrieves fire weather alerts from the specified URL
     * 
     * @param fireWeatherUrl the URL for fire weather alerts
     * @return a JSONArray object containing fire weather alert data
     */
    JSONArray getFireWeather(String fireWeatherUrl);

    /**
     * Retrieves marine forecasts for the specified marine zone
     * 
     * @param marineZone the identifier for the marine zone
     * @return a JSONArray object containing marine forecast data
     */
    JSONArray getMarineForecast(String marineZone);

    /**
     * Encapsulates point data including URLs for forecasts, alerts and other weather-related information
     */
    public static class PointData {
        public String forecastUrl;
        public String hourlyForecastUrl;
        public String alertsUrl;
        public String fireWeatherUrl;
        public String marineZone;

        /**
         * Constructs a new PointData object with the specified URLs
         * @param forecastUrl the URL for daily forecasts 
         * @param hourlyForecastUrl the URL for hourly forecasts
         * @param alertsUrl the URL for weather alerts
         * @param fireWeatherUrl the URL for fire weather alerts
         * @param marineZone the marine zone identifier
         */
        public PointData(String forecastUrl, String hourlyForecastUrl, String alertsUrl, String fireWeatherUrl, String marineZone) {
            this.forecastUrl = forecastUrl;
            this.hourlyForecastUrl = hourlyForecastUrl;
            this.alertsUrl = alertsUrl;
            this.fireWeatherUrl = fireWeatherUrl;
            this.marineZone = marineZone;
        }
    }
}