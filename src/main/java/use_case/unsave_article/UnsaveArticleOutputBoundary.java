package use_case.unsave_article;

/**
 * The output boundary for the UnsaveArticle Use Case.
 */
public interface UnsaveArticleOutputBoundary {
    /**
     * Prepares the success view for the UnsaveArticle Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(UnsaveArticleOutputData outputData);

    /**
     * Prepares the failure view for the SaveArticle Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
