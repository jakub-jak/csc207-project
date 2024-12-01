package use_case.remove_category;

/**
 * Output Data for the RemoveCategory Use Case.
 */
public class RemoveCategoryOutputData {
    private final String category;
    private final boolean useCaseFailed;

    public RemoveCategoryOutputData(String category, boolean useCaseFailed) {
        this.category = category;
        this.useCaseFailed = useCaseFailed;
    }

    public String getCategory() {
        return category;
    }
}
