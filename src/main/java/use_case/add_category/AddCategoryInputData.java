package use_case.add_category;

/**
 * The Input Data for the AddCategory Use Case.
 */
public class AddCategoryInputData {
    private final String category;

    public AddCategoryInputData(String inputCategory) {
        this.category = inputCategory;
    }

    public String getCategory() { return category; }
}
