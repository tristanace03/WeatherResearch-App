public class Location {
  private double latitude;
  private double longitude;
  private String state;
  private String town;

  public Location(double latitude, double longitude, String state, String town) {
      this.latitude = latitude;
      this.longitude = longitude;
      this.state = state;
      this.town = town;
  }

  public double getLatitude() {
      return latitude;
  }

  public double getLongitude() {
      return longitude;
  }

  public String getState() {
      return state;
  }

  public String getTown() {
      return town;
  }

  public String toString() {
      return (town.isEmpty() ? "Unknown" : town) + ", " + state;
  }
}