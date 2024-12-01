package use_case.remove_category;

/**
 * The output boundary for the RemoveCategory Use Case.
 */
public interface RemoveCategoryOutputBoundary {
    /**
     * Prepares the success view for the RemoveCategory Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(RemoveCategoryOutputData outputData);

    /**
     * Prepares the failure view for the RemoveCategory Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
