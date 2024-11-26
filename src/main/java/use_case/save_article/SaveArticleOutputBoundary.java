package use_case.save_article;

/**
 * The output boundary for the SaveArticle Use Case.
 */
public interface SaveArticleOutputBoundary {
    /**
     * Prepares the success view for the SaveArticle Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(SaveArticleOutputData outputData);

    /**
     * Prepares the failure view for the SaveArticle Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
