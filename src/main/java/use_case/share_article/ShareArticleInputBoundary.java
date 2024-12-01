package use_case.share_article;

/**
 * Input Boundary for actions which are related to sharing an article.
 */
public interface ShareArticleInputBoundary {
    /**
     * Executes the share article use case.
     * @param shareArticleInputData the input data
     * @throws Exception exception
     */
    void execute(ShareArticleInputData shareArticleInputData) throws Exception;
}
