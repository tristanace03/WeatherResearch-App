import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;


public class SuggestActivitiesTest {

    @Before
    public void setUp() {
        System.out.println("Starting a test...");
    }

    @After
    public void cleanUp() {
        System.out.println("Finished a test.\n");
    }

    @Test
    public void testColdAndRainy() {
        String forecast = "2025-05-03T13:00:00-05:00: 50°F, Chance Rain Showers, 40% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Stay indoors or bring an umbrella. Good time for getting homework done or to watch a good movie.", result);
    }

    @Test
    public void testWarmAndRainy() {
        String forecast = "2025-05-03T14:00:00-05:00: 65°F, Light Rain, 50% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Stay indoors or bring an umbrella. Good time for getting homework done or to watch a good movie.", result);
    }

    @Test
    public void testHotAndRainy() {
        String forecast = "2025-05-03T15:00:00-05:00: 85°F, Thunderstorms, 70% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Stay indoors or bring an umbrella. Good time for getting homework done or to watch a good movie.", result);
    }

    @Test
    public void testColdAndClear() {
        String forecast = "2025-05-03T16:00:00-05:00: 45°F, Clear, 0% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Stay indoors or bring an umbrella. Good time for getting homework done or to watch a good movie.", result);
    }

    @Test
    public void testHotAndClear() {
        String forecast = "2025-05-03T17:00:00-05:00: 90°F, Sunny, 0% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Great for outdoor fun like biking or a picnic, maybe a round of golf.", result);
    }

    @Test
    public void testMildAndClear() {
        String forecast = "2025-05-03T18:00:00-05:00: 70°F, Mostly Cloudy, 10% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Go for a walk or light outdoor activity.", result);
    }

    @Test
    public void testUpperBoundaryMild() {
        String forecast = "2025-05-03T19:00:00-05:00: 79°F, Clear, 5% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Go for a walk or light outdoor activity.", result);
    }

    @Test
    public void testLowerBoundaryHot() {
        String forecast = "2025-05-03T20:00:00-05:00: 80°F, Sunny, 5% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Great for outdoor fun like biking or a picnic, maybe a round of golf.", result);
    }

    @Test
    public void testUpperBoundaryCold() {
        String forecast = "2025-05-03T21:00:00-05:00: 59°F, Partly Cloudy, 10% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Stay indoors or bring an umbrella. Good time for getting homework done or to watch a good movie.", result);
    }

    @Test
    public void testLowerBoundaryMild() {
        String forecast = "2025-05-03T22:00:00-05:00: 60°F, Partly Cloudy, 10% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Go for a walk or light outdoor activity.", result);
    }

    @Test
    public void testEmptyForecast() {
        String forecast = "";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("No forecast available", result);
    }

    @Test
    public void testNoTemperature() {
        String forecast = "2025-05-03T23:00:00-05:00: Mostly Cloudy, 0% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Invalid temperature data", result);
    }

    @Test
    public void testGarbledInput() {
        String forecast = "foobar";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Invalid temperature data", result);
    }

    @Test
    public void testTempAtZero() {
        String forecast = "2025-05-03T23:00:00-05:00: 0°F, Blizzard, 90% chance of snow";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Stay indoors or bring an umbrella. Good time for getting homework done or to watch a good movie.", result);
    }

    @Test
    public void testRainWithZeroChance() {
        String forecast = "2025-05-03T23:00:00-05:00: 75°F, Light Rain, 0% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Go for a walk or light outdoor activity.", result);
    }

    @Test
    public void testJustRainWordWithHighChance() {
        String forecast = "2025-05-03T23:00:00-05:00: 75°F, Rain, 50% chance of rain";
        String result = SuggestActivities.suggestActivityFromForecast(forecast);
        assertEquals("Suggested Activity: Stay indoors or bring an umbrella. Good time for getting homework done or to watch a good movie.", result);
    }
}
