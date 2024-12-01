package use_case.share_article;

/**
 * Share article output boundary.
 */
public interface ShareArticleOutputBoundary {
    /**
     * Prepare the success view.
     * @param outputData output data
     */
    void prepareSuccessView(ShareArticleOutputData outputData);

    /**
     * Prepare the fail view.
     * @param errorMessage error
     */
    void prepareFailView(String errorMessage);
}
