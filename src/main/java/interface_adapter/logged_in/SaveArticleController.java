package interface_adapter.logged_in;

import entity.Article;
import use_case.save_article.SaveArticleInputBoundary;
import use_case.save_article.SaveArticleInputData;

/**
 * The controller for the SaveArticle Use Case.
 */
public class SaveArticleController {
    private final SaveArticleInputBoundary saveArticleUseCaseInteractor;

    public SaveArticleController(SaveArticleInputBoundary saveArticleUseCaseInteractor) {
        this.saveArticleUseCaseInteractor = saveArticleUseCaseInteractor;
    }

    /**
     * Executes the SaveArticle Use Case.
     * @param article the article to add
     */
    public void execute(Article article) {
        final SaveArticleInputData saveArticleInputData = new SaveArticleInputData(article);
        saveArticleUseCaseInteractor.execute(saveArticleInputData);
    }
}
