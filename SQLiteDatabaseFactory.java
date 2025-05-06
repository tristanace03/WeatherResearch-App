/**
 * A factory class for creating instances of database managers, specifically for SQLite databases.
 * 
 * Encapsulates the logic fore creating database manager instances based on the specified type.
 * Currently supports SQLite database managemenet through DatabaseManager.
 * 
 * Follows Factory design pattern to abstract the creation of database managers.
 */
public class SQLiteDatabaseFactory{

    /**
     * Creates and returns an instanec of a databse manager based on the specified type
     * 
     * @param type the type of database manager to create
     * @return an AbstractDatabaseManager instance for the specified type (or null if type is unsupported)
     */
    public AbstractDatabaseManager createDatabaseManager(String type) {
        if ("SQLLite".equalsIgnoreCase(type))
            return new DatabaseManager();
        return null;
    }

}
