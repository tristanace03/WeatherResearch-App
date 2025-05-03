import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuggestActivities {

    public static String suggestActivityFromForecast(String hourlyForecastText) {
        if (hourlyForecastText == null || hourlyForecastText.isEmpty()) {
            return "No forecast available";
        }

        String[] lines = hourlyForecastText.split("\n");
        if (lines.length == 0) {
            return "No forecast lines found";
        }

        String firstLine = lines[0];

        int tempF;
        try {
            // Match the first temperature (e.g., "90°F")
            Pattern pattern = Pattern.compile("(\\d+)°F");
            Matcher matcher = pattern.matcher(firstLine);
            if (matcher.find()) {
                tempF = Integer.parseInt(matcher.group(1));
            } else {
                return "Invalid temperature data";
            }
        } catch (Exception e) {
            return "Invalid temperature data";
        }

        int rainChance = 0;
        try {
            Pattern rainPattern = Pattern.compile("(\\d+)% chance of rain");
            Matcher rainMatcher = rainPattern.matcher(firstLine);
            if (rainMatcher.find()) {
                rainChance = Integer.parseInt(rainMatcher.group(1));
            }
        } catch (Exception e) {
            rainChance = 0;
        }

        boolean isRainLikely = rainChance >= 20;

        if (isRainLikely || tempF < 60) {
            return "Suggested Activity: Stay indoors or bring an umbrella. Good time for getting homework done or to watch a good movie.";
        } else if (tempF >= 80) {
            return "Suggested Activity: Great for outdoor fun like biking or a picnic, maybe a round of golf.";
        } else {
            return "Suggested Activity: Go for a walk or light outdoor activity.";
        }
    }
}
