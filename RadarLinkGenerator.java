// david
/**
 * A class that generates a radar loop URL based on the user's location.
 *
 * Fetches the user's latitude and longitude using a public IP service, retrieves the nearest NWS radar station using the Weather.gov API, and constructs the radar loop URL.
 * 
 * This class is designed to simplify the process of finding radar imagery for a specific location
 * 
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class RadarLinkGenerator {

    /**
     * Main method to execute the radar link generation process
     * 
     * @param args 
     */
    public static void main(String[] args) {
        try {
            //Detect latitude and longitude via public IP
            String geoJson = httpGet("https://ipapi.co/json/");
            JSONObject geo = new JSONObject(geoJson);
            double lat = geo.getDouble("latitude");
            double lon = geo.getDouble("longitude");
            String city = geo.optString("city", "Unknown");

            System.out.printf("detected: %s (lat=%.5f, lon=%.5f)%n", city, lat, lon);

            // get nearest NWS radar station
            String ptsJson = httpGet(
                    String.format("https://api.weather.gov/points/%.5f,%.5f", lat, lon));
            JSONObject props = new JSONObject(ptsJson).getJSONObject("properties");
            String station = props.getString("radarStation");

            // Build and print radar loop url
            String radarUrl = "https://radar.weather.gov/ridge/standard/"
                    + station + "_loop.gif";
            System.out.println("radar url: " + radarUrl);

        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
        }
    }

    /**
     * Performs a HTTP GET request to retrieve data from the specified URL
     * 
     * @param urlStr the URL to send the GET request to
     * @return response body as a string
     */
    private static String httpGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "java-radar");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}
