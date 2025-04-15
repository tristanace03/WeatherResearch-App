
public class SQLiteDatabaseFactory{

    // Write loigc for returning SQLLite DBManager
    public AbstractDatabaseManager createDatabaseManager(String type) {
        if ("SQLLite".equalsIgnoreCase(type))
            return new DatabaseManager();
        return null;
    }

}
