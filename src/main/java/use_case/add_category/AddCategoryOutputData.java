package use_case.add_category;

/**
 * Output Data for the AddCategory Use Case.
 */
public class AddCategoryOutputData {
    private final String username;
    private final String category;
    private final boolean useCaseFailed;

    public AddCategoryOutputData(String username, String category, boolean useCaseFailed) {
        this.username = username;
        this.category = category;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
    }

    public String getCategory() { return category; }

}
