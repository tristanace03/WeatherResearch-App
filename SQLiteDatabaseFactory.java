
public class SQLiteDatabaseFactory{

    // Factory class to create SQLite database manager instances
    // This class is responsible for creating instances of the database manager based on the type of database required.
    
    public AbstractDatabaseManager createDatabaseManager(String type) {
        if ("SQLLite".equalsIgnoreCase(type))
            return new DatabaseManager();
        return null;
    }

}
