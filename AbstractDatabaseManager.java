import java.sql.Date;

public abstract class AbstractDatabaseManager {
    
    public abstract void savePreference(String key, String value);
    public abstract String getPreference(String key);
    public abstract boolean logObservation(Observation obs);
    public abstract Observation searchLogs(Date timestamp);
}
