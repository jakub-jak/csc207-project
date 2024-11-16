package use_case.add_category;

/**
 * The output boundary for the AddCategory Use Case.
 */
public interface AddCategoryOutputBoundary {
    /**
     * Prepares the success view for the AddCategory Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(AddCategoryOutputData outputData);

    /**
     * Prepares the failure view for the AddCategory Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
