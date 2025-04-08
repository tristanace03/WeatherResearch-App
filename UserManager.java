import java.util.HashMap;
import java.util.Map;

public class UserManager{
  //handles login, preferenecs and favorite locations 
    private Map<String, String> users;
    private Map<String, String> preferences;

    public UserManager() {
        users = new HashMap<>();
        preferences = new HashMap<>();
    }

    public void addUser(String username, String password) {
        users.put(username, password);
    }

    public boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public void setPreference(String username, String preference) {
        preferences.put(username, preference);
    }

    public String getPreference(String username) {
        if (preferences.containsKey(username)) {
            return preferences.get(username);
        } else {
            return "no preference set";
        }
    }
}
