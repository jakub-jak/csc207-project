package use_case.unsave_article;

import java.util.List;
import java.util.Map;

import entity.Article;

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
        final Map<String, List<Article>> articlesMap = unsaveArticleDataAccessObject.getUserArticles();

        if (containsArticle(articlesMap, article)) {
            unsaveArticleDataAccessObject.removeArticle(article);
            final UnsaveArticleOutputData unsaveArticleOutputData = new UnsaveArticleOutputData(article, false);
            unsaveArticlePresenter.prepareSuccessView(unsaveArticleOutputData);
        }
        else {
            unsaveArticlePresenter.prepareFailView("Article is not saved.");
        }
    }

    /**
     * Checks if the given article exists in the provided map of articles.
     * @param articlesMap A map containing lists of articles, where the key is a string;
     *                    (category) and the value is a list of articles in that category.
     * @param article The article to check for existence in the map.
     * @return {@code true} if an article with the same title, author, date, and link, otherwise {@code false}.
     */
    private boolean containsArticle(Map<String, List<Article>> articlesMap, Article article) {
        for (List<Article> articlesList : articlesMap.values()) {
            for (Article compareArticle : articlesList) {
                if (compareArticle.getTitle().equals(article.getTitle())
                        && compareArticle.getAuthor().equals(article.getAuthor())
                        && compareArticle.getDate().equals(article.getDate())
                        && compareArticle.getLink().equals(article.getLink())) {
                    return true;
                }
            }
        }
        return false;
    }
}
