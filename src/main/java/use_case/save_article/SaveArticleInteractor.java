package use_case.save_article;

import entity.Article;

import java.util.List;
import java.util.Map;

/**
 * Save Article Interactor.
 */
public class SaveArticleInteractor implements SaveArticleInputBoundary {
    private final SaveArticleDataAccessInterface saveArticleDataAccessObject;
    private final SaveArticleOutputBoundary saveArticlePresenter;

    public SaveArticleInteractor(SaveArticleDataAccessInterface saveArticleDataAccessInterface,
                                 SaveArticleOutputBoundary saveArticleOutputBoundary) {
        this.saveArticleDataAccessObject = saveArticleDataAccessInterface;
        this.saveArticlePresenter = saveArticleOutputBoundary;
    }

    @Override
    public void execute(SaveArticleInputData saveArticleInputData) {
        final Article article = saveArticleInputData.getArticle();
        final Map<String, List<Article>> articles = saveArticleDataAccessObject.getUserArticles();

        if (articles.containsKey(article.getCategory()) && articles.get(article.getCategory()).contains(article)) {
            saveArticlePresenter.prepareFailView("Article already saved.");
        }
        else {
            saveArticleDataAccessObject.saveArticle(article);
            final SaveArticleOutputData saveArticleOutputData = new SaveArticleOutputData(article, false);
            saveArticlePresenter.prepareSuccessView(saveArticleOutputData);
        }

    }
}
