package interface_adapter.saved_articles;

import entity.Article;
import use_case.unsave_article.UnsaveArticleInputBoundary;
import use_case.unsave_article.UnsaveArticleInputData;

/**
 * The controller for the UnsaveArticle Use Case.
 */
public class UnsaveArticleController {
    private final UnsaveArticleInputBoundary unsaveArticleUseCaseInteractor;

    public UnsaveArticleController(UnsaveArticleInputBoundary unsaveArticleUseCaseInteractor) {
        this.unsaveArticleUseCaseInteractor = unsaveArticleUseCaseInteractor;
    }

    /**
     * Executes the SaveArticle Use Case.
     * @param article the article to remove
     */
    public void execute(Article article) {
        final UnsaveArticleInputData unsaveArticleInputData = new UnsaveArticleInputData(article);
        unsaveArticleUseCaseInteractor.execute(unsaveArticleInputData);
    }
}
