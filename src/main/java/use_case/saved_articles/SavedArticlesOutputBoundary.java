package use_case.saved_articles;

/**
 * Saved articles use case output boundary.
 */
public interface SavedArticlesOutputBoundary {
    /**
     * Prepares the success view for the News Case.
     * @param savedArticleOutputData output data
     */
    void prepareSuccessView(SavedArticleOutputData savedArticleOutputData);

    /**
     * Prepares the failure view for the News Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
