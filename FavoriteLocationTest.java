import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class FavoriteLocationTest {
    private UserManager userManager;
    private DatabaseManager databaseManager;

    @Before
    public void setUp() {
        userManager = new UserManager();
        databaseManager = new DatabaseManager();
        System.out.println("Starting a test...");
    }

    @After
    public void cleanUp() {
        userManager = null;
        databaseManager = null;
        System.out.println("Finished a test.\n");
    }

    // Test case for retrieving a favorite location
    @Test
    public void testGetFavoriteLocation() {
        String username = "testUser";
        String favoriteZip = "12345";

        databaseManager.savePreference(username, "favorite_location", favoriteZip);
        String result = databaseManager.getPreference(username, "favorite_location");

        assertEquals("Favorite location should match", favoriteZip, result);
    }

    // Test case for no favorite location
    @Test
    public void testGetFavoriteLocation_NoFavorite() {
        String username = "testUser";

        String result = databaseManager.getPreference(username, "favorite_location");

        assertNull("Favorite location should be null", result);
    }

    // Test case for logged-in user
    @Test
    public void testIsLoggedIn() {
        String username = "testUser";

        userManager.setLoggedInUser(username);
        boolean isLoggedIn = userManager.isLoggedIn();

        assertTrue("User should be logged in", isLoggedIn);
    }

    // Test case for logged-out user
    @Test
    public void testIsNotLoggedIn() {
        userManager.setLoggedInUser(null);
        boolean isLoggedIn = userManager.isLoggedIn();

        assertFalse("No user should be logged in", isLoggedIn);
    }

    // Test case for option logic using favorite location
    @Test
    public void testOptionUsingFavoriteLocation() {
        String username = "testUser";
        String favoriteZip = "12345";

        userManager.setLoggedInUser(username);
        databaseManager.savePreference(username, "favorite_location", favoriteZip);

        String zip = null;
        if (userManager.isLoggedIn()) {
            String favorite = databaseManager.getPreference(userManager.getCurrentUsername(), "favorite_location");
            if (favorite != null) {
                zip = favorite;
            }
        }

        assertEquals("Favorite ZIP should be used", favoriteZip, zip);
    }

    // Test case for option logic prompting for ZIP code
    @Test
    public void testOptionPromptForZipCode() {
        String username = "testUser";

        userManager.setLoggedInUser(username);

        String zip = null;
        if (userManager.isLoggedIn()) {
            String favorite = databaseManager.getPreference(userManager.getCurrentUsername(), "favorite_location");
            if (favorite != null) {
                zip = favorite;
            }
        }

        assertNull("ZIP code should be prompted", zip);
    }

    // Test case for option when not logged in
    @Test
    public void testOptionWhenNotLoggedIn() {
        userManager.setLoggedInUser(null);

        String zip = null;
        if (userManager.isLoggedIn()) {
            String favorite = databaseManager.getPreference(userManager.getCurrentUsername(), "favorite_location");
            if (favorite != null) {
                zip = favorite;
            }
        }

        assertNull("ZIP code should be prompted", zip);
    }
}