package interface_adapter.loggedin;

import java.util.ArrayList;
import java.util.List;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {
    private String username = "";
    private List<String> categoriesList = new ArrayList<>();


    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        categoriesList = copy.categoriesList;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public LoggedInState() {

    }

    public String getUsername() {
        return username;
    }

    public List<String> getCategoriesList() { return categoriesList; }

    public void setCategoriesList(List<String> categoriesList) { this.categoriesList = categoriesList; }

    public void setUsername(String username) {
        this.username = username;
    }
}
