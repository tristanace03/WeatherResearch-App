import java.sql.Date;

public abstract class AbstractDatabaseManager {
    
    // Abstract class for database manager
    // This class will be used to create a connection to the database and perform operations
    public abstract void savePreference(String key, String value);
    public abstract String getPreference(String key);
    public abstract boolean logObservation(Observation obs);
    public abstract Observation searchLogs(Date timestamp);
    public abstract void exportToCSV();
}
