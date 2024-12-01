package use_case.save_article;

/**
 * Input Boundary for actions which are related to saving an article.
 */
public interface SaveArticleInputBoundary {
    /**
     * Executes the save article use case.
     * @param saveArticleInputData the input data
     */
    void execute(SaveArticleInputData saveArticleInputData);
}
