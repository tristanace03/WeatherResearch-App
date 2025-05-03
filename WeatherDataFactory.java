import org.json.JSONObject;

public class WeatherDataFactory {
    public WeatherData createWeatherData(JSONObject json, String type) {
        String time;
        int temperature = 0;
        String description;
        int precipitationChance = 0;
        String severity = null;

        switch (type.toLowerCase()) {
            case "daily":
                time = json.optString("name", "Unknown");
                description = json.optString("detailedForecast", "No forecast available");
                break;

            case "hourly":
                time = json.optString("startTime", "Unknown");
                temperature = json.optInt("temperature", 0);
                description = json.optString("shortForecast", "No forecast available");
                JSONObject probOfPrecip = json.optJSONObject("probabilityOfPrecipitation");
                precipitationChance = (probOfPrecip != null && !probOfPrecip.isNull("value")) ? probOfPrecip.getInt("value") : 0;
                break;

            case "marine":
                time = json.optString("name", "Unknown");
                description = json.optString("detailedForecast", "No marine forecast available");
                break;

            case "alert":
                time = json.optString("effective", "Unknown");
                severity = json.optString("severity", "Unknown");
                String event = json.optString("event", "Unknown Alert");
                String headline = json.optString("headline", "No headline");
                String alertDesc = json.optString("description", "No description");
                description = String.format("%s: %s\nDescription: %s", event, headline, alertDesc);
                break;

            default:
                throw new IllegalArgumentException("Unsupported WeatherData type: " + type);
        }

        return new WeatherData(type, time, temperature, description, precipitationChance, severity);
    }
}