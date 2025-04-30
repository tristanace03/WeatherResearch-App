/** 
public abstract class WeatherDataSource {
    
     * Fetches weather data for a specific location.
     * @param location Name of the location (city, ZIP, etc.)
     * @return WeatherData object containing the weather details
     
    public abstract WeatherData getWeatherData(String location);
}
*/

import org.json.JSONArray;

public interface WeatherDataSource {
    public static class PointData {
        public String forecastUrl;
        public String hourlyForecastUrl;
        public String alertsUrl;
        public String fireWeatherUrl;
        public String marineZone;

        public PointData(String forecastUrl, String hourlyForecastUrl, String alertsUrl, String fireWeatherUrl, String marineZone) {
            this.forecastUrl = forecastUrl;
            this.hourlyForecastUrl = hourlyForecastUrl;
            this.alertsUrl = alertsUrl;
            this.fireWeatherUrl = fireWeatherUrl;
            this.marineZone = marineZone;
        }
    }

    PointData getPointData(Location location);
    JSONArray getDailyForecast(String forecastUrl);
    JSONArray getHourlyForecast(String hourlyForecastUrl);
    JSONArray getAlerts(String alertsUrl);
    JSONArray getFireWeather(String fireWeatherUrl);
    JSONArray getMarineForecast(String marineZone);
}