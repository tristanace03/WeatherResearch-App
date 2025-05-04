import java.util.Map;

public abstract class AbstractDatabaseManager {
    
    // Abstract class for database manager
    // This class will be used to create a connection to the database and perform operations
    public abstract void savePreference(String username, String key, String value);
    public abstract String getPreference(String username, String key);
    public abstract boolean logObservation(Observation obs);
    public abstract Observation searchLogs(String location);
    public abstract void exportToCSV();
    public abstract void saveUser(User user);
    public abstract Map<String, Integer> getObservationCountsByLocation();
}
