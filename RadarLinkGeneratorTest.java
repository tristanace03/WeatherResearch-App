import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class RadarLinkGeneratorTest {

    @Test
    void buildRadarUrl_formatsCorrectly() {
        // simulate a minimal /points JSON
        String test = new JSONObject()
            .put("properties", new JSONObject()
                .put("radarStation", "TEST123")
            )
            .toString();

        String url = RadarLinkGenerator.buildRadarUrl(test);

        assertEquals(
            "https://radar.weather.gov/ridge/standard/TEST123_loop.gif",
            url,
            "should insert the station ID into the .gif URL"
        );
    }
}

//this was created by David Arellano, but Tristan copied and pasted this to main. Proof that he completed this can be found in his branch. 
