import java.sql.Connection;

public abstract class AbstractDatabaseHelper {
    
    protected Connection connection = null;
    
    // Abstract methods to be implemented by subclasses
    protected abstract boolean connect();
    protected abstract void initializeDatabase();
    protected abstract boolean disconnect();
    public abstract Connection getConnection();

}
