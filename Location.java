/**
 * Represents a geographical location with latitude, longitude, state, and town.
 * 
 * Stores geographical details such as latitude, longitude, state, and town.
 * Provides methods to access these details and a string representation of the location.
 */

public class Location {

  private double latitude;
  private double longitude;
  private String state;
  private String town;

  /**
   * Constructor to initialize a Location object with latitude, longitude, state, and town.
   * @param latitude the latitude of the location
   * @param longitude the longitude of the location
   * @param state the state of the location
   * @param town the town of the location
   */
  public Location(double latitude, double longitude, String state, String town) {
      this.latitude = latitude;
      this.longitude = longitude;
      this.state = state;
      this.town = town;
  }

  /**
   * Gets the latitude of the location.
   * 
   * @return the latitude of the location
   */
  public double getLatitude() {
      return latitude;
  }

  /**
   * Gets the longitude of the location.
   * 
   * @return the longitude of the location
   */
  public double getLongitude() {
      return longitude;
  }

  /**
   * Gets the state of the location.
   * 
   * @return the state of the location
   */
  public String getState() {
      return state;
  }

  /**
   * Gets the town of the location.
   * 
   * @return the town of the location
   */
  public String getTown() {
      return town;
  }

  /**
   * Returns a string representation of the location in the format "Town, State".
   * If the town is empty, it returns "Unknown, State".
   * 
   * @return a string representation of the location
   */
  @Override
  public String toString() {
      return (town.isEmpty() ? "Unknown" : town) + ", " + state;
  }
}