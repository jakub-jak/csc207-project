package use_case.remove_category;

/**
 * Input Boundary for actions which are related to removing a category.
 */
public interface RemoveCategoryInputBoundary {
    /**
     * Executes the add category use case.
     * @param removeCategoryInputData the input data
     */
    void execute(RemoveCategoryInputData removeCategoryInputData);
}
