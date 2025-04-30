import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class NOAASource implements WeatherDataSource {
    private static final String NWS_BASE_URL = "https://api.weather.gov";
    private static final String USER_AGENT = "WeatherResearchApp/1.0 (tristanace2@gmail.com)";

    @Override
    public PointData getPointData(Location location) {
        try {
            String pointUrl = NWS_BASE_URL + "/points/" + location.getLatitude() + "," + location.getLongitude();
            JSONObject pointData = getJson(pointUrl);
            if (pointData == null) {
                return null;
            }

            JSONObject properties = pointData.getJSONObject("properties");
            String forecastUrl = properties.getString("forecast");
            String hourlyForecastUrl = properties.getString("forecastHourly");
            String alertsUrl = NWS_BASE_URL + "/alerts/active?point=" + location.getLatitude() + "," + location.getLongitude();
            String fireWeatherUrl = NWS_BASE_URL + "/alerts/active?event=Red%20Flag%20Warning&point=" + location.getLatitude() + "," + location.getLongitude();
            String marineZone = properties.optString("forecastZone", "").contains("MZ") ? properties.getString("forecastZone") : "";

            return new PointData(forecastUrl, hourlyForecastUrl, alertsUrl, fireWeatherUrl, marineZone);
        } catch (Exception e) {
            System.err.println("Error retrieving point data: " + e.getMessage());
            return null;
        }
    }

    @Override
    public JSONArray getDailyForecast(String forecastUrl) {
        try {
            JSONObject forecast = getJson(forecastUrl);
            return forecast.getJSONObject("properties").getJSONArray("periods");
        } catch (Exception e) {
            System.err.println("Error fetching daily forecast: " + e.getMessage());
            return null;
        }
    }

    @Override
    public JSONArray getHourlyForecast(String hourlyForecastUrl) {
        try {
            JSONObject forecast = getJson(hourlyForecastUrl);
            return forecast.getJSONObject("properties").getJSONArray("periods");
        } catch (Exception e) {
            System.err.println("Error fetching hourly forecast: " + e.getMessage());
            return null;
        }
    }

    @Override
    public JSONArray getAlerts(String alertsUrl) {
        try {
            JSONObject alerts = getJson(alertsUrl);
            return alerts.getJSONArray("features");
        } catch (Exception e) {
            System.err.println("Error fetching alerts: " + e.getMessage());
            return null;
        }
    }

    @Override
    public JSONArray getFireWeather(String fireWeatherUrl) {
        try {
            JSONObject alerts = getJson(fireWeatherUrl);
            return alerts.getJSONArray("features");
        } catch (Exception e) {
            System.err.println("Error fetching fire weather warnings: " + e.getMessage());
            return null;
        }
    }

    @Override
    public JSONArray getMarineForecast(String marineZone) {
        try {
            String forecastUrl = NWS_BASE_URL + "/forecasts/zones/" + marineZone.split("/")[-1];
            JSONObject forecast = getJson(forecastUrl);
            return forecast.getJSONObject("properties").getJSONArray("periods");
        } catch (Exception e) {
            System.err.println("Error fetching marine forecast: " + e.getMessage());
            return null;
        }
    }

    private JSONObject getJson(String url) throws Exception {
        return new JSONObject(getString(url));
    }

    private String getString(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            return sb.toString();
        }
    }
}