import java.util.Date;

public class Observation{
  //representation of a single logbook entry (location, timestamp, notes, etc.)
    private String location;
    private Date timestamp;
    private String notes;
    private double temperature;

    public Observation(String location, Date timestamp, String notes, double temperature) {
        this.location = location;
        this.timestamp = timestamp;
        this.notes = notes;
        this.temperature = temperature;
    }

    public String getLocation() {
        return location;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public double getTemperature() {
        return temperature;
    }

    public String toString() {
        return location + " at " + timestamp + " — " + temperature + "C: " + notes;
    }
  
}