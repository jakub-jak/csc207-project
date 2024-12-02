package use_case.unsave_article;

import entity.Article;

import java.util.List;
import java.util.Map;

/**
 * Unsave Article Use Case Interactor.
 */
public class UnsaveArticleInteractor implements UnsaveArticleInputBoundary {
    private final UnsaveArticleDataAccessInterface unsaveArticleDataAccessObject;
    private final UnsaveArticleOutputBoundary unsaveArticlePresenter;

    public UnsaveArticleInteractor(UnsaveArticleDataAccessInterface unsaveArticleDataAccessInterface,
                                 UnsaveArticleOutputBoundary unsaveArticleOutputBoundary) {
        this.unsaveArticleDataAccessObject = unsaveArticleDataAccessInterface;
        this.unsaveArticlePresenter = unsaveArticleOutputBoundary;
    }

    @Override
    public void execute(UnsaveArticleInputData unsaveArticleInputData) {
        final Article article = unsaveArticleInputData.getArticle();
        final Map<String, List<Article>> articles = unsaveArticleDataAccessObject.getUserArticles();

        if (!articles.containsKey(article.getCategory()) || !articles.get(article.getCategory()).contains(article)) {
            unsaveArticlePresenter.prepareFailView("Article is not saved.");
        }
        else {
            unsaveArticleDataAccessObject.removeArticle(article);
            final UnsaveArticleOutputData unsaveArticleOutputData = new UnsaveArticleOutputData(article, false);
            unsaveArticlePresenter.prepareSuccessView(unsaveArticleOutputData);
        }
    }
}
