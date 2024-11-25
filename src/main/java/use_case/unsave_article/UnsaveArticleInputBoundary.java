package use_case.unsave_article;

/**
 * Input Boundary for actions which are related to un-saving an article.
 */
public interface UnsaveArticleInputBoundary {
    /**
     * Executes the save article use case.
     * @param unsaveArticleInputData the input data
     */
    void execute(UnsaveArticleInputData unsaveArticleInputData);
}
