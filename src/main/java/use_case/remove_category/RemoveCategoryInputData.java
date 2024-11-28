package use_case.remove_category;

/**
 * The Input Data for the RemoveCategory Use Case.
 */
public class RemoveCategoryInputData {
    private final String category;

    public RemoveCategoryInputData(String inputCategory) {
        this.category = inputCategory;
    }

    public String getCategory() { return category; }
}
