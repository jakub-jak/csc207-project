package use_case.news;

/**
 * News Output Boundary for the DAI.
 */
public interface NewsOutputBoundary {
    /**
     * Prepares the success view for the News Case.
     */
    void prepareSuccessView();

    /**
     * Prepares the failure view for the News Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
