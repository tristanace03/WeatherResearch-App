/**
 * Represents a single logbook entry that contains weather observation details
 * 
 * Stores details such as location, timestamp, notes, and temperature.
 * Provides methods to access these details and a string representation of the observation.
 * 
 */
import java.util.Date;

public class Observation{

    private String location;
    private Date timestamp;
    private String notes;
    private double temperature;

    /**
     * Constructor to initialize an Observation object with location, timestamp, notes, and temperature.
     * 
     * @param location the location of the observation
     * @param timestamp the date and time of the observation
     * @param notes the notes about the observation
     * @param temperature the temperature at the time of observation
     */
    public Observation(String location, Date timestamp, String notes, double temperature) {
        this.location = location;
        this.timestamp = timestamp;
        this.notes = notes;
        this.temperature = temperature;
    }

    /**
     * Gets the location of the observation.
     * 
     * @return the location of the observation
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the timestamp of the observation.
     * 
     * @return the date and time of the observation
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the notes about the observation.
     * 
     * @return notes for the observation
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Gets the temperature at the time of observation.
     * 
     * @return the temperature of the observation
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Returns a string representation of the observation.
     * 
     * @return a string containing the location, timestamp, temperature, and notes of the observation
     */
    @Override
    public String toString() {
        return location + " at " + timestamp + " — " + temperature + "C: " + notes;
    }
  
}