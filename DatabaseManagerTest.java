import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.Date;

public class DatabaseManagerTest {

    private DatabaseManager dbManager;

    @Before
    public void setUp() {
        dbManager = new DatabaseManager();
    }

    @Test
    public void testLogAndRetrieveObservation() {
        // Arrange
        String locationName = "Testville, IL";
        String notes = "Clear skies with light breeze";
        double temperature = 72.5;
        Date now = new Date();

        Observation obs = new Observation(locationName, now, notes, temperature);

        // Act
        boolean saved = dbManager.logObservation(obs);
        Observation result = dbManager.searchLogs(locationName);

        // Assert
        assertTrue("Observation should be logged successfully", saved);
        assertNotNull("Search should return a result", result);
        assertEquals("Location should match", locationName, result.getLocation());
        assertEquals("Notes should match", notes, result.getNotes());
        assertEquals("Temperature should match", temperature, result.getTemperature(), 0.01);
    }
}
