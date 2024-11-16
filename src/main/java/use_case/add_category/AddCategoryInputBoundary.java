package use_case.add_category;

/**
 * Input Boundary for actions which are related to adding a category.
 */
public interface AddCategoryInputBoundary {
    /**
     * Executes the add category use case.
     * @param addCategoryInputData the input data
     */
    void execute(AddCategoryInputData addCategoryInputData);
}
