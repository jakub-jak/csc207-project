package use_case.login;

import java.util.List;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final List<String> categories;
    private final boolean useCaseFailed;

    public LoginOutputData(String username, List<String> categories, Boolean useCaseFailed) {
        this.username = username;
        this.categories = categories;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getCategories() { return categories; }
}
