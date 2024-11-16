package use_case.remove_category;

/**
 * The Input Data for the RemoveCategory Use Case.
 */
public class RemoveCategoryInputData {
    private final String username;
    private final String category;

    public RemoveCategoryInputData(String currentUsername, String inputCategory) {
        this.username = currentUsername;
        this.category = inputCategory;
    }

    public String getUsername() { return username; }

    public String getCategory() { return category; }
}
