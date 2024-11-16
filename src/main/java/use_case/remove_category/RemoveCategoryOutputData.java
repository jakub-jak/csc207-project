package use_case.remove_category;

/**
 * Output Data for the RemoveCategory Use Case.
 */
public class RemoveCategoryOutputData {
    private final String username;
    private final String category;
    private final boolean useCaseFailed;

    public RemoveCategoryOutputData(String username, String category, boolean useCaseFailed) {
        this.username = username;
        this.category = category;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
    }

    public String getCategory() { return category; }
}
