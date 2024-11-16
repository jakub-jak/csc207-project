package use_case.add_category;

/**
 * The Input Data for the AddCategory Use Case.
 */
public class AddCategoryInputData {
    private final String username;
    private final String category;

    public AddCategoryInputData(String currentUsername, String inputCategory) {
        this.username = currentUsername;
        this.category = inputCategory;
    }

    public String getUsername() { return username; }

    public String getCategory() { return category; }
}
