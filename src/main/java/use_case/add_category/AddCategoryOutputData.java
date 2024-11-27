package use_case.add_category;

/**
 * Output Data for the AddCategory Use Case.
 */
public class AddCategoryOutputData {
    private final String category;
    private final boolean useCaseFailed;

    public AddCategoryOutputData(String category, boolean useCaseFailed) {
        this.category = category;
        this.useCaseFailed = useCaseFailed;
    }

    public String getCategory() { return category; }
}
