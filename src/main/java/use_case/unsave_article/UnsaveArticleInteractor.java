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

        unsaveArticleDataAccessObject.removeArticle(article);
        final UnsaveArticleOutputData unsaveArticleOutputData = new UnsaveArticleOutputData(article, false);
        unsaveArticlePresenter.prepareSuccessView(unsaveArticleOutputData);
    }
}
