/**
 * A class that fetches news articles based on a given location using the NewsData API.
 * 
 * Connects to the NewsData API, retrieves news articles for a specified location, and parses the JSON response to display article titles, summaries, and URLs.
 * 
 * Used by other parts of the application to fetch and display news articles based on user location.
 */

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class NewsService {

    private static final String API_KEY = "pub_84434af38f457a60c8b78cf845a3afab540c9";
    private static final String BASE_URL = "https://newsdata.io/api/1/latest";

    /**
     * Fetches news articles for a specified location using the NewsData API.
     * 
     * Connects to the NewsData API, retrieves news articles for a specified location, and parses the JSON response to display the top 5 article titles, summaries, and URLs.
     * 
     * @param location the location for which to fetch news articles
     */
    public void getNewsByLocation(Location location) {
        try{
            String locationQuery = location.getTown() + "," + location.getState();
            String urlStr = BASE_URL +
                    "?apikey=" + API_KEY +
                    "&q=" + URLEncoder.encode(location.getTown() + ", " + location.getState(), "UTF-8") +
                    "&country=us";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");

            Scanner in = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (in.hasNextLine()) {
                response.append(in.nextLine());
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray articles = jsonResponse.getJSONArray("results");

            System.out.println("\n=== News for " + location + " ===");
            for (int i = 0; i < Math.min(5, articles.length()); i++) {
                JSONObject article = articles.getJSONObject(i);
                System.out.println("\nTitle: " + article.getString("title"));
                System.out.println("Summary: " + article.getString("description"));
                System.out.println("URL: " + article.getString("link"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving news: " + e.getMessage());
        }
    }
}
