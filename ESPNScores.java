/**
 * A class to fetch and display live sports scores from ESPN's API.
 * Rrtrieves scores for a specified sport and league, parases the data to display team names and scores.
 * 
 * Connects to the ESPN API, retrieves live scores for a specified sport and league, and parses the JSON response to display team names and scores.
 * 
 * Used by other parts of the application to fetch and display live sports scores.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ESPNScores {

    /**
     * Fetches live scores for a specified sport and league from ESPN's API.
     * 
     * @param sport the type of sport 
     * @param league the league of the sport
     */
    public void getScores(String sport, String league) {
        String urlString = "https://site.api.espn.com/apis/site/v2/sports/" + sport + "/" + league + "/scoreboard";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // Parse the JSON response
            JsonElement jsonElement = JsonParser.parseString(response.toString());
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonArray events = jsonObject.getAsJsonArray("events");
            if(events.size() == 0) {
                System.out.println("No events found for the specified sport and league.");
                return;
            }
            else {
                for (JsonElement eventElement : events) {
                    JsonObject event = eventElement.getAsJsonObject();
                    
                    // competitions is an array!
                    JsonArray competitionsArray = event.getAsJsonArray("competitions");
                    JsonObject competition = competitionsArray.get(0).getAsJsonObject();
                    
                    // competitors is an array too
                    JsonArray competitors = competition.getAsJsonArray("competitors");
                
                    String homeTeam = "";
                    String awayTeam = "";
                    int homeScore = -1;
                    int awayScore = -1;
                    
                    for (JsonElement competitorElement : competitors) {
                        JsonObject competitor = competitorElement.getAsJsonObject();
                        String teamName = competitor.getAsJsonObject("team").get("displayName").getAsString();
                        int score = Integer.parseInt(competitor.get("score").getAsString());
                        
                        if (competitor.get("homeAway").getAsString().equals("home")) {
                            homeTeam = teamName;
                            homeScore = score;
                        } else {
                            awayTeam = teamName;
                            awayScore = score;
                        }
                    }
                    System.out.println(awayTeam + " " + awayScore + " @ " + homeTeam + " " + homeScore);
                }
            }                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
